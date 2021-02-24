package com.cumulocity.lpwan.payload.uplink.model;

import org.joda.time.DateTime;

public abstract class UplinkMessage {

    public abstract String getPayloadHex();
    public abstract String getExternalId();
    public abstract DateTime getDateTime();
    
}
