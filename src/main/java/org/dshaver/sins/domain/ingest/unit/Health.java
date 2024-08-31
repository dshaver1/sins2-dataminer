package org.dshaver.sins.domain.ingest.unit;

import lombok.Data;

import java.util.List;

@Data
public class Health {
    double durability;
    List<Level> levels;
}
