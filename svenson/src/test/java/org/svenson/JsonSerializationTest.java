package org.svenson;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonSerializationTest {

    @Test
    public void shouldConvertBasicMapToJson() {
        //given
        Map<String, Object> map = new HashMap<>();
        map.put("self", "http://integration.cumulocity.com/inventory/managedObjects/10200/assetParents");

        //when
        String serialized = JSON.defaultJSON().forValue(map);

        //then
        assertEquals("{\"self\":\"http://integration.cumulocity.com/inventory/managedObjects/10200/assetParents\"}", serialized);
    }

    @Test
    public void shouldDeserializeObjectWithOverloadedSetterAndOverriddenProperties() {
        //given
        Map<String, Object> map = new HashMap<>();
        map.put("value", "ANY");
        map.put("text", "test");
        String serialized = JSON.defaultJSON().forValue(map);

        //when
        final TestJsonObject result = JSONParser.defaultJSONParser().parse(TestJsonObject.class, serialized);

        //then
        assertEquals(TestJsonObject.Value.ANY, result.getValue());
        assertEquals("test", result.getText());
    }

    @Test
    public void shouldDeserializeToSpecificNumberClass() {
        //given
        JSONParser jsonParser = JSONParser.defaultJSONParser();
        List<ExpectedMapping> jsonMappings = asList(
                new ExpectedMapping("-432432432432", Long.class).expectedValue(-432432432432L),
                new ExpectedMapping("-432432432432.0", BigDecimal.class).expectedValue(new BigDecimal("-432432432432.0")),
                new ExpectedMapping("-4324324324324324324343", BigDecimal.class).expectedValue(new BigDecimal("-4324324324324324324343")),
                new ExpectedMapping("43e27", Double.class),
                new ExpectedMapping("-1.5e-75", Double.class),
                new ExpectedMapping("1.23e2", Double.class),
                new ExpectedMapping("1e0", Double.class),
                new ExpectedMapping("-0", Long.class).expectedValue(0L),
                new ExpectedMapping("-9223372036854775808", Long.class).expectedValue(-9223372036854775808L),
                new ExpectedMapping("-9223372036854775809", BigDecimal.class).expectedValue(new BigDecimal("-9223372036854775809")),
                new ExpectedMapping("9223372036854775807", Long.class).expectedValue(9223372036854775807L),
                new ExpectedMapping("9223372036854775808", BigDecimal.class).expectedValue(new BigDecimal("9223372036854775808")),
                new ExpectedMapping("1.7976931348623157e308", Double.class),
                new ExpectedMapping("1.7976931348623159e308", Double.class).expectedValue(Double.POSITIVE_INFINITY),
                new ExpectedMapping("-1.7976931348623157e308", Double.class),
                new ExpectedMapping("-1.7976931348623159e308", Double.class).expectedValue(Double.NEGATIVE_INFINITY)
        );

        for (ExpectedMapping mapping : jsonMappings) {
            //when
            Object parsed = jsonParser.parse(mapping.json);
            //then
            assertEquals(parsed.getClass(), mapping.expectedClass);
            //only for some scenarios, not good to check exact equality for Doubles
            if (mapping.expectedValue != null) {
                assertEquals(parsed, mapping.expectedValue);
            }
        }
    }

    @Test
    public void shouldDeserializeEmbeddedFloatingPoints() {
        //given
        JSONParser jsonParser = JSONParser.defaultJSONParser();

        //when
        TypeWithBigDecimal parsed1 = jsonParser.parse(TypeWithBigDecimal.class, "{\"value\": 432432432432.0}");
        TypeWithDouble parsed2 = jsonParser.parse(TypeWithDouble.class, "{\"value\": -432432432432.0}");
        TypeWithFloat parsed3 = jsonParser.parse(TypeWithFloat.class, "{\"value\": 432432432432.0}");

        //then
        assertTrue(parsed1.getValue().doubleValue() > 0);
        assertTrue(parsed2.getValue() < 0);
        assertTrue(parsed3.getValue() > 0);
    }

    static class ExpectedMapping {
        String json;
        Class<?> expectedClass;
        Number expectedValue;

        public ExpectedMapping(String json, Class<?> expectedClass) {
            this.json = json;
            this.expectedClass = expectedClass;
        }

        public ExpectedMapping expectedValue(Number value) {
            this.expectedValue = value;
            return this;
        }
    }

    public static class TypeWithBigDecimal {
        private BigDecimal value;

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }

    public static class TypeWithDouble {
        private Double value;

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

    public static class TypeWithFloat {
        public Float value;

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }
    }

}


