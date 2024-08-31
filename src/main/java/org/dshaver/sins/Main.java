package org.dshaver.sins;

import org.apache.commons.cli.*;
import org.dshaver.sins.domain.Manifest;
import org.dshaver.sins.domain.ingest.unit.Unit;
import org.dshaver.sins.domain.ingest.unit.UnitType;
import org.dshaver.sins.domain.ingest.unititem.UnitItem;
import org.dshaver.sins.service.*;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

public class Main {
    private static final String DEFAULT_STEAM_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Sins2\\";
    private static final String DEFAULT_OUTPUT_DIR = "wiki\\";

    public static UnitService unitService;
    public static PlanetService planetService;

    public static String steamDir;
    public static String outputDir;

    public static void main(String[] args) {
        Options options = new Options();

        Option steamdirOption = new Option("sd", "steamdir", true, "Path to Sins2 folder. Default: \"C:\\Program Files (x86)\\Steam\\steamapps\\common\\Sins2\\\"");
        steamdirOption.setRequired(false);
        options.addOption(steamdirOption);

        Option outputOption = new Option("o", "output", true, "Output directory for wiki json. Default: current directory under .\\wiki\\");
        outputOption.setRequired(false);
        options.addOption(outputOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        steamDir = Optional.ofNullable(cmd.getOptionValue("steamdir")).orElse(DEFAULT_STEAM_DIR);
        outputDir = Optional.ofNullable(cmd.getOptionValue("output")).orElse(DEFAULT_OUTPUT_DIR);

        if (!FileTools.validSteamDir(steamDir)) {
            System.out.println("Invalid steamdir!");
            System.exit(1);
        }

        System.out.println(STR."Starting with steamdir \{steamDir} and outputdir \{outputDir}");

        unitService = new UnitService(new GameFileService(steamDir), steamDir);
        planetService = new PlanetService(new ManifestService(steamDir));

        loadFilesAndExport();
    }

    public static void loadFilesAndExport() {
        // Write units file
        Manifest<UnitType, Unit> units = unitService.loadUnitManifest();

        FileTools.writeUnitsJsonFile(outputDir, units.getIdMap().values());

        // Write structures file
        FileTools.writeStructuresJsonFile(outputDir, units.getIdMap().values());

        // Write planet upgrades file
        Collection<UnitItem> planetsItems = planetService.loadPlanetItems();

        FileTools.writePlanetItemsJsonFile(outputDir, planetsItems);

        System.out.println("Done printing all!");
    }
}