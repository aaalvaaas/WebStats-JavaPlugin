package ru.joutak.plugin.services.limiter;

public class RateLimiterService {
    private final int limit;
    private final long windowMillis;

    private double tokens;
    private long lastRefillTime;

    public RateLimiterService(int limit, long windowMillis) {
        this.limit = limit;
        this.windowMillis = windowMillis;

        this.tokens = limit;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allow() {
        refill();

        if (tokens >= 1) {
            tokens--;
            return true;
        }

        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        double refillTokens = (elapsed / (double) windowMillis) * limit;

        if (refillTokens > 0) {
            tokens = Math.min(limit, tokens + refillTokens);
            lastRefillTime = now;
        }
    }
}
