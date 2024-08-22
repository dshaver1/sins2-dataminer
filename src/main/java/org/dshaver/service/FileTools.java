package org.dshaver.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import org.checkerframework.common.value.qual.ArrayLenRange;
import org.dshaver.Main;
import org.dshaver.domain.Manifest;
import org.dshaver.domain.export.WikiPlanetUpgrade;
import org.dshaver.domain.export.WikiUnit;
import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unititem.UnitItem;

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

    private static final String UNIT_ITEM_MANIFEST_FILE_PATH = "/steamdir/entities/unit_item.entity_manifest";
    private static final String LOCALIZED_TEXT_FILE_PATH = "/steamdir/localized_text/en.localized_text";
    private static final String UNIT_JSON_OUTPUT_NAME = "SoaSE2_units.json";
    private static final String PLANET_UPGRADE_OUTPUT_NAME = "SoaSE2_planet_upgrades.json";
    private static final ObjectMapper objectMapper;
    private static File wikiTargetDir;
    private static Map<String, String> localizedText;

    static {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String wikiPath = path.substring(1) + "wiki";
        wikiTargetDir = new File(wikiPath);
        wikiTargetDir.mkdirs();

        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        localizedText = FileTools.readLocalizedTextFile();
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

    public static Map<String, String> readLocalizedTextFile() {
        InputStream localizedTextInput = Unit.class.getResourceAsStream(LOCALIZED_TEXT_FILE_PATH);

        MapType typeReference = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);

        try {
            return objectMapper.readValue(localizedTextInput, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Unit readUnitFile(String unitId) {
        System.out.println("Reading unit file for " + unitId);
        try (InputStream is = Unit.class.getResourceAsStream("/steamdir/entities/" + unitId + ".unit")) {
            return objectMapper.readValue(is, Unit.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnitItem readUnitItemFile(String unitItemId) {
        var unitItemPath = STR."/steamdir/entities/\{unitItemId}.unit_item";
        System.out.println(STR."Reading unitItem file \{unitItemId}");

        try (InputStream is = Unit.class.getResourceAsStream(unitItemPath)) {
            UnitItem unitItem = objectMapper.readValue(is, UnitItem.class);
            unitItem.setId(unitItemId);
            unitItem.setName(localizedText.get(unitItem.getName()));
            unitItem.setDescription(localizedText.get(unitItem.getDescription()));
            unitItem.findRace();
            unitItem.findFaction();

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
                .collect(Collectors.toMap(u -> STR."\{u.getRace()} \{u.getName()}", Function.identity()));
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

    public static Manifest loadUnitItemManifest() {
        System.out.println("Reading unit item manifest");
        try (InputStream is = Unit.class.getResourceAsStream(UNIT_ITEM_MANIFEST_FILE_PATH)) {
            return objectMapper.readValue(is, Manifest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
