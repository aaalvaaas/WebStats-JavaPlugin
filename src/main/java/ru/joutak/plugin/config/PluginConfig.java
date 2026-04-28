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
        this.intervalTicks = config.getInt("batch.intervalTicks");

        this.rateLimit = config.getInt("rateLimit.perSecond");
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
