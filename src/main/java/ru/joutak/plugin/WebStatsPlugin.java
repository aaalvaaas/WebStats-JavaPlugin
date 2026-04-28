package ru.joutak.plugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class WebStatsPlugin extends JavaPlugin {
    @Getter
    private static WebStatsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info(
                String.format("Плагин %s версии %s включен!", getPluginMeta().getName(), getPluginMeta().getVersion())
        );
    }

    @Override
    public void onDisable() {

    }
}
