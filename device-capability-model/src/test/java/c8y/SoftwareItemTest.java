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

    @Test
    public void missingParameters() {
        String toDeserializeWithoutUrl = "{\"name\":\"" + NAME + "\",\"version\":\"" + VERSION + "\"}";
        SoftwareItem deserialized = JSONParser.defaultJSONParser().parse(SoftwareItem.class, toDeserializeWithoutUrl);

        Assert.assertEquals(NAME, deserialized.getName());
        Assert.assertEquals(VERSION, deserialized.getVersion());
        Assert.assertEquals(null, deserialized.getUrl());

        String serialized = JSON.defaultJSON().forValue(deserialized);

        Assert.assertEquals(toDeserializeWithoutUrl, serialized);
    }

    @Test
    public void additionalParameters() {
        String additionalParameterName = "abc";
        String additionalParameterValue = "def";
        String toDeserializeWithParameter = "{\"name\":\"" + NAME + "\",\"url\":\"" + URL + "\",\"version\":\"" + VERSION + "\",\""
                + additionalParameterName + "\":\"" + additionalParameterValue + "\"}";
        SoftwareItem deserialized = JSONParser.defaultJSONParser().parse(SoftwareItem.class, toDeserializeWithParameter);

        Assert.assertEquals(NAME, deserialized.getName());
        Assert.assertEquals(VERSION, deserialized.getVersion());
        Assert.assertEquals(URL, deserialized.getUrl());

        Assert.assertEquals(additionalParameterValue, deserialized.getProperty(additionalParameterName));

        String serialized = JSON.defaultJSON().forValue(deserialized);

        Assert.assertEquals(toDeserializeWithParameter, serialized);
    }
}
