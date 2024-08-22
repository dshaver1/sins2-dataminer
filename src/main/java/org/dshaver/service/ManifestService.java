package org.dshaver.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.dshaver.domain.Manifest;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ManifestService {

    public Manifest loadUnitItemManifest() {
        Manifest unitItemManifest = FileTools.loadUnitItemManifest();

        System.out.println(STR."Loaded \{unitItemManifest.getIds().size()} unitItemIds");

        // Organize by id
        Map<String, UnitItem> unitItemMap = unitItemManifest.getIds().stream()
                .map(FileTools::readUnitItemFile)
                .collect(Collectors.toMap(UnitItem::getId, Function.identity()));

        unitItemManifest.setUnitItemsMap(unitItemMap);

        // Organize by type
        Multimap<UnitItemType, UnitItem> typeIndex = ArrayListMultimap.create();
        unitItemMap.values().forEach(unitItem -> typeIndex.put(unitItem.getItemType(), unitItem));
        unitItemManifest.setTypeIndex(typeIndex);

        return unitItemManifest;
    }
}
