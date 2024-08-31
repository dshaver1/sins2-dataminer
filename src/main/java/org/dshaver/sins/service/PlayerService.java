package org.dshaver.sins.service;

import org.dshaver.sins.domain.Manifest;
import org.dshaver.sins.domain.ingest.player.Player;
import org.dshaver.sins.domain.ingest.player.PlayerType;

import java.util.Optional;

public class PlayerService {
    private final GameFileService gameFileService;
    private final ManifestService manifestService;

    private Manifest<PlayerType, Player> playerManifest;

    public PlayerService(GameFileService gameFileService, ManifestService manifestService) {
        this.gameFileService = gameFileService;
        this.manifestService = manifestService;
    }

    public Optional<Player> getPlayer(String playerId) {
        if (playerManifest == null) {
            playerManifest = manifestService.loadPlayerManifest();
        }

        return playerManifest.getById(playerId);
    }

    public Manifest<PlayerType, Player> getPlayerManifest() {
        if (playerManifest == null) {
            playerManifest = manifestService.loadPlayerManifest();
        }

        return playerManifest;
    }
}
