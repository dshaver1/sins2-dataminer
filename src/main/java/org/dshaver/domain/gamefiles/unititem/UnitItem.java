package org.dshaver.domain.gamefiles.unititem;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.ExoticPrice;
import org.dshaver.domain.gamefiles.unit.Price;

import java.util.Arrays;
import java.util.List;

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
}
