package org.dshaver.domain.gamefiles.structure;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.Unit;

@Data
public class Structure {
    private StructureSlotType slotType;
    private int slotsRequired;
}
