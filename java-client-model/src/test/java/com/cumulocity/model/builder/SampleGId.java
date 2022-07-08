package com.cumulocity.model.builder;

import com.cumulocity.model.idtype.GId;

public enum SampleGId {

    GID_1("GIdVal1"),
    GID_2("GIdVal2"),
    GID_3("GIdVal3");

    public static final String GID_VALUE_1 = "GIdVal1";
    public static final String GID_VALUE_2 = "GIdVal2";

    private final String gidValue;

    SampleGId(String gidValue) {
        this.gidValue = gidValue;
    }

    public GIdBuilder builder() {
        return new GIdBuilder().withValue(gidValue);
    }

    public GId build() {
        return builder().build();
    }
}
