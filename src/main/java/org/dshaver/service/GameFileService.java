package org.dshaver.service;

import org.dshaver.domain.gamefiles.unit.Unit;

import java.util.Map;

public class GameFileService {

    private Map<String, String> localizedText;

    public void loadLocalizedText() {
        localizedText = FileTools.readLocalizedTextFile();
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
