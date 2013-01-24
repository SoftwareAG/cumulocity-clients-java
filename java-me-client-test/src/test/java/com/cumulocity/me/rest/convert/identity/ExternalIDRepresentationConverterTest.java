package com.cumulocity.me.rest.convert.identity;

import static com.cumulocity.me.rest.convert.JSONMatcher.jsonObject;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.representation.identity.ExternalIDRepresentationBuilder.aExternalIDRepresentation;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class ExternalIDRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    ExternalIDRepresentationConverter converter = new ExternalIDRepresentationConverter();
    
    String externalId = "some_id";
    String type = "some_type";
    ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
    ExternalIDRepresentationBuilder representation = aExternalIDRepresentation();
    
    @Before
    public void setUp() {
        converter.setJsonConversionService(conversionService);
    }
    
    @Test
    public void shouldFormatEmptyExternalIDRepresentation() throws Exception {
        JSONObject json = converter.toJson(new ExternalIDRepresentation());
        
        assertThat(json.toString()).isEqualTo("{}");
    }
    
    @Test
    public void shouldFormatExternalIDRepresentationWithSimpleProps() throws Exception {
        representation.withExternalId(externalId).withType(type);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"externalId\":\"some_id\",\"type\":\"some_type\"}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(ExternalIDRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"externalId\":\"some_id\",\"type\":\"some_type\"}");
        
        ExternalIDRepresentation parsed = (ExternalIDRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getExternalId()).isEqualTo(externalId);
        assertThat(parsed.getType()).isEqualTo(type);
    }
    
    @Test
    public void shouldFormatWithComplexProps() throws Exception {
        when(conversionService.toJson(managedObject)).thenReturn(aJSONObject().withProperty("id", "src_id").build());
        representation.withExternalId(externalId).withType(type).withManagedObject(managedObject);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"externalId\":\"some_id\",\"managedObject\":{\"id\":\"src_id\"},\"type\":\"some_type\"}");
    }
    
    @Test
    public void shouldParseWithComplexProps() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"id\":\"src_id\"}"), same(ManagedObjectRepresentation.class))).thenReturn(managedObject);
        JSONObject json = new JSONObject("{\"externalId\":\"some_id\",\"managedObject\":{\"id\":\"src_id\"},\"type\":\"some_type\"}");
        
        ExternalIDRepresentation parsed = (ExternalIDRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getExternalId()).isEqualTo(externalId);
        assertThat(parsed.getType()).isEqualTo(type);
        assertThat(parsed.getManagedObject()).isSameAs(managedObject);
    }
    
}
