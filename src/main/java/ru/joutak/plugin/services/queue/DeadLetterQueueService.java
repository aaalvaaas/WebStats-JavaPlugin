package ru.joutak.plugin.services.queue;

import ru.joutak.plugin.model.KillEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeadLetterQueueService {
    private final Queue<KillEvent> dlq = new ConcurrentLinkedQueue<>();
    private final boolean enabled;

    public DeadLetterQueueService(boolean enabled) {
        this.enabled = enabled;
    }

    public void offer(KillEvent event) {
        if (enabled) dlq.offer(event);
    }

    public int size() {
        return dlq.size();
    }
}
