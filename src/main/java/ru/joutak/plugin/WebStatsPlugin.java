package ru.joutak.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.joutak.plugin.config.PluginConfig;
import ru.joutak.plugin.listeners.KillListener;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.services.MessageService;

public final class WebStatsPlugin extends JavaPlugin {

    private PluginConfig config;
    private KillService killService;
    private MessageService messageService;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        this.killService = new KillService();
        this.messageService = new MessageService();

        Bukkit.getPluginManager().registerEvents(new KillListener(killService, messageService), this);

        getLogger().info("WebStats plugin enabled!");
    }

    @Override
    public void onDisable() {

    }
}
