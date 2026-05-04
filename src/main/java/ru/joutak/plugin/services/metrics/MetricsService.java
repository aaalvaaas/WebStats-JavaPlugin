package ru.joutak.plugin.services.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricsService {
    private final AtomicInteger sent = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger retried = new AtomicInteger();
    private final AtomicInteger dropped = new AtomicInteger();

    public void incSent(int n) {
        sent.addAndGet(n);
    }

    public void incFailed(int n) {
        failed.addAndGet(n);
    }

    public void incRetried(int n) {
        retried.addAndGet(n);
    }

    public void incDropped(int n) {
        dropped.addAndGet(n);
    }

    public String snapshot(int queueSize, int retrySize) {
        return "sent=" + sent.get()
                + ", failed=" + failed.get()
                + ", retried=" + retried.get()
                + ", dropped=" + dropped.get()
                + ", queueSize=" + queueSize
                + ", retrySize=" + retrySize;
    }
}
