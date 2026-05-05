package ru.joutak.plugin.services.processor;

import lombok.extern.slf4j.Slf4j;
import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.logging.PluginLogger;
import ru.joutak.plugin.services.metrics.MetricsService;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.retry.RetryEvent;
import ru.joutak.plugin.services.retry.RetryQueueService;
import ru.joutak.plugin.services.sender.HttpSender;

import java.util.List;

@Slf4j
public class BatchProcessor {
    private final EventQueueService queueService;
    private final HttpSender sender;
    private final RetryQueueService retryService;
    private final MetricsService metrics;
    private final PluginLogger logger;

    private final int batchSize;
    private final int retryMaxAttempts;

    public BatchProcessor(EventQueueService queueService, HttpSender sender, RetryQueueService retryService, MetricsService metrics, PluginLogger logger, int batchSize, int retryMaxAttempts) {
        this.queueService = queueService;
        this.sender = sender;
        this.retryService = retryService;
        this.metrics = metrics;
        this.logger = logger;
        this.batchSize = batchSize;
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public void process() {
        List<KillEvent> batch = queueService.drainBatch(batchSize);

        if (batch.isEmpty()) return;

        logger.debug("Processing batch size=" + batch.size());
        send(batch, 1);
        processRetries();
    }

    private void send(List<KillEvent> batch, int attempt) {
        sender.send(batch).thenAccept(success -> {
            if (success) {
                metrics.incSent(batch.size());
                logger.debug("Batch sent size=" + batch.size());
            } else {
                metrics.incFailed(batch.size());

                if (attempt >= retryMaxAttempts) {
                    metrics.incDropped(batch.size());
                    logger.warn("Batch dropped size=" + batch.size());
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

    private void send(KillEvent event, int attempt) {
        sender.send(List.of(event)).thenAccept(success -> {
            if (success) {
                metrics.incSent(1);
            } else {
                if (attempt >= retryMaxAttempts) {
                    metrics.incDropped(1);
                    logger.warn("Retry dropped event after max attempts");
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
            send(retry.event(), retry.attempt());
        }
    }
}
