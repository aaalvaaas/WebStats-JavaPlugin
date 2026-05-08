package ru.joutak.plugin.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig {
    private final JavaPlugin plugin;

    private String backendUrl;

    private String eventsEndpoint;
    private String statsEndpoint;
    private String leaderboardEndpoint;

    @Getter
    private int batchSize;
    @Getter
    private int batchIntervalMs;
    @Getter
    private int batchMinSize;
    @Getter
    private int batchMaxSize;

    @Getter
    private int rateLimit;
    @Getter
    private int windowMs;

    @Getter
    private int queueMaxSize;

    @Getter
    private int initialDelayMs;

    @Getter
    private int retryMaxAttempts;
    @Getter
    private int retryDelayTicks;
    @Getter
    private int retryMaxDelayMs;
    @Getter
    private int retryJitterMs;

    @Getter
    private boolean backpressureEnabled;
    @Getter
    private double backpressureThreshold;

    @Getter
    private boolean dlqEnabled;

    @Getter
    private boolean debugEnabled;

    public PluginConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        this.backendUrl = config.getString("backend.url");

        this.eventsEndpoint = config.getString("api.events");
        this.statsEndpoint = config.getString("api.stats");
        this.leaderboardEndpoint = config.getString("api.leaderboard");

        this.batchSize = config.getInt("batch.size");
        this.batchIntervalMs = config.getInt("batch.intervalMs");
        this.batchMinSize = config.getInt("batch.minSize");
        this.batchMaxSize = config.getInt("batch.maxSize");

        this.rateLimit = config.getInt("rateLimit.perSecond");
        this.windowMs = config.getInt("rateLimit.windowMs");

        this.queueMaxSize = config.getInt("queue.maxSize");

        this.initialDelayMs = config.getInt("scheduler.initialDelayMs");

        this.retryMaxAttempts = config.getInt("retry.maxAttempts");
        this.retryDelayTicks = config.getInt("retry.delayMs");
        this.retryMaxDelayMs = config.getInt("retry.maxDelayMs");
        this.retryJitterMs = config.getInt("retry.jitterMs;");

        this.backpressureEnabled = config.getBoolean("backpressure.enabled");
        this.backpressureThreshold = config.getDouble("backpressure.threshold");

        this.dlqEnabled = config.getBoolean("dlq.enabled");

        this.debugEnabled = config.getBoolean("debug.enabled");
    }

    public String getEventsUrl() {
        return backendUrl + eventsEndpoint;
    }

    public String getStatsUrl() {
        return backendUrl + statsEndpoint;
    }

    public String getLeaderboardUrl() {
        return backendUrl + leaderboardEndpoint;
    }
}
