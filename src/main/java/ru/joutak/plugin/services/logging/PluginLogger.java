package ru.joutak.plugin.services.logging;

import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLogger {
    private final JavaPlugin plugin;
    private boolean debugEnabled;

    public PluginLogger(JavaPlugin plugin, boolean debugEnabled) {
        this.plugin = plugin;
        this.debugEnabled = debugEnabled;
    }

    public void info(String message) {
        plugin.getLogger().info(message);
    }

    public void warn(String message) {
        plugin.getLogger().warning(message);
    }


    public void debug(String message) {
        if (debugEnabled) {
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }

    public void setDebugEnabled(boolean debugEnabled) {
        info("[DEBUG MODE = " + (debugEnabled ? "TRUE" : "FALSE") + "]");
        this.debugEnabled = debugEnabled;
    }
}
