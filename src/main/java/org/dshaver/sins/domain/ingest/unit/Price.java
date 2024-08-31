package org.dshaver.sins.domain.ingest.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class Price {
    Double credits;
    Double metal;
    Double crystal;
}
