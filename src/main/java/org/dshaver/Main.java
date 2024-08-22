package org.dshaver;

import org.dshaver.domain.Manifest;
import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unit.UnitType;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.service.*;

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

        // Write planet upgrades file
        Collection<UnitItem> planets = planetService.loadPlanetsUpgrades();

        FileTools.writePlanetUpgradesJsonFile(planets);

        System.out.println("Done printing all!");
    }
}