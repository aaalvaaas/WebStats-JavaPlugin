package ru.joutak.plugin.model;

import java.util.UUID;

public record KillEvent(UUID playerId, long timestamp) {
    public KillEvent(UUID playerId) {
        this(playerId, System.currentTimeMillis());
    }
}
