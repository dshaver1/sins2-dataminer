package org.dshaver.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.dshaver.domain.Manifest;
import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unit.UnitType;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dshaver.service.UnitService.getDescriptionProperty;
import static org.dshaver.service.UnitService.getNameProperty;

public class ManifestService {

    private static Map<String, String> localizedText;

    private final String steamDir;

    public ManifestService(String steamDir) {
        this.steamDir = steamDir;
    }

    public Map<String, String> getLocalizedText() {
        if (localizedText == null) {
            localizedText = FileTools.readLocalizedTextFile(steamDir);
        }

        return localizedText;
    }

    public Manifest<UnitItemType, UnitItem> loadUnitItemManifest() {
        Manifest<UnitItemType, UnitItem> unitItemManifest = FileTools.loadUnitItemManifest(steamDir);

        System.out.println(STR."Loaded \{unitItemManifest.getIds().size()} unitItemIds");

        // Organize by id
        Map<String, UnitItem> unitItemMap = unitItemManifest.getIds().stream()
                .map(id -> FileTools.readUnitItemFile(steamDir, id))
                .map(this::populateUnitItem)
                .collect(Collectors.toMap(UnitItem::getId, Function.identity()));

        unitItemManifest.setIdMap(unitItemMap);

        // Organize by type
        Multimap<UnitItemType, UnitItem> typeIndex = ArrayListMultimap.create();
        unitItemMap.values().forEach(unitItem -> typeIndex.put(unitItem.getItemType(), unitItem));
        unitItemManifest.setTypeIndex(typeIndex);

        return unitItemManifest;
    }

    private UnitItem populateUnitItem(UnitItem unitItem) {
        unitItem.setName(getLocalizedText().get(unitItem.getName()));
        unitItem.setDescription(getLocalizedText().get(unitItem.getDescription()));
        unitItem.findRace();
        unitItem.findFaction();

        return unitItem;
    }
}
