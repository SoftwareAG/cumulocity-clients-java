package com.cumulocity.me.rest.convert.measurement;

import static com.cumulocity.me.rest.convert.JSONMatcher.jsonObject;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.representation.measurement.MeasurementRepresentationBuilder.aMeasurementRepresentation;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.convert.TestFragment;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentationBuilder;

public class MeasurementRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    MeasurementRepresentationConverter converter = new MeasurementRepresentationConverter();
    
    GId id = new GId("testid");
    String type = "some_type";
    Date time = new Date(978307201000L); // 2001-01-01 00:00:01
    ManagedObjectRepresentation source = new ManagedObjectRepresentation();
    TestFragment fragment = new TestFragment();
    MeasurementRepresentationBuilder representation = aMeasurementRepresentation();
    
    @Before
    public void setUp() {
        converter.setJsonConversionService(conversionService);
    }
    
    @Test
    public void shouldFormatEmptyRepresentation() throws Exception {
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{}");
    }
    
    @Test
    public void shouldFormatWithSimpleProps() throws Exception {
        representation.withID(id).withType(type).withTime(time);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"type\":\"some_type\",\"time\":\"2001-01-01T00:00:01.0Z\",\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldFormatWithComplexProps() throws Exception {
        when(conversionService.toJson(source)).thenReturn(aJSONObject().withProperty("id", "src_id").build());
        representation.withID(id).withSource(source);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"source\":{\"id\":\"src_id\"},\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldFormatWithFragments() throws Exception {
        when(conversionService.toJson(fragment)).thenReturn(aJSONObject().withProperty("type", "test").build());
        representation.withID(id).with(fragment);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"com_cumulocity_me_rest_convert_TestFragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(MeasurementRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"type\":\"some_type\",\"time\":\"2001-01-01T00:00:01.0Z\",\"id\":\"testid\"}");
        
        MeasurementRepresentation parsed = (MeasurementRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getType()).isEqualTo(type);
        assertThat(parsed.getTime()).isEqualTo(time);
    }
    
    @Test
    public void shouldParseWithComplexProps() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"id\":\"src_id\"}"), same(ManagedObjectRepresentation.class))).thenReturn(source);
        JSONObject json = new JSONObject("{\"source\":{\"id\":\"src_id\"},\"id\":\"testid\"}");
        
        MeasurementRepresentation parsed = (MeasurementRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getSource()).isSameAs(source);
    }
    
    @Test
    public void shouldParseWithFragment() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"type\":\"test\"}"), same(TestFragment.class))).thenReturn(fragment);
        JSONObject json = new JSONObject("{\"com_cumulocity_me_rest_convert_TestFragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
        
        MeasurementRepresentation parsed = (MeasurementRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getAttrs().size()).isEqualTo(1);
        assertThat(parsed.get(TestFragment.class)).isSameAs(fragment);
    }
}
