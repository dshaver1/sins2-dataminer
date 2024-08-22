package org.dshaver.service;

import org.dshaver.domain.gamefiles.unit.Unit;

import java.util.Map;

public class GameFileService {


    private static Map<String, String> localizedText;

    private final String steamDir;

    public GameFileService(String steamDir) {
        this.steamDir = steamDir;
    }

    public void loadLocalizedText() {
        localizedText = FileTools.readLocalizedTextFile(steamDir);
    }

    public String getLocalizedText(String key) {
        if (localizedText == null) {
            loadLocalizedText();
        }

        return localizedText.get(key);
    }


    public Unit readUnitFile(String unitId) {
        return FileTools.readUnitFile(unitId);
    }
}
