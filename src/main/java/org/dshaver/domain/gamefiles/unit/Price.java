package org.dshaver.domain.gamefiles.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties
@Data
public class Price {
    Double credits;
    Double metal;
    Double crystal;
}
