package org.dshaver.domain.gamefiles.unititem;

public enum Faction {
    trader_rebel("TEC", "Primacy"),
    trader_loyalist("TEC", "Enclave"),
    advent_rebel("Advent", "Reborn"),
    advent_loyalist("Advent", "Wrath"),
    vasari_alliance("Vasari", "Alliance"),
    vasari_exodus("Vasari", "Exodus"),
    pranast_("Pranast", null),
    jiskun_("Jiskun", null),
    aluxian_("Aluxian", null);

    private final String raceName;
    private final String factionName;

    Faction(String raceName, String factionName) {
        this.raceName = raceName;
        this.factionName = factionName;
    }

    public String getRaceName() {
        return raceName;
    }

    public String getFactionName() {
        return factionName;
    }
}
