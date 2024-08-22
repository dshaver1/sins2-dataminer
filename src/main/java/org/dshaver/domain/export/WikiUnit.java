package org.dshaver.domain.export;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.Unit;

import static java.util.FormatProcessor.FMT;
import static org.dshaver.domain.gamefiles.unit.Exotics.*;

@Data
public class WikiUnit {
    String race;
    String name;
    String type;
    String credits;
    String metal;
    String crystal;
    String andvar = "";
    String tauranite = "";
    String indurium = "";
    String kalanide = "";
    String quarnium = "";
    String buildtime;
    String supply;
    String speed;
    String durability;
    String shield;
    String armor;
    String hull;
    String armorstr;
    String description;

    public static WikiUnit fromUnit(Unit unit) {
        System.out.println(STR."Creating WikiUnit for \{unit.getId()}");
        WikiUnit newUnit = new WikiUnit();
        newUnit.race = unit.getRace();
        newUnit.name = unit.getName();
        newUnit.type = unit.getTargetFilterUnitType();
        newUnit.durability = FMT."%.0f\{unit.getHealth().getDurability()}";
        newUnit.speed = FMT."%.0f\{unit.getModifiedSpeed()}";

        if (unit.getBuild() != null) {
            newUnit.credits = FMT."%.0f\{unit.getBuild().getPrice().getCredits()}";
            newUnit.metal = FMT."%.0f\{unit.getBuild().getPrice().getMetal()}";
            newUnit.crystal = FMT."%.0f\{unit.getBuild().getPrice().getCrystal()}";
            newUnit.supply = FMT."\{unit.getBuild().getSupplyCost()}";
            newUnit.buildtime = FMT."%.0f\{unit.getBuild().getBuildTime()}";

            if (unit.getBuild().getExoticPrice() != null) {
                unit.getBuild().getExoticPrice().stream()
                        .filter(exoticPrice -> economic.name().equals(exoticPrice.getExoticType()))
                        .findAny()
                        .ifPresent(exotic -> newUnit.andvar = String.valueOf(exotic.getCount()));

                unit.getBuild().getExoticPrice().stream()
                        .filter(exoticPrice -> offense.name().equals(exoticPrice.getExoticType()))
                        .findAny()
                        .ifPresent(exotic -> newUnit.tauranite = String.valueOf(exotic.getCount()));

                unit.getBuild().getExoticPrice().stream()
                        .filter(exoticPrice -> defense.name().equals(exoticPrice.getExoticType()))
                        .findAny()
                        .ifPresent(exotic -> newUnit.indurium = String.valueOf(exotic.getCount()));

                unit.getBuild().getExoticPrice().stream()
                        .filter(exoticPrice -> utility.name().equals(exoticPrice.getExoticType()))
                        .findAny()
                        .ifPresent(exotic -> newUnit.kalanide = String.valueOf(exotic.getCount()));

                unit.getBuild().getExoticPrice().stream()
                        .filter(exoticPrice -> ultimate.name().equals(exoticPrice.getExoticType()))
                        .findAny()
                        .ifPresent(exotic -> newUnit.quarnium = String.valueOf(exotic.getCount()));
            }
        }

        newUnit.shield = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxShieldPoints()}";
        newUnit.armor = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxArmorPoints()}";
        newUnit.hull = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxHullPoints()}";
        newUnit.armorstr = FMT."%.0f\{unit.getHealth().getLevels().get(0).getArmorStrength()}";
        newUnit.description = unit.getDescription();


        return newUnit;
    }
}
