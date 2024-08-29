package org.dshaver.domain.gamefiles.unititem;

import lombok.Data;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.dshaver.domain.gamefiles.unititem.ModifierBehavior.additive;
import static org.dshaver.domain.gamefiles.unititem.ModifierBehavior.scalar;

@Data
public class PlanetModifier {
    private static final String MINING_TRACK_METAL_INCOME_RATE = "mining_track_metal_income_rate";
    private static final String METAL_EXTRACTION_RATE = "metal_extraction_rate";
    private static final String MINING_TRACK_CRYSTAL_INCOME_RATE = "mining_track_crystal_income_rate";
    private static final String CRYSTAL_EXTRACTION_RATE = "crystal_extraction_rate";

    String name;
    String modifierType;
    ModifierBehavior valueBehavior;
    Collection<Double> values;
    String effect;

    /**
     * Must have set name from localized text before calling this!
     */
    public void setEffect() {
        String value = getValues().stream()
                .map(d -> scalar.equals(getValueBehavior()) ? d * 100 : d)
                .map(d -> {
                    StringBuilder sb = new StringBuilder();

                    if (d > 0) {
                        sb.append("+");
                    }

                    sb.append(d);

                    if (scalar.equals(getValueBehavior())) {
                        sb.append(getValueBehavior().getOperation());
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining(" → "));

        setEffect(STR."\{getName()} \{value}");

    }

    // why? The ids for these 2 don't actually match with the localized text.
    // mining_track_metal_income_rate -> planet_modifier.metal_extraction_rate
    // mining_track_crystal_income_rate -> planet_modifier.crystal_extraction_rate
    public String getModifierType() {
        if (MINING_TRACK_METAL_INCOME_RATE.equals(this.modifierType)) {
            return METAL_EXTRACTION_RATE;
        }

        if (MINING_TRACK_CRYSTAL_INCOME_RATE.equals(this.modifierType)) {
            return CRYSTAL_EXTRACTION_RATE;
        }

        return this.modifierType;
    }
}
