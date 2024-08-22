package org.dshaver;

import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.service.*;

import java.util.Collection;

public class Main {

    public static UnitService unitService;
    public static PlanetService planetService;

    public static void main(String[] args) {
        System.out.println("Starting!");

        unitService = new UnitService(new GameFileService());
        planetService = new PlanetService(new ManifestService());

        loadFilesAndExport();
    }

    public static void loadFilesAndExport() {
        // Write units file
        Collection<Unit> units = unitService.loadAllUnits();

        FileTools.writeUnitsJsonFile(units);

        // Write planet upgrades file
        Collection<UnitItem> planets = planetService.loadPlanetsUpgrades();

        FileTools.writePlanetUpgradesJsonFile(planets);

        System.out.println("Done printing all!");
    }
}