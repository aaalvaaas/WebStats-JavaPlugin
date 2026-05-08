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
    private int intervalTicks;
    @Getter
    private int rateLimit;
    @Getter
    private int windowMillis;
    @Getter
    private int queueMaxSize;
    @Getter
    private int initialDelayTicks;
    @Getter
    private int retryMaxAttempts;
    @Getter
    private int retryDelayTicks;
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
        this.intervalTicks = config.getInt("batch.intervalMs");
        this.initialDelayTicks = config.getInt("scheduler.initialDelayMs");

        this.rateLimit = config.getInt("rateLimit.perSecond");
        this.windowMillis = config.getInt("rateLimit.windowMs");
        this.queueMaxSize = config.getInt("queue.maxSize");

        this.retryMaxAttempts = config.getInt("retry.maxAttempts");
        this.retryDelayTicks = config.getInt("retry.delayMs");

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
