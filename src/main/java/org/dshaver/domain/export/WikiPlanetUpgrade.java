package org.dshaver.domain.export;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.ExoticPrice;
import org.dshaver.domain.gamefiles.unit.Price;
import org.dshaver.domain.gamefiles.unititem.PlanetTypeGroup;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;
import static org.dshaver.domain.gamefiles.unit.Exotics.*;

@Data
public class WikiPlanetUpgrade {
    String id;
    String name;
    String description;
    String race;
    String faction;
    String buildtime;
    String credits = "";
    String metal = "";
    String crystal = "";
    String andvar = "";
    String tauranite = "";
    String indurium = "";
    String kalanide = "";
    String quarnium = "";
    List<String> planettypes;

    public static WikiPlanetUpgrade fromUnitItem(UnitItem unitItem) {
        System.out.println(STR."Formatting planet upgrade \{unitItem.getId()} for wiki");
        WikiPlanetUpgrade planetUpgrade = new WikiPlanetUpgrade();
        planetUpgrade.id = unitItem.getId();
        planetUpgrade.name = unitItem.getName();
        planetUpgrade.description = unitItem.getDescription();
        planetUpgrade.race = unitItem.getRace();
        Optional.ofNullable(unitItem.getFaction()).ifPresent(faction -> planetUpgrade.setFaction(faction.getFactionName()));
        planetUpgrade.buildtime = FMT."%.0f\{unitItem.getBuildTime()}";

        if (unitItem.getPrice() != null) {
            planetUpgrade.credits = FMT."%.0f\{unitItem.getPrice().getCredits()}";
            planetUpgrade.metal = FMT."%.0f\{unitItem.getPrice().getMetal()}";
            planetUpgrade.crystal = FMT."%.0f\{unitItem.getPrice().getCrystal()}";
        }
        planetUpgrade.planettypes = unitItem.getPlanetTypeGroups().stream()
                .flatMap(group -> group.getPlanetTypes().stream())
                .collect(Collectors.toList());

        if (unitItem.getExoticPrice() != null) {
            unitItem.getExoticPrice().stream()
                    .filter(exoticPrice -> economic.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> planetUpgrade.andvar = String.valueOf(exotic.getCount()));

            unitItem.getExoticPrice().stream()
                    .filter(exoticPrice -> offense.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> planetUpgrade.tauranite = String.valueOf(exotic.getCount()));

            unitItem.getExoticPrice().stream()
                    .filter(exoticPrice -> defense.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> planetUpgrade.indurium = String.valueOf(exotic.getCount()));

            unitItem.getExoticPrice().stream()
                    .filter(exoticPrice -> utility.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> planetUpgrade.kalanide = String.valueOf(exotic.getCount()));

            unitItem.getExoticPrice().stream()
                    .filter(exoticPrice -> ultimate.name().equals(exoticPrice.getExoticType()))
                    .findAny()
                    .ifPresent(exotic -> planetUpgrade.quarnium = String.valueOf(exotic.getCount()));
        }

        return planetUpgrade;
    }
}
