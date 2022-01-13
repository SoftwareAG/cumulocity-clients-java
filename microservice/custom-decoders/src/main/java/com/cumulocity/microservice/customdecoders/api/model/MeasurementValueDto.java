package com.cumulocity.microservice.customdecoders.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class MeasurementValueDto implements Serializable {
    private String seriesName;
    private String unit;
    private BigDecimal value;
}
