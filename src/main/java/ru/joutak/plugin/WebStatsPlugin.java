package ru.joutak.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.joutak.plugin.config.PluginConfig;
import ru.joutak.plugin.listeners.KillListener;
import ru.joutak.plugin.services.limiter.RateLimiterService;
import ru.joutak.plugin.services.logging.PluginLogger;
import ru.joutak.plugin.services.metrics.MetricsService;
import ru.joutak.plugin.services.processor.BatchProcessor;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.ui.MessageService;
import ru.joutak.plugin.services.retry.RetryQueueService;
import ru.joutak.plugin.services.sender.HttpSender;

public final class WebStatsPlugin extends JavaPlugin {

    private PluginConfig config;
    private KillService killService;
    private MessageService messageService;
    private EventQueueService eventQueueService;
    private HttpSender sender;
    private BatchProcessor processor;
    private RateLimiterService rateLimiter;
    private RetryQueueService retryService;
    private MetricsService metrics;
    private PluginLogger logger;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);

        this.killService = new KillService();
        this.messageService = new MessageService();
        this.eventQueueService = new EventQueueService(logger, config.getQueueMaxSize());
        this.retryService = new RetryQueueService();
        this.metrics = new MetricsService();
        this.logger = new PluginLogger(this, config.isDebugEnabled());

        this.rateLimiter = new RateLimiterService(config.getRateLimit(), config.getWindowMillis());
        this.sender = new HttpSender(config.getEventsUrl(), rateLimiter, logger);
        this.processor = new BatchProcessor(eventQueueService, sender, retryService, metrics, logger, config.getBatchSize(), config.getRetryMaxAttempts());

        Bukkit.getPluginManager().registerEvents(new KillListener(killService, messageService, eventQueueService), this);

        logger.info("WebStats plugin enabled!");

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
