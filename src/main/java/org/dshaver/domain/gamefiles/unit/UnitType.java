package org.dshaver.domain.gamefiles.unit;

public enum UnitType {
    strikecraft(true),
    corvette(true),
    frigate(true),
    cruiser(true),
    capital_ship(true),
    titan(true),
    structure(false),
    starbase(false),
    torpedo(false),
    cannon_shell(false),
    planet(false),
    asteroid(false),
    star(false),
    loot(false),
    phase_lane(false),
    gravity_well(false),
    buff_agent(false),
    debris(false);

    private final boolean ship;

    UnitType(boolean ship) {
        this.ship = ship;
    }

    public boolean isShip() {
        return ship;
    }
}
