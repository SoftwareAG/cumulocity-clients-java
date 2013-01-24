package com.cumulocity.me.rest.convert.identity;

import static com.cumulocity.me.rest.convert.JSONMatcher.jsonObject;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;

public class ExternalIDCollectionRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    ExternalIDCollectionRepresentationConverter converter = new ExternalIDCollectionRepresentationConverter();
    
    ExternalIDRepresentation externalId = new ExternalIDRepresentation();
    ExternalIDCollectionRepresentation representation = new ExternalIDCollectionRepresentation();
    
    @Before
    public void setUp() {
        converter.setJsonConversionService(conversionService);
    }
    
    @Test
    public void shouldFormatEmptyRepresentation() throws Exception {
        JSONObject json = converter.toJson(new ExternalIDCollectionRepresentation());
        
        assertThat(json.toString()).isEqualTo("{\"externalIds\":[]}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(ExternalIDCollectionRepresentation.class);
    }
    
    @Test
    public void shouldFormatWithExternalId() throws Exception {
        when(conversionService.toJson(externalId)).thenReturn(aJSONObject().withProperty("externalId","some_id").withProperty("type", "some_type").build());
        representation.getExternalIds().add(externalId);
        
        JSONObject json = converter.toJson(representation);
        
        assertThat(json.toString()).isEqualTo("{\"externalIds\":[{\"externalId\":\"some_id\",\"type\":\"some_type\"}]}");
    }
    
    @Test
    public void shouldParseWithExternalId() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"externalId\":\"some_id\",\"type\":\"some_type\"}"), same(ExternalIDRepresentation.class))).thenReturn(externalId);
        JSONObject json = new JSONObject("{\"externalIds\":[{\"externalId\":\"some_id\",\"type\":\"some_type\"}]}");
        
        ExternalIDCollectionRepresentation parsed = (ExternalIDCollectionRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getExternalIds().size()).isEqualTo(1);
        assertThat(parsed.getExternalIds().get(0)).isSameAs(externalId);
    }
    
}
