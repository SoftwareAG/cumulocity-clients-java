package c8y;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class SoftwareItemTest {

    private static final String NAME = "some_name";

    private static final String VERSION = "1.2.3";

    private static final String URL = "http://some.url.com";

    private String toDeserialize;

    private SoftwareItem toSerialize;

    @Before
    public void setUp() throws Exception {
        toSerialize = new SoftwareItem();
        toSerialize.setName(NAME);
        toSerialize.setVersion(VERSION);
        toSerialize.setUrl(URL);

        toDeserialize = "{\"name\":\"" + NAME + "\",\"url\":\"" + URL + "\",\"version\":\"" + VERSION + "\"}";
    }

    @Test
    public void serialize() {
        String serialized = JSON.defaultJSON().forValue(toSerialize);
        Assert.assertTrue(serialized.contains("\"name\":\"" + NAME + "\""));
        Assert.assertTrue(serialized.contains("\"url\":\"" + URL + "\""));
        Assert.assertTrue(serialized.contains("\"version\":\"" + VERSION + "\""));
    }

    @Test
    public void deserialize() {
        SoftwareItem deserialized = JSONParser.defaultJSONParser().parse(SoftwareItem.class, toDeserialize);
        Assert.assertEquals(NAME, deserialized.getName());
        Assert.assertEquals(VERSION, deserialized.getVersion());
        Assert.assertEquals(URL, deserialized.getUrl());
    }
}
