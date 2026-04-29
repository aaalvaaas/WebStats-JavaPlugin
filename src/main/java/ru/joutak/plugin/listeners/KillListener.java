package ru.joutak.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.joutak.plugin.services.KillService;
import ru.joutak.plugin.services.MessageService;

public class KillListener implements Listener {
    private final KillService killService;
    private final MessageService messageService;

    public KillListener(KillService killService, MessageService messageService) {
        this.killService = killService;
        this.messageService = messageService;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer == null) return;

        killService.addKill(killer.getUniqueId());

        messageService.sendKillMessage(killer, killService.getKills(killer.getUniqueId()));
    }
}
