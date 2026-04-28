package ru.joutak.plugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.joutak.plugin.config.PluginConfig;

public final class WebStatsPlugin extends JavaPlugin {
    @Getter
    private static WebStatsPlugin instance;
    private PluginConfig config;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        getLogger().info("Backend URL: " + config.getEventsUrl());
    }

    @Override
    public void onDisable() {

    }
}
