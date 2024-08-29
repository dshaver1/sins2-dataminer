package org.dshaver.domain.gamefiles.unititem;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.ExoticPrice;
import org.dshaver.domain.gamefiles.unit.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.dshaver.domain.gamefiles.unit.Unit.*;

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
        if (id.contains(ADVENT_ID_PREFIX)) {
            this.race = ADVENT;
        } else if (id.contains(VASARI_ID_PREFIX)) {
            this.race = VASARI;
        } else if (id.contains(TEC_ID_PREFIX)) {
            this.race = TEC;
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
