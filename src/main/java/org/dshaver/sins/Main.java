package org.dshaver.sins;

import org.apache.commons.cli.*;
import org.dshaver.sins.domain.Manifest;
import org.dshaver.sins.domain.ingest.player.Player;
import org.dshaver.sins.domain.ingest.player.PlayerType;
import org.dshaver.sins.domain.ingest.unit.Unit;
import org.dshaver.sins.domain.ingest.unit.UnitType;
import org.dshaver.sins.domain.ingest.unititem.UnitItem;
import org.dshaver.sins.service.*;

import java.util.Collection;
import java.util.Optional;

public class Main {
    /**
     * Defaults
     */
    private static final String DEFAULT_STEAM_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Sins2\\";
    private static final String DEFAULT_OUTPUT_DIR = "wiki\\";

    /**
     * Input options
     */
    public static String steamDir;
    public static String outputDir;

    /**
     * Services
     */
    public static GameFileService gameFileService;
    public static ManifestService manifestService;
    public static UnitService unitService;
    public static PlanetService planetService;
    public static PlayerService playerService;

    public static void main(String[] args) {
        CommandLine cmd = readArgs(args);

        steamDir = Optional.ofNullable(cmd.getOptionValue("steamdir")).orElse(DEFAULT_STEAM_DIR);
        outputDir = Optional.ofNullable(cmd.getOptionValue("output")).orElse(DEFAULT_OUTPUT_DIR);

        if (!FileTools.validSteamDir(steamDir)) {
            System.out.println("Invalid steamdir!");
            System.exit(1);
        }

        System.out.println(STR."Starting with steamdir \{steamDir} and outputdir \{outputDir}");

        buildServices();

        loadFilesAndExport();
    }

    private static void buildServices() {
        gameFileService = new GameFileService(steamDir, outputDir);
        manifestService = new ManifestService(gameFileService);
        unitService = new UnitService(gameFileService, steamDir);
        planetService = new PlanetService(manifestService);
        playerService = new PlayerService(gameFileService, manifestService);
    }

    private static CommandLine readArgs(String[] args) {
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

        return cmd;
    }

    public static void loadFilesAndExport() {
        // Read player files as a starting point
        Manifest<PlayerType, Player> playerManifest = playerService.getPlayerManifest();

        // Write units file
        Manifest<UnitType, Unit> units = unitService.loadUnitManifest();

        FileTools.writeUnitsJsonFile(outputDir, units.getIdMap().values());

        // Write structures file
        FileTools.writeStructuresJsonFile(outputDir, units.getIdMap().values());

        // Write planet upgrades file
        Collection<UnitItem> planetsItems = planetService.loadPlanetItems();

        FileTools.writePlanetItemsJsonFile(outputDir, planetsItems);

        System.out.println(STR."Done writing all files to \{FileTools.getTargetDir(outputDir).toAbsolutePath()}!");
    }
}