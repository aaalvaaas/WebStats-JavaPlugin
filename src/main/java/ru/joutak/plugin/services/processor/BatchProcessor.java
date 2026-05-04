package ru.joutak.plugin.services.processor;

import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.retry.RetryEvent;
import ru.joutak.plugin.services.retry.RetryQueueService;
import ru.joutak.plugin.services.sender.HttpSender;

import java.util.List;

public class BatchProcessor {
    private final EventQueueService queueService;
    private final HttpSender sender;
    private final RetryQueueService retryService;
    private final int batchSize;
    private final int retryMaxAttempts;

    public BatchProcessor(EventQueueService queueService, HttpSender sender, RetryQueueService retryService, int batchSize, int retryMaxAttempts) {
        this.queueService = queueService;
        this.sender = sender;
        this.retryService = retryService;
        this.batchSize = batchSize;
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public void process() {
        List<KillEvent> batch = queueService.drainBatch(batchSize);

        if (batch.isEmpty()) return;

        send(batch, 1);
        processRetries();
    }

    private void send(List<KillEvent> batch, int attempt) {
        sender.send(batch).thenAccept(success -> {
            if (!success) {
                retryService.offer(batch, attempt);
            }
        });
    }

    private void processRetries() {
        RetryEvent retry;

        while ((retry = retryService.poll()) != null) {
            if (retry.attempt() >= retryMaxAttempts) continue;

            send(retry.events(), retry.attempt() + 1);
        }
    }
}
