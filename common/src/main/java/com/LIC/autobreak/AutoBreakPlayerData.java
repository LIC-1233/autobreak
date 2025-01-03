package com.LIC.autobreak;

import java.util.UUID;

public class AutoBreakPlayerData {
    private final UUID playerId;

    public AutoBreakPlayerData(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
