package ru.joutak.plugin.services.processor;

import lombok.extern.slf4j.Slf4j;
import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.logging.PluginLogger;
import ru.joutak.plugin.services.metrics.MetricsService;
import ru.joutak.plugin.services.queue.DeadLetterQueueService;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.model.RetryEvent;
import ru.joutak.plugin.services.queue.RetryQueueService;
import ru.joutak.plugin.services.sender.HttpSender;

import java.util.List;

@Slf4j
public class BatchProcessor {
    private final EventQueueService queueService;
    private final HttpSender sender;
    private final RetryQueueService retryService;
    private final MetricsService metrics;
    private final PluginLogger logger;
    private final DeadLetterQueueService dlqService;

    private final int batchSize;
    private final int batchMinSize;
    private int currBatchSize;
    private final int queueMaxSize;
    private final int retryMaxAttempts;
    private final double backpressureThreshold;
    private final boolean backpressureEnabled;

    public BatchProcessor(EventQueueService queueService, HttpSender sender, RetryQueueService retryService, MetricsService metrics, PluginLogger logger, DeadLetterQueueService dlqService, int batchSize, int batchMinSize, int queueMaxSize, int retryMaxAttempts, double backpressureThreshold, boolean backpressureEnabled) {
        this.queueService = queueService;
        this.sender = sender;
        this.retryService = retryService;
        this.metrics = metrics;
        this.logger = logger;
        this.dlqService = dlqService;
        this.batchSize = batchSize;
        this.batchMinSize = batchMinSize;
        this.currBatchSize = batchSize;
        this.queueMaxSize = queueMaxSize;
        this.retryMaxAttempts = retryMaxAttempts;
        this.backpressureThreshold = backpressureThreshold;
        this.backpressureEnabled = backpressureEnabled;
    }

    public void process() {
        applyBackpressure();

        List<KillEvent> batch = queueService.drainBatch(batchSize);

        if (!batch.isEmpty()) {
            logger.debug("Processing batch size=" + batch.size());
            sendBatch(batch, 1);
        }

        processRetries();
    }

    private void applyBackpressure() {
        if (!backpressureEnabled) return;

        double usage = (double) queueService.size() / queueMaxSize;
        if (usage >= backpressureThreshold) {
            logger.warn("Backpressure activated");
            currBatchSize = Math.max(batchMinSize, currBatchSize / 2);
            return;
        }

        currBatchSize = batchSize;
    }

    private void sendBatch(List<KillEvent> batch, int attempt) {
        sender.send(batch).thenAccept(success -> {
            if (success) {
                metrics.incSent(batch.size());
                logger.debug("Batch sent size=" + batch.size());
            } else {
                metrics.incFailed(batch.size());

                if (attempt >= retryMaxAttempts) {
                    metrics.incDropped(batch.size());
                    for (KillEvent event : batch) dlqService.offer(event);
                    logger.warn("Batch dropped to dlq");
                } else {
                    for (KillEvent event : batch) {
                        retryService.offer(event, attempt);
                        metrics.incRetried(1);
                    }
                    logger.debug("Batch moved to retry attempt=" + attempt);
                }
            }
        });
    }

    private void sendEvent(KillEvent event, int attempt) {
        sender.send(List.of(event)).thenAccept(success -> {
            if (success) {
                metrics.incSent(1);
            } else {
                if (attempt >= retryMaxAttempts) {
                    metrics.incDropped(1);
                    dlqService.offer(event);
                    logger.warn("Retry dropped event to dlq after max attempts");
                } else {
                    metrics.incRetried(1);
                    retryService.offer(event, attempt + 1);
                    logger.debug("Retry scheduled attempt=" + (attempt + 1));
                }
            }
        });
    }

    private void processRetries() {
        RetryEvent retry;

        while ((retry = retryService.poll()) != null) {
            if (retry.nextRetryAtMs() > System.currentTimeMillis()) {
                retryService.offer(retry.event(), retry.attempt());
                continue;
            }

            sendEvent(retry.event(), retry.attempt());
        }
    }
}
