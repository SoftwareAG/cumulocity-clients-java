package c8y;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class SoftwareListTest {

    private SoftwareList toSerialize;

    private String toDeserialize;

    @Before
    public void setUp() throws Exception {
        toSerialize = new SoftwareList();
        List<SoftwareItem> items = new ArrayList<SoftwareItem>();
        for (int i = 0; i < 3; i++) {
            SoftwareItem item = new SoftwareItem();
            item.setName("name" + i);
            item.setUrl("url" + i);
            item.setVersion(i + "." + i + "." + i);
            items.add(item);
        }
        toSerialize.setList(items);

        toDeserialize = "{\"list\":[{\"name\":\"name0\",\"url\":\"url0\",\"version\":\"0.0.0\"},{\"name\":\"name1\",\"url\":\"url1\",\"version\":\"1.1.1\"},{\"name\":\"name2\",\"url\":\"url2\",\"version\":\"2.2.2\"}]}";
    }

    @Test
    public void serialize() {
        String serialized = JSON.defaultJSON().forValue(toSerialize);
        Assert.assertEquals(
                "{\"list\":[{\"name\":\"name0\",\"url\":\"url0\",\"version\":\"0.0.0\"},{\"name\":\"name1\",\"url\":\"url1\",\"version\":\"1.1.1\"},{\"name\":\"name2\",\"url\":\"url2\",\"version\":\"2.2.2\"}]}",
                serialized);
    }

    @Test
    public void deserialize() {
        JSONParser parser = new JSONParser();
        SoftwareList deserialized = parser.parse(SoftwareList.class, toDeserialize);
        Assert.assertEquals(3, deserialized.getList().size());

        for (SoftwareItem currentItem : deserialized.getList()) {
            Assert.assertTrue(currentItem instanceof SoftwareItem);
        }
    }

    @Test
    public void inClass() {
        SoftwareListContainer container = JSONParser.defaultJSONParser().parse(SoftwareListContainer.class,
                "{\"c8y_SoftwareList\":" + toDeserialize + "}");
        Assert.assertEquals(3, container.getC8y_SoftwareList().getList().size());
        for (SoftwareItem currentItem : container.getC8y_SoftwareList().getList()) {
            Assert.assertTrue(currentItem instanceof SoftwareItem);
        }

        String serialized = JSON.defaultJSON().forValue(container);

        Assert.assertEquals("{\"c8y_SoftwareList\":" + toDeserialize + "}", serialized);
    }
}
