package com.cumulocity.model.inventory;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

import c8y.SoftwareList;
import c8y.SoftwareListEntry;

public class SoftwareListTest {

    private SoftwareList list;
    
    @Before
    public void init() {
        list = new SoftwareList();
        list.add(new SoftwareListEntry("1", "2", "3"));
        list.add(new SoftwareListEntry("4", "5", "6"));
    }
    
    @Test
    public void deserialize() throws Exception {
        String jsonString = JSON.defaultJSON().forValue(list);
        SoftwareList newList = JSONParser.defaultJSONParser().parse(SoftwareList.class, jsonString);
        assertThat(newList.get(0).getName()).isEqualTo("1");
        assertThat(newList.get(0).getVersion()).isEqualTo("2");
        assertThat(newList.get(0).getUrl()).isEqualTo("3");
        assertThat(newList.get(1).getName()).isEqualTo("4");
        assertThat(newList.get(1).getVersion()).isEqualTo("5");
        assertThat(newList.get(1).getUrl()).isEqualTo("6");
    }
}
