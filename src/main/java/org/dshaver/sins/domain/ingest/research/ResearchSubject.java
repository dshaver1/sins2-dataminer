package org.dshaver.sins.domain.ingest.research;

import lombok.Data;
import org.dshaver.sins.domain.ingest.unit.ExoticPrice;
import org.dshaver.sins.domain.ingest.unit.Price;

import java.util.List;

@Data
public class ResearchSubject {
    String id;
    String name;
    String description;
    String domain;
    int tier;
    String field;
    Double researchTime;
    Price price;
    List<ExoticPrice> exoticPrice;
}
