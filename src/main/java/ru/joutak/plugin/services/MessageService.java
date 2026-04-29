package ru.joutak.plugin.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class MessageService {
    public void send(Player player, Component message) {
        player.sendMessage(message);
    }

    public void sendKillMessage(Player player, int totalKills) {
        Component message = Component.text()
                .append(Component.text("You killed a mob!").color(NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text("Kills: ").color(NamedTextColor.GRAY))
                .append(Component.text(totalKills).color(NamedTextColor.GREEN))
                .build();

        send(player, message);
    }
}
