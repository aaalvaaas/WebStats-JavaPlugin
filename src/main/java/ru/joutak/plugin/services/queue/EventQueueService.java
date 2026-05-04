package ru.joutak.plugin.services.queue;

import ru.joutak.plugin.model.KillEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueueService {
    private final Queue<KillEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private final int maxSize;

    public EventQueueService(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean offer(KillEvent event) {
        if (size() >= maxSize) return false;
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
