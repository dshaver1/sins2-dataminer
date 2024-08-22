package org.dshaver.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;
import org.dshaver.Main;
import org.dshaver.domain.Manifest;
import org.dshaver.domain.gamefiles.ManifestFile;
import org.dshaver.domain.export.WikiPlanetUpgrade;
import org.dshaver.domain.export.WikiUnit;
import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unit.UnitType;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class FileTools {

    private static final String ENTITY_DIR = "entities";
    private static final String UNIT_ITEM_MANIFEST_FILE_PATH = "entities/unit_item.entity_manifest";
    private static final String UNIT_MANIFEST_FILE_PATH = "entities/unit.entity_manifest";
    private static final String LOCALIZED_TEXT_FILE_PATH = "localized_text/en.localized_text";
    private static final String UNIT_JSON_OUTPUT_NAME = "SoaSE2_units.json";
    private static final String PLANET_UPGRADE_OUTPUT_NAME = "SoaSE2_planet_upgrades.json";
    private static final ObjectMapper objectMapper;
    private static File wikiTargetDir;

    static {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String wikiPath = path.substring(1) + "wiki";
        wikiTargetDir = new File(wikiPath);
        wikiTargetDir.mkdirs();

        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static File getWikiTargetDir() {
        return wikiTargetDir;
    }

    /**
     * Probably not going to use this anymore. Initial stab at generating the wiki syntax here. Pivoted to just exporting
     * json and have the wiki read the json via lua module.
     */
    @Deprecated
    public static void writeInitialWikiFiles(List<Unit> units) {
        units.forEach(unit -> {
            try {
                Path unitPath = wikiTargetDir.toPath().resolve(unit.getId() + ".txt");
                writeStringToFile(Files.createFile(unitPath).toFile(), unit.toString(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Map<String, String> readLocalizedTextFile(String steamDir) {
        Path path = getPath(steamDir, LOCALIZED_TEXT_FILE_PATH);

        try (InputStream localizedTextInput = Files.newInputStream(path)) {
            MapType typeReference = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);

            return objectMapper.readValue(localizedTextInput, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Unit readUnitFile(String steamDir, String unitId) {
        var unitPath = getEntityPath(steamDir, STR."\{unitId}.unit");
        System.out.println(STR."Reading unit file \{unitPath}");

        try (InputStream is = Files.newInputStream(unitPath)) {
            Unit unit = objectMapper.readValue(is, Unit.class);
            unit.setId(unitId);
            unit.findRace();

            if (StringUtils.isNotBlank(unit.getTargetFilterUnitType())) {
                unit.setUnitType(UnitType.valueOf(unit.getTargetFilterUnitType()));
            }

            return unit;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnitItem readUnitItemFile(String steamDir, String unitItemId) {
        var unitItemPath = getEntityPath(steamDir, STR."\{unitItemId}.unit_item");
        System.out.println(STR."Reading unitItem file \{unitItemPath}");

        try (InputStream is = Files.newInputStream(unitItemPath)) {
            UnitItem unitItem = objectMapper.readValue(is, UnitItem.class);

            unitItem.setId(unitItemId);

            return unitItem;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeUnitsJsonFile(Collection<Unit> units) {
        Map<String, WikiUnit> allUnitMap = getAllWikiUnits(units);
        Path allUnitsJsonPath = wikiTargetDir.toPath().resolve(UNIT_JSON_OUTPUT_NAME);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(allUnitsJsonPath.toFile(), allUnitMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, WikiUnit> getAllWikiUnits(Collection<Unit> units) {
        return units.stream()
                .map(WikiUnit::fromUnit)
                .collect(Collectors.toMap(FileTools::unitKeyMapper, Function.identity()));
    }

    private static String unitKeyMapper(WikiUnit wikiUnit) {
        List<String> keyComponents = new ArrayList<>();
        if (StringUtils.isNotBlank(wikiUnit.getRace())) {
            keyComponents.add(wikiUnit.getRace());
        }

        keyComponents.add(wikiUnit.getName());

        return String.join(" ", keyComponents);
    }

    public static void writePlanetUpgradesJsonFile(Collection<UnitItem> unitItems) {
        Map<String, WikiPlanetUpgrade> allUnitItemMap = getAllWikiPlanetUpgrades(unitItems);
        Path allUnitsJsonPath = wikiTargetDir.toPath().resolve(PLANET_UPGRADE_OUTPUT_NAME);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(allUnitsJsonPath.toFile(), allUnitItemMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, WikiPlanetUpgrade> getAllWikiPlanetUpgrades(Collection<UnitItem> unitItems) {
        return unitItems.stream()
                .map(WikiPlanetUpgrade::fromUnitItem)
                .collect(Collectors.toMap(FileTools::planetUpgradeKeyMapper, Function.identity()));
    }

    private static String planetUpgradeKeyMapper(WikiPlanetUpgrade wikiPlanetUpgrade) {
        List<String> keyComponents = new ArrayList<>();
        if (wikiPlanetUpgrade.getRace() != null) {
            keyComponents.add(wikiPlanetUpgrade.getRace());
        }

        if (wikiPlanetUpgrade.getFaction() != null) {
            keyComponents.add(wikiPlanetUpgrade.getFaction());
        }

        keyComponents.add(wikiPlanetUpgrade.getName());

        return String.join(" ", keyComponents);
    }

    public static Path getEntityPath(String steamDir, String filename) {
        return Path.of(steamDir).resolve(ENTITY_DIR).resolve(filename);
    }

    public static Path getPath(String steamDir, String filePart) {
        return Path.of(steamDir).resolve(filePart);
    }

    public static Manifest<UnitItemType, UnitItem> loadUnitItemManifest(String steamDir) {
        System.out.println("Reading unit item manifest");
        Path path = getPath(steamDir, UNIT_ITEM_MANIFEST_FILE_PATH);

        try (InputStream is = Files.newInputStream(path)) {
            ManifestFile manifestFile = objectMapper.readValue(is, ManifestFile.class);
            Manifest<UnitItemType, UnitItem> manifest = new Manifest<>();
            manifest.setIds(manifestFile.getIds());

            return manifest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Manifest<UnitType, Unit> loadUnitManifest(String steamDir) {
        System.out.println("Reading unit manifest");
        Path path = getPath(steamDir, UNIT_MANIFEST_FILE_PATH);

        try (InputStream is = Files.newInputStream(path)) {
            ManifestFile manifestFile = objectMapper.readValue(is, ManifestFile.class);
            Manifest<UnitType, Unit> manifest = new Manifest<>();
            manifest.setIds(manifestFile.getIds());

            return manifest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
