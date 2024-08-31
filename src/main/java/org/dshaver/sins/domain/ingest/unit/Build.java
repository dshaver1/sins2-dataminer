package org.dshaver.sins.domain.ingest.unit;

import lombok.Data;

import java.util.List;

@Data
public class Build {

    int supplyCost;
    double buildTime;
    Price price;
    List<ExoticPrice> exoticPrice;
}
