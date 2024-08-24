package org.dshaver.domain.export;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
public class WikiPlanetUpgrade implements Priced {
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

    public WikiPlanetUpgrade(UnitItem unitItem) {
        System.out.println(STR."Formatting planet upgrade \{unitItem.getId()} for wiki");
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
    }
}
