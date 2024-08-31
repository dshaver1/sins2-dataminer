package org.dshaver.sins.service;

import org.dshaver.sins.domain.ingest.player.Player;
import org.dshaver.sins.domain.ingest.research.ResearchSubject;
import org.dshaver.sins.domain.ingest.unititem.UnitItem;

import java.nio.file.Path;
import java.util.Map;

public class GameFileService {
    private static final String PLAYER_MANIFEST_FILE_PATH = "entities/player.entity_manifest";
    private static final String UNIT_ITEM_MANIFEST_FILE_PATH = "entities/unit_item.entity_manifest";
    private static final String RESEARCH_SUBJECT_MANIFEST_FILE_PATH = "entities/research_subject.entity_manifest";
    private static Map<String, String> localizedText;

    private final String steamDir;
    private final String outputDir;

    public GameFileService(String steamDir, String outputDir) {
        this.steamDir = steamDir;
        this.outputDir = outputDir;
    }

    public Map<String, String> getLocalizedText() {
        if (localizedText == null) {
            localizedText = FileTools.readLocalizedTextFile(steamDir);
        }

        return localizedText;
    }

    public String getLocalizedTextForKey(String key) {
        return getLocalizedText().get(key);
    }

    public Player readPlayerFile(String playerId) {
        return FileTools.readPlayerFile(steamDir, playerId);
    }

    public UnitItem readUnitItemFile(String unitItemId) {
        return FileTools.readUnitItemFile(steamDir, unitItemId);
    }

    public ResearchSubject readResearchSubjectFile(String unitItemId) {
        return FileTools.readResearchSubjectFile(steamDir, unitItemId);
    }

    public Path getPlayerManifestPath() {
        return getPath(PLAYER_MANIFEST_FILE_PATH);
    }

    public Path getUnitItemManifestPath() {
        return getPath(UNIT_ITEM_MANIFEST_FILE_PATH);
    }

    public Path getResearchSubjectManifest() {
        return getPath(RESEARCH_SUBJECT_MANIFEST_FILE_PATH);
    }

    public Path getPath(String filePart) {
        return Path.of(steamDir).resolve(filePart);
    }
}
