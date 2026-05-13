package ru.joutak.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.joutak.plugin.services.logging.PluginLogger;
import ru.joutak.plugin.services.metrics.MetricsService;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.queue.RetryQueueService;
import ru.joutak.plugin.ui.MessageService;
import ru.joutak.plugin.ui.Messages;

public class WebStatsCommand implements CommandExecutor {
    private final PluginLogger logger;
    private final MetricsService metrics;
    private final MessageService messageService;
    private final int eventQueueSize;
    private final int retryQueueSize;
    private final int dlqQueueSize;
    private final double queuePressure;
    private final int currBatchSize;

    public WebStatsCommand(PluginLogger logger, MetricsService metrics, MessageService messageService, int eventQueueSize, int retryQueueSize, int dlqQueueSize, double queuePressure, int currBatchSize) {
        this.logger = logger;
        this.metrics = metrics;
        this.messageService = messageService;
        this.eventQueueSize = eventQueueSize;
        this.retryQueueSize = retryQueueSize;
        this.dlqQueueSize = dlqQueueSize;
        this.queuePressure = queuePressure;
        this.currBatchSize = currBatchSize;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            messageService.send(commandSender, Messages.UNKNOWN_COMMAND);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "debug" -> handleDebug(commandSender, args);
            case "metrics" -> handleMetrics(commandSender);
            case "queue" -> handleQueue(commandSender);

            default -> messageService.send(commandSender, Messages.UNKNOWN_COMMAND);
        }

        return true;
    }

    private void handleDebug(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            messageService.send(commandSender, Messages.DEBUG_USAGE);
            return;
        }

        boolean debugEnabled = args[1].equalsIgnoreCase("on");

        logger.setDebugEnabled(debugEnabled);
        messageService.send(commandSender, Messages.debugState(debugEnabled));
    }

    private void handleMetrics(CommandSender commandSender) {
        String snapshot = metrics.snapshot(eventQueueSize, retryQueueSize, dlqQueueSize, queuePressure, currBatchSize);
        messageService.send(commandSender, Messages.metrics(snapshot));
    }

    private void handleQueue(CommandSender commandSender) {
        messageService.send(commandSender, Messages.queue(eventQueueSize, retryQueueSize));
    }
}
