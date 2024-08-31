package org.dshaver.sins.domain.ingest.player;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    String id;
    String race;
    List<String> buildableUnits;
    List<String> factionBuildableUnits;
    List<String> buildableStrikecraft;
    List<String> structures;
    List<String> shipComponents;
    List<String> planetComponents;
    List<String> factionPlanetComponents;
}
