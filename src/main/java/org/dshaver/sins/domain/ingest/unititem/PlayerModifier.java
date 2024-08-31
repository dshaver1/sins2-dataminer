package org.dshaver.sins.domain.ingest.unititem;

import lombok.Data;

import java.util.List;

@Data
public class PlayerModifier {
    List<EmpireModifier> empireModifiers;
}
