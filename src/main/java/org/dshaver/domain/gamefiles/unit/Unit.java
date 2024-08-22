package org.dshaver.domain.gamefiles.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

@JsonIgnoreProperties
@Data
public class Unit {
    public static final String ADVENT_ID_PREFIX = "advent";
    public static final String VASARI_ID_PREFIX = "vasari";
    public static final String TEC_ID_PREFIX = "trader";
    public static final String ADVENT = "Advent";
    public static final String VASARI = "Vasari";
    public static final String TEC = "TEC";

    String id;
    String name;
    String description;
    String race;
    String targetFilterUnitType;
    UnitType unitType;

    Weapons weapons;
    Physics physics;
    Health health;
    Build build;
    public double getModifiedSpeed() {
        return physics.getMaxLinearSpeed();
    }

    public void findRace() {
        if (id.contains(ADVENT_ID_PREFIX)) {
            this.race = ADVENT;
        } else if (id.contains(VASARI_ID_PREFIX)) {
            this.race = VASARI;
        } else if (id.contains(TEC_ID_PREFIX)) {
            this.race = TEC;
        }
    }


    private String getCosts() {
        Optional<Double> creditCost = Optional.ofNullable(getBuild().getPrice().getCredits());
        Optional<Double> metalCost = Optional.ofNullable(getBuild().getPrice().getMetal());
        Optional<Double> crystalCost = Optional.ofNullable(getBuild().getPrice().getCrystal());

        List<String> costs = new ArrayList<>();

        creditCost.ifPresent(credit -> costs.add(FMT."%.0f\{credit} credits"));
        metalCost.ifPresent(metal -> costs.add(FMT."%.0f\{metal} metal"));
        crystalCost.ifPresent(crystal -> costs.add(FMT."%.0f\{crystal} crystal"));

        return String.join(", ", costs);
    }


    private String buildWeaponTable() {
        if (getWeapons() == null) {
            return "";
        }

        return getWeapons().getWeapons()
                .stream()
                .map(this::buildWeaponRow)
                .collect(Collectors.joining("|-\n"));
    }

    /**
     * | Medium Pulse Gun x1 || 10.5 || 150.0
     * @return
     */
    private String buildWeaponRow(Weapon weapon) {
        return FMT."""
                | \{weapon.getName()} x\{weapon.getCount()} || %.1f\{weapon.getDps()} || %.0f\{weapon.getPenetration()} || %.0f\{weapon.getRange()}
                """;
    }

    @Override
    public String toString() {
        var supply = getBuild().getSupplyCost();
        var buildTimeSec = getBuild().getBuildTime();
        var durability = getHealth().getDurability();
        Level level = getHealth().getLevels().get(0);
        var maxShields = level.getMaxShieldPoints();
        var maxArmor = level.getMaxArmorPoints();
        var maxHull = level.getMaxHullPoints();
        var armorStrength = level.getArmorStrength();
        var weaponTable = buildWeaponTable();

        return FMT."""
{{stub}}
{{Header Nav|game=Sins of a Solar Empire II}}

{{SoaSE2 unit
|image=
[[File:SoaSE2 \{name}.png|\{name}|250px]]
|supply=\{supply}
|build=%.0f\{buildTimeSec} sec
|durability=%.0f\{durability}
|shield=%.0f\{maxShields}
|armour=%.0f\{maxArmor}
|hull=%.0f\{maxHull}
|armourstr=%.0f\{armorStrength}
|speed=%.0f\{getModifiedSpeed()}
|price=\{getCosts()}
}}

\{description}

{| {{prettytable|notwide=1}}
!Weapon || DPS || Pierce || Range
|-
\{weaponTable}|}

'''Official Description:''' ''\{description}''
{{Footer Nav|game=Sins of a Solar Empire II|prevpage=|nextpage=}}
      """;
    }
}
