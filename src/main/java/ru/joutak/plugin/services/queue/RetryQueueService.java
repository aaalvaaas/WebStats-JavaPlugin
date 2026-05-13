package ru.joutak.plugin.services.queue;

import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.model.RetryEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RetryQueueService {
    private final Queue<RetryEvent> retryQueue = new ConcurrentLinkedQueue<>();
    private final long retryDelayMs;
    private final long retryMaxDelayMs;
    private final long retryJitterMs;

    public RetryQueueService(long retryDelayMs, long retryMaxDelayMs, long retryJitterMs) {
        this.retryDelayMs = retryDelayMs;
        this.retryMaxDelayMs = retryMaxDelayMs;
        this.retryJitterMs = retryJitterMs;
    }

    public void offer(KillEvent event, int attempt) {
        long delay = calculateRetryDelay(attempt);

        retryQueue.offer(new RetryEvent(event, attempt, System.currentTimeMillis() + delay));
    }

    private long calculateRetryDelay(int attempt) {
        long exponential = retryDelayMs * (1L << attempt);
        long capped = Math.min(exponential, retryMaxDelayMs);

        return capped + (long) (Math.random() * retryJitterMs);
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
