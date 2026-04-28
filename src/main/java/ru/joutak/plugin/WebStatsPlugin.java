package ru.joutak.plugin;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.joutak.plugin.config.PluginConfig;
import ru.joutak.plugin.listeners.KillListener;

public final class WebStatsPlugin extends JavaPlugin {
    @Getter
    private static WebStatsPlugin instance;
    private PluginConfig config;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        Bukkit.getPluginManager().registerEvents(new KillListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
