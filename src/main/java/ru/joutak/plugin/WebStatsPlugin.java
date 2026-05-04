package ru.joutak.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.joutak.plugin.config.PluginConfig;
import ru.joutak.plugin.listeners.KillListener;
import ru.joutak.plugin.services.limiter.RateLimiterService;
import ru.joutak.plugin.services.processor.BatchProcessor;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.services.MessageService;
import ru.joutak.plugin.services.sender.HttpSender;

public final class WebStatsPlugin extends JavaPlugin {

    private PluginConfig config;
    private KillService killService;
    private MessageService messageService;
    private EventQueueService eventQueueService;
    private HttpSender sender;
    private BatchProcessor processor;
    private RateLimiterService rateLimiter;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        this.killService = new KillService();
        this.messageService = new MessageService();
        this.eventQueueService = new EventQueueService(config.getQueueMaxSize());

        this.rateLimiter = new RateLimiterService(config.getRateLimit(), config.getWindowMillis());
        this.sender = new HttpSender(config.getEventsUrl(), rateLimiter);
        this.processor = new BatchProcessor(eventQueueService, sender, config.getBatchSize());

        Bukkit.getPluginManager().registerEvents(new KillListener(killService, messageService, eventQueueService), this);

        getLogger().info("WebStats plugin enabled!");

        new BukkitRunnable() {
            @Override
            public void run() {
                processor.process();
            }
        }.runTaskTimerAsynchronously(this, config.getInitialDelayTicks(), config.getIntervalTicks());
    }

    @Override
    public void onDisable() {

    }
}
