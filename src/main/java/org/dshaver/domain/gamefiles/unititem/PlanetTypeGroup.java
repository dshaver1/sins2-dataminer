package org.dshaver.domain.gamefiles.unititem;

import lombok.Data;

import java.util.List;

@Data
public class PlanetTypeGroup {
    List<String> planetTypes;
    List<List<String>> buildPrerequisites;
}
