package ru.joutak.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.joutak.plugin.config.PluginConfig;
import ru.joutak.plugin.listeners.KillListener;
import ru.joutak.plugin.services.KillService;

public final class WebStatsPlugin extends JavaPlugin {

    private PluginConfig config;
    private KillService killService;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        this.killService = new KillService();

        Bukkit.getPluginManager().registerEvents(new KillListener(killService), this);
    }

    @Override
    public void onDisable() {

    }
}
