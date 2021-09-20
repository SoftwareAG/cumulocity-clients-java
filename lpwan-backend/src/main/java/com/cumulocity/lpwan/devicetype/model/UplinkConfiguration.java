package com.cumulocity.lpwan.devicetype.model;

import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.lpwan.payload.uplink.model.ManagedObjectMapping;
import com.cumulocity.lpwan.payload.uplink.model.MeasurementMapping;

import lombok.Data;

@Data
public class UplinkConfiguration extends UplinkConfigurationMapping {

    private Integer messageTypeId;
    private Integer startBit;
    private Integer noBits;
    private Double multiplier;
    private Double offset;
    private String unit;
    private boolean littleEndian;
    private boolean signed;
    private boolean bcd;
    private Codec codec;

}
