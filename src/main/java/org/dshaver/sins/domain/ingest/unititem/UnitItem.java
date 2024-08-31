package org.dshaver.sins.domain.ingest.unititem;

import lombok.Data;
import org.dshaver.sins.domain.ingest.unit.ExoticPrice;
import org.dshaver.sins.domain.ingest.unit.Price;
import org.dshaver.sins.domain.ingest.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UnitItem {
    String id;
    String name;
    String description;
    String race;
    Faction faction;
    UnitItemType itemType;
    Double buildTime;
    Price price;
    List<ExoticPrice> exoticPrice;
    List<PlanetTypeGroup> planetTypeGroups;
    PlayerModifier playerModifiers;
    List<PlanetModifier> planetModifiers;
    String ability;
    List<String> prerequisites;
    int prerequisiteTier;
    String prerequisiteDomain;

    public void findRace() {
        if (id.contains(Unit.ADVENT_ID_PREFIX)) {
            this.race = Unit.ADVENT;
        } else if (id.contains(Unit.VASARI_ID_PREFIX)) {
            this.race = Unit.VASARI;
        } else if (id.contains(Unit.TEC_ID_PREFIX)) {
            this.race = Unit.TEC;
        }
    }

    public void findFaction() {
        Arrays.stream(Faction.values())
                .filter(f -> id.contains(f.name()))
                .findFirst()
                .ifPresent(this::setFaction);
    }

    public List<String> getPrerequisitesIds() {
        if (getPlanetTypeGroups() != null && getPlanetTypeGroups().size() > 1) {
            System.out.println("Unit item " + getName() + " has more than 1 planet_type_group!");
        }

        if (getPlanetTypeGroups() != null && getPlanetTypeGroups().get(0) != null && getPlanetTypeGroups().get(0).getBuildPrerequisites() != null) {
            return getPlanetTypeGroups().get(0).getBuildPrerequisites().stream().flatMap(Collection::stream).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
