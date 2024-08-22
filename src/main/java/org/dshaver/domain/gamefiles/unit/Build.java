package org.dshaver.domain.gamefiles.unit;

import lombok.Data;

import java.util.List;

@Data
public class Build {

    int supplyCost;
    double buildTime;
    Price price;
    List<ExoticPrice> exoticPrice;
}
