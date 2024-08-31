package org.dshaver.sins.domain.ingest.player;

public enum PlayerType {
    user, npc;

    public static PlayerType getType(Player player) {
        if (player.getId().contains("vasari") || player.getId().contains("advent") || player.getId().contains("trader")) {
            return user;
        }

        return npc;
    }
}
