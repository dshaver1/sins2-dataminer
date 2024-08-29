package org.dshaver.domain.gamefiles.research;

import lombok.Data;
import org.dshaver.domain.gamefiles.unit.ExoticPrice;
import org.dshaver.domain.gamefiles.unit.Price;

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
