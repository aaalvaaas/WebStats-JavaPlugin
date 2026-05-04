package ru.joutak.plugin.services.processor;

import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.sender.HttpSender;

import java.util.List;

public class BatchProcessor {
    private final EventQueueService queueService;
    private final HttpSender sender;
    private final int batchSize;

    public BatchProcessor(EventQueueService queueService, HttpSender sender, int batchSize) {
        this.queueService = queueService;
        this.sender = sender;
        this.batchSize = batchSize;
    }

    public void process() {
        List<KillEvent> batch = queueService.drainBatch(batchSize);

        if (batch.isEmpty()) return;

        sender.send(batch).thenAccept(success -> {
            if (!success) {
                // TODO: retry system
            }
        });
    }
}
