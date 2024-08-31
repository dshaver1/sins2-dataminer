package org.dshaver.sins.domain.ingest.structure;

import lombok.Data;

@Data
public class Structure {
    private StructureSlotType slotType;
    private int slotsRequired;
}
