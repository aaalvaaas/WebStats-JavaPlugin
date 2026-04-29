package ru.joutak.plugin.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillService {
    private final Map<UUID, Integer> killsPerPlayers = new HashMap<>();

    public void addKill(UUID playerId) {
        killsPerPlayers.put(playerId, getKills(playerId) + 1);
    }

    public int getKills(UUID playerId) {
        return killsPerPlayers.getOrDefault(playerId, 0);
    }
}
