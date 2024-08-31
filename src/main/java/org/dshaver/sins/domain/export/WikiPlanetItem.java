package org.dshaver.sins.domain.export;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.dshaver.sins.RomanNumeral;
import org.dshaver.sins.domain.ingest.unititem.EmpireModifier;
import org.dshaver.sins.domain.ingest.unititem.PlanetModifier;
import org.dshaver.sins.domain.ingest.unititem.UnitItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

@Data
public class WikiPlanetItem implements Priced {
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
    List<String> empireeffects;
    List<String> planeteffects;
    String ability;
    String prerequisites;
    String tier;

    public WikiPlanetItem(UnitItem unitItem) {
        System.out.println(STR."Formatting planet item \{unitItem.getId()} for wiki");
        this.id = unitItem.getId();
        this.name = unitItem.getName();
        this.description = unitItem.getDescription();
        this.race = unitItem.getRace();
        Optional.ofNullable(unitItem.getFaction()).ifPresent(faction -> this.setFaction(faction.getFactionName()));
        this.buildtime = FMT."%.0f\{unitItem.getBuildTime()}";
        this.planettypes = unitItem.getPlanetTypeGroups().stream()
                .flatMap(group -> group.getPlanetTypes().stream().map(StringUtils::capitalize))
                .collect(Collectors.toList());

        setPrices(unitItem.getPrice(), unitItem.getExoticPrice());

        if (unitItem.getPlayerModifiers() != null && unitItem.getPlayerModifiers().getEmpireModifiers() != null) {
            this.empireeffects = unitItem.getPlayerModifiers().getEmpireModifiers()
                    .stream()
                    .map(EmpireModifier::getEffect)
                    .toList();
        }

        if (unitItem.getPlanetModifiers() != null) {
            this.planeteffects = unitItem.getPlanetModifiers().stream()
                    .map(PlanetModifier::getEffect)
                    .toList();
        }

        this.ability = unitItem.getAbility();

        if (unitItem.getPrerequisites() != null && !unitItem.getPrerequisites().isEmpty()) {
            this.prerequisites = String.join(",", unitItem.getPrerequisites());
            String romanTier = RomanNumeral.toRoman(unitItem.getPrerequisiteTier() + 1);
            this.tier = STR."\{StringUtils.capitalize(unitItem.getPrerequisiteDomain())} \{romanTier}";
        }
    }
}
