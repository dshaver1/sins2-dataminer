package org.dshaver.domain.export;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.Unit;

import java.util.List;

import static java.util.FormatProcessor.FMT;
import static org.dshaver.domain.gamefiles.unit.Exotics.*;

@Data
public class WikiUnit implements Priced {
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
    List<String> weapons;

    public WikiUnit(Unit unit) {
        System.out.println(STR."Creating WikiUnit for \{unit.getId()}");
        this.race = unit.getRace();
        this.name = unit.getName();
        this.type = unit.getTargetFilterUnitType();
        this.durability = FMT."%.0f\{unit.getHealth().getDurability()}";
        this.speed = FMT."%.0f\{unit.getModifiedSpeed()}";

        if (unit.getBuild() != null) {
            setPrices(unit.getBuild().getPrice(), unit.getBuild().getExoticPrice());
            this.supply = FMT."\{unit.getBuild().getSupplyCost()}";
            this.buildtime = FMT."%.0f\{unit.getBuild().getBuildTime()}";
        }

        this.shield = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxShieldPoints()}";
        this.armor = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxArmorPoints()}";
        this.hull = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxHullPoints()}";
        this.armorstr = FMT."%.0f\{unit.getHealth().getLevels().get(0).getArmorStrength()}";
        this.description = unit.getDescription();
    }
}
