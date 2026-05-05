package ru.joutak.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.ui.MessageService;
import ru.joutak.plugin.ui.Messages;

import java.util.UUID;

public class KillListener implements Listener {
    private final KillService killService;
    private final MessageService messageService;
    private final EventQueueService eventQueueService;

    public KillListener(KillService killService, MessageService messageService, EventQueueService eventQueueService) {
        this.killService = killService;
        this.messageService = messageService;
        this.eventQueueService = eventQueueService;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer == null) return;

        UUID killerId = killer.getUniqueId();
        killService.addKill(killerId);

        messageService.send(killer, Messages.killMessage(killService.getKills(killerId)));

        eventQueueService.offer(new KillEvent(killerId));
    }
}
