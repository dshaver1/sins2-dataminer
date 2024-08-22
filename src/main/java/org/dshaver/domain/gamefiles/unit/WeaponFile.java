package org.dshaver.domain.gamefiles.unit;

import lombok.Data;

@Data
public class WeaponFile {
    String name;
    String weaponType;
    double range;
    double cooldown_duration;
    double damage;
    double bombingDamage;
    double penetration;
    Firing firing;

    @Data
    public static class Firing {
        String firing_type;
        double travel_speed;
        double charge_duration;
    }
}
