package org.dshaver.sins.domain.ingest.unititem;

import lombok.Data;

import static org.dshaver.sins.domain.ingest.unititem.ModifierBehavior.additive;
import static org.dshaver.sins.domain.ingest.unititem.ModifierBehavior.scalar;

@Data
public class EmpireModifier {
    String modifierType;
    ModifierBehavior valueBehavior;
    double value;
    String effect;

    /**
     * Must have set name from localized text before calling this!
     */
    public void setEffect() {
        if (additive.equals(valueBehavior)) {
            setEffect(STR."\{getModifierType()} \{getValueBehavior().getOperation()}\{getValue()}");
        } else if (scalar.equals(valueBehavior)) {
            String percentValue = Double.toString(value * 100);
            if (getValue() > 0) {
                setEffect(STR."\{getModifierType()} +\{percentValue}\{getValueBehavior().getOperation()}");
            } else {
                setEffect(STR."\{getModifierType()} \{percentValue}\{getValueBehavior().getOperation()}");
            }
        }
    }
}
