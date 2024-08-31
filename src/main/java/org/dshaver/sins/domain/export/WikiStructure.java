package org.dshaver.sins.domain.export;

import lombok.Data;
import org.dshaver.sins.domain.ingest.unit.Unit;

import java.util.List;

import static java.util.FormatProcessor.FMT;

@Data
public class WikiStructure implements Priced {
    String race;
    String faction;
    String name;
    String credits;
    String metal;
    String crystal;
    String andvar = "";
    String tauranite = "";
    String indurium = "";
    String kalanide = "";
    String quarnium = "";
    String buildtime;
    String durability;
    String shield;
    String armor;
    String hull;
    String armorstr;
    String description;
    List<WikiWeapon> weapons;
    String civilianslots;
    String militaryslots;

    public WikiStructure(Unit unit) {
        System.out.println(STR."Creating WikiStructure for \{unit.getId()}");
        this.race = unit.getRace();
        if (unit.getFaction() != null) {
            this.faction = unit.getFaction().getFactionName();
        }
        this.name = unit.getName();
        this.durability = FMT."%.0f\{unit.getHealth().getDurability()}";

        if (unit.getBuild() != null) {
            setPrices(unit.getBuild().getPrice(), unit.getBuild().getExoticPrice());
            this.buildtime = FMT."%.0f\{unit.getBuild().getBuildTime()}";
        }

        this.shield = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxShieldPoints()}";
        this.armor = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxArmorPoints()}";
        this.hull = FMT."%.0f\{unit.getHealth().getLevels().get(0).getMaxHullPoints()}";
        this.armorstr = FMT."%.0f\{unit.getHealth().getLevels().get(0).getArmorStrength()}";
        this.description = unit.getDescription();

        if (unit.getWeapons() != null) {
            this.weapons = unit.getWeapons().getWeapons().stream()
                    .map(WikiWeapon::new)
                    .toList();
        }

        if (unit.getStructure() != null && unit.getStructure().getSlotType() != null) {
            switch (unit.getStructure().getSlotType()) {
                case military -> this.militaryslots = Integer.toString(unit.getStructure().getSlotsRequired());
                case civilian -> this.civilianslots = Integer.toString(unit.getStructure().getSlotsRequired());
            }
        }
    }
}
