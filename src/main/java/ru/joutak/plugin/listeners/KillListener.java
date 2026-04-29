package ru.joutak.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.joutak.plugin.services.KillService;

public class KillListener implements Listener {
    private final KillService killService;

    public KillListener(KillService killService) {
        this.killService = killService;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer == null) return;

        killService.addKill(killer.getUniqueId());

        killer.sendMessage("§aYou killed a mob!");
    }
}
