package ru.joutak.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.joutak.plugin.model.KillEvent;
import ru.joutak.plugin.services.queue.EventQueueService;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.services.MessageService;

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
        UUID killerId = killer.getUniqueId();

        if (killer == null) return;

        killService.addKill(killerId);

        messageService.sendKillMessage(killer, killService.getKills(killerId));

        eventQueueService.add(new KillEvent(killerId));
    }
}
