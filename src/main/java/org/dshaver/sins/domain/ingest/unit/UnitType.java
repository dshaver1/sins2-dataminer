package org.dshaver.sins.domain.ingest.unit;

public enum UnitType {
    strikecraft(true, false),
    corvette(true, false),
    frigate(true, false),
    cruiser(true, false),
    capital_ship(true, false),
    titan(true, false),
    structure(false, true),
    starbase(false, true),
    torpedo(false, false),
    cannon_shell(false, false),
    planet(false, false),
    asteroid(false, false),
    star(false, false),
    loot(false, false),
    phase_lane(false, false),
    gravity_well(false, false),
    buff_agent(false, false),
    debris(false, false),
    untargetable(false, false);

    private final boolean ship;
    private final boolean building;

    UnitType(boolean ship, boolean building) {
        this.ship = ship;
        this.building = building;
    }

    public boolean isShip() {
        return ship;
    }

    public boolean isBuilding() {
        return building;
    }
}
