package org.dshaver.service;

import org.dshaver.domain.Manifest;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.util.Collection;

public class PlanetService {

    private final ManifestService manifestService;

    public PlanetService(ManifestService manifestService) {
        this.manifestService = manifestService;
    }

    public Collection<UnitItem> loadPlanetItems() {
        Manifest<UnitItemType, UnitItem> unitItemManifest = manifestService.loadUnitItemManifest();

        Collection<UnitItem> planetComponents = unitItemManifest.getByType(UnitItemType.planet_component);

        System.out.println(STR."Found \{planetComponents.size()}");

        return planetComponents;
    }
}
