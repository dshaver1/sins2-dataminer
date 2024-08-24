package org.dshaver.domain.gamefiles.unit;

import lombok.Data;

@Data
public class WeaponFile {
    String name;
    String weaponType;
    double range;
    double cooldownDuration;
    double damage;
    double bombingDamage;
    double penetration;
    Firing firing;

    @Data
    public static class Firing {
        String firingType;
        double travelSpeed;
        double chargeDuration;
    }
}
