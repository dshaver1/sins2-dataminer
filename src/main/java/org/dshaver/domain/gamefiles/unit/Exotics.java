package org.dshaver.domain.gamefiles.unit;

public enum Exotics {
    economic("Andvar"),
    offense("Tauranite"),
    defense("Indurium"),
    utility("Kalanide"),
    ultimate("Quarnium");

    private final String alias;

    Exotics(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
