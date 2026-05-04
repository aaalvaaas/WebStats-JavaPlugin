package ru.joutak.plugin.services.retry;

import ru.joutak.plugin.model.KillEvent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RetryQueueService {
    private final Queue<RetryEvent> retryQueue = new ConcurrentLinkedQueue<>();

    public void offer(List<KillEvent> events, int attempt) {
        retryQueue.offer(new RetryEvent(events, attempt));
    }

    public RetryEvent poll() {
        return retryQueue.poll();
    }

    public boolean isEmpty() {
        return retryQueue.isEmpty();
    }

    public int size() {
        return retryQueue.size();
    }
}
