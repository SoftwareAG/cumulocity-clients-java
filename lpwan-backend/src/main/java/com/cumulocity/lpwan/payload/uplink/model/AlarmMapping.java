package com.cumulocity.lpwan.payload.uplink.model;

import lombok.Data;

@Data
public class AlarmMapping {

    private String type;
    private String text;
    private String severity;

}
