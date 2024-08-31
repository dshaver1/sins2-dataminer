package org.dshaver.sins;

import org.dshaver.sins.domain.Manifest;
import org.dshaver.sins.domain.ingest.unit.Unit;
import org.dshaver.sins.domain.ingest.unit.UnitType;
import org.dshaver.sins.domain.ingest.unititem.UnitItem;
import org.dshaver.sins.service.*;

import java.util.Collection;

public class Main {

    public static UnitService unitService;
    public static PlanetService planetService;

    public static void main(String[] args) {
        String steamDir = args[0].split("=")[1].replace("\"", "");
        System.out.println("Starting with steamdir " + steamDir);

        unitService = new UnitService(new GameFileService(steamDir), steamDir);
        planetService = new PlanetService(new ManifestService(steamDir));

        loadFilesAndExport();
    }

    public static void loadFilesAndExport() {
        // Write units file
        Manifest<UnitType, Unit> units = unitService.loadUnitManifest();

        FileTools.writeUnitsJsonFile(units.getIdMap().values());

        // Write structures file
        FileTools.writeStructuresJsonFile(units.getIdMap().values());

        // Write planet upgrades file
        Collection<UnitItem> planetsItems = planetService.loadPlanetItems();

        FileTools.writePlanetItemsJsonFile(planetsItems);

        System.out.println("Done printing all!");
    }
}