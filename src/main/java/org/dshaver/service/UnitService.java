package org.dshaver.service;

import org.dshaver.domain.gamefiles.unit.Unit;
import org.dshaver.domain.gamefiles.unit.Weapon;
import org.dshaver.domain.gamefiles.unit.WeaponFile;
import org.dshaver.domain.gamefiles.unit.Weapons;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class UnitService {
    private final GameFileService gameFileService;

    public UnitService(GameFileService gameFileService) {
        this.gameFileService = gameFileService;
    }

    public List<Unit> loadAllUnits() {
        Set<String> idList = Set.of(
                "advent_scout_corvette",
                "advent_colony_frigate",
                "advent_light_frigate",
                "advent_gunship_corvette",
                "advent_siege_frigate",
                "advent_defense_cruiser",
                "advent_guardian_cruiser",
                "advent_subjugator_cruiser",
                "advent_envoy_frigate",
                "advent_long_range_cruiser",
                "advent_missile_frigate",
                "advent_heavy_cruiser",
                "advent_carrier_cruiser",
                "advent_battle_capital_ship",
                "advent_battle_psionic_capital_ship",
                "advent_carrier_capital_ship",
                "advent_colony_capital_ship",
                "advent_planet_psionic_capital_ship",
                "trader_antiarmor_frigate",
                "trader_antifighter_frigate",
                "trader_carrier_cruiser",
                "trader_colony_frigate",
                "trader_envoy_frigate",
                "trader_heavy_cruiser",
                "trader_light_frigate",
                "trader_long_range_cruiser",
                "trader_missile_battery_frigate",
                "trader_robotics_cruiser",
                "trader_scout_corvette",
                "trader_siege_frigate",
                "trader_skirmisher_corvette_frigate",
                "trader_support_capital_ship",
                "trader_torpedo_cruiser",
                "trader_siege_capital_ship",
                "trader_colony_capital_ship",
                "trader_carrier_capital_ship",
                "trader_battle_capital_ship",
                "trader_loyalist_titan",
                "trader_rebel_titan",
                "vasari_scout_corvette",
                "vasari_light_frigate",
                "vasari_heavy_cruiser",
                "vasari_siege_cruiser",
                "vasari_long_range_frigate",
                "vasari_colony_cruiser",
                "vasari_carrier_cruiser",
                "vasari_anticorvette_corvette",
                "vasari_antiarmor_cruiser",
                "vasari_overseer_cruiser",
                "vasari_fabricator_cruiser",
                "vasari_raider_corvette",
                "vasari_battle_capital_ship",
                "vasari_carrier_capital_ship",
                "vasari_siege_capital_ship",
                "vasari_marauder_capital_ship",
                "vasari_colony_capital_ship");

        return loadUnits(idList);
    }


    public List<Unit> loadUnits(Collection<String> idList) {
        List<Unit> units = idList.stream().map(id -> {
            Unit unit = gameFileService.readUnitFile(id);

            if (unit.getWeapons() != null) {
                unit = collapseWeapons(unit);
            }

            unit.setId(id);
            unit.setName(gameFileService.getLocalizedText(getNameProperty(id)));
            unit.setDescription(gameFileService.getLocalizedText(getDescriptionProperty(id)));
            unit.findRace();

            return unit;
        }).toList();

        return units;
    }


    private Unit collapseWeapons(Unit unit) {
        Set<String> weaponIds = unit.getWeapons().getWeapons().stream().map(Weapon::getWeapon).collect(Collectors.toSet());

        Map<String, WeaponFile> weaponMap = new HashMap<>();

        for (String weaponId : weaponIds) {
            try (InputStream weaponInput = Unit.class.getResourceAsStream("/steamdir/entities/" + weaponId + ".weapon")) {
                weaponMap.put(weaponId, FileTools.getObjectMapper().readValue(weaponInput, WeaponFile.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        unit.getWeapons().getWeapons().forEach(weapon -> {
            WeaponFile weaponFile = weaponMap.get(weapon.getWeapon());
            String weaponName = gameFileService.getLocalizedText(weaponFile.getName());
            weapon.fromWeaponFile(weaponMap.get(weapon.getWeapon()), weaponName);
        });

        List<Weapon> aggregatedWeapons = new ArrayList<>();

        for (int i = 0; i < unit.getWeapons().getWeapons().size(); i++) {
            Weapon weapon = unit.getWeapons().getWeapons().get(i);
            Set<String> finishedIds = aggregatedWeapons.stream().map(Weapon::getWeapon).collect(Collectors.toSet());
            if (finishedIds.contains(weapon.getWeapon())) {
                continue;
            }

            for (int j = i + 1; i < unit.getWeapons().getWeapons().size(); j++) {
                if (j >= unit.getWeapons().getWeapons().size()) {
                    break;
                }

                Weapon testWeapon = unit.getWeapons().getWeapons().get(j);
                if (weapon.getWeapon().equals(testWeapon.getWeapon())) {
                    weapon.add(testWeapon);
                }
            }

            aggregatedWeapons.add(weapon);
        }

        unit.setWeapons(new Weapons(aggregatedWeapons));

        return unit;
    }

    public static String getNameProperty(String unitId) {
        return STR."\{unitId}_name";
    }

    public static String getDescriptionProperty(String unitId) {
        return STR."\{unitId}_description";
    }
}
