package com.cumulocity.lpwan.payload.uplink.model;

import lombok.Data;

@Data
public class MessageIdMapping {

    private Integer startBit;
    private Integer noBits;
}
