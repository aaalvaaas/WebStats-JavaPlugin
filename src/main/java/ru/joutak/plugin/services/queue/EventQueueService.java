package ru.joutak.plugin.services.queue;

import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.logging.PluginLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueueService {
    private final Queue<KillEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private final PluginLogger logger;
    private final int maxSize;

    public EventQueueService(PluginLogger logger, int maxSize) {
        this.logger = logger;
        this.maxSize = maxSize;
    }

    public boolean offer(KillEvent event) {
        if (size() >= maxSize) {
            logger.warn("Event queue full, dropping event");
            return false;
        }
        return eventQueue.offer(event);
    }

    public List<KillEvent> drainBatch(int batchSize) {
        List<KillEvent> batch = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            KillEvent e = eventQueue.poll();
            if (e == null) break;
            batch.add(e);
        }

        return batch;
    }

    public int size() {
        return eventQueue.size();
    }
}
