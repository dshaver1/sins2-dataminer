package org.dshaver.domain.gamefiles.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties
@Data
public class Weapon {
    /**
     * The id such as vasari_heavy_cruiser_medium_wave_cannon. Can be used to lookup the stats in another file.
     */
    String weapon;
    String name;
    String weaponType;
    double damage;
    double bombingDamage;
    double penetration;
    double cooldownDuration;
    double travelSpeed;
    double range;
    int count = 1;

    public void fromWeaponFile(WeaponFile weaponFile, String name) {
        this.name = name;
        this.weaponType = weaponFile.getWeaponType();
        this.damage = weaponFile.getDamage();
        this.bombingDamage = weaponFile.getBombingDamage();
        this.penetration = weaponFile.getPenetration();
        this.cooldownDuration = weaponFile.getCooldownDuration();
        if (weaponFile.getFiring() != null) {
            this.travelSpeed = weaponFile.getFiring().getTravelSpeed();
        }
        this.range = weaponFile.getRange();
    }

    public void add(Weapon identicalWeapon) {
        if (getName().equals(identicalWeapon.getName())) {
            this.count += identicalWeapon.getCount();
        } else {
            System.out.println("Tried to add different weapons together!");
        }
    }

    public double getDps() {
        var selectedDamage = "planet_bombing".equals(getWeaponType()) ? bombingDamage : damage;

        return (60 / cooldownDuration) * selectedDamage / 60;
    }
}
