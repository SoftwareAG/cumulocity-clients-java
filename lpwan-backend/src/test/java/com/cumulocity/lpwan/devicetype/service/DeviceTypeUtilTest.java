package com.cumulocity.lpwan.devicetype.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cumulocity.model.idtype.GId;

import c8y.LpwanDevice;

public class DeviceTypeUtilTest {

    private final String pathPrefix = "inventory/managedObjects/";

    @Test
    public void shouldSetIdWithPath() {
        LpwanDevice lpwanDevice = new LpwanDevice();

        LpwanDeviceTypeUtil.setTypePath(lpwanDevice, "11805");

        assertEquals(pathPrefix + "11805", lpwanDevice.getType());
    }

    @Test
    public void shouldReturnIdFromLpwanTypePath() {
        LpwanDevice lpwanDevice = new LpwanDevice();
        LpwanDeviceTypeUtil.setTypePath(lpwanDevice, "11805");
        
        GId gId = LpwanDeviceTypeUtil.getTypeId(lpwanDevice);
        
        assertEquals("11805", gId.getValue());
    }
}
