package ru.joutak.plugin.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Messages {
    private Messages() {}

    public static final Component UNKNOWN_COMMAND =
            Component.text("Unknown subcommand", NamedTextColor.RED);

    public static final Component DEBUG_USAGE =
            Component.text("Usage: /webstats debug <on|off>", NamedTextColor.YELLOW);

    public static Component debugState(boolean debugEnabled) {
        return Component.text("Debug mode: ", NamedTextColor.GRAY)
                .append(Component.text(debugEnabled ? "ON" : "OFF", NamedTextColor.GREEN));
    }

    public static Component metrics(String snapshot) {
        return Component.text("Metrics:", NamedTextColor.GREEN)
                .appendNewline()
                .append(Component.text(snapshot, NamedTextColor.GRAY));
    }

    public static Component queue(int event, int retry) {
        return Component.text("Queue:", NamedTextColor.GREEN)
                .appendNewline()
                .append(Component.text("Event: " + event, NamedTextColor.GRAY))
                .appendNewline()
                .append(Component.text("Retry: " + retry, NamedTextColor.GRAY));
    }

    public static Component killMessage(int totalKills) {
        return Component.text("You killed a mob!", NamedTextColor.GREEN)
                .appendNewline()
                .append(Component.text("Kills: ", NamedTextColor.GRAY))
                .append(Component.text(totalKills, NamedTextColor.GREEN));
    }
}
