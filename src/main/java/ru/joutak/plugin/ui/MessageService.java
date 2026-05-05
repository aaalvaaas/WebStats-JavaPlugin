package ru.joutak.plugin.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageService {
    public void send(Player player, Component message) {
        player.sendMessage(message);
    }

    public void send(CommandSender commandSender, Component message) {
        commandSender.sendMessage(message);
    }
}
