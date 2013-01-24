package com.cumulocity.me.rest.convert.identity;

import static com.cumulocity.me.rest.representation.identity.IdentityRepresentationBuilder.aIdentityRepresentation;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentationBuilder;

public class IdentityRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    IdentityRepresentationConverter converter = new IdentityRepresentationConverter();
    
    String externalId = "some_id";
    String externalIdsOfGlobalId = "externalIdsOfGlobalId";
    IdentityRepresentation source = new IdentityRepresentation();
    IdentityRepresentationBuilder representation = aIdentityRepresentation();
    
    @Before
    public void setUp() {
        converter.setJsonConversionService(conversionService);
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(IdentityRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"externalId\":\"some_id\",\"externalIdsOfGlobalId\":\"externalIdsOfGlobalId\"}");
        
        IdentityRepresentation parsed = (IdentityRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getExternalId()).isEqualTo(externalId);
        assertThat(parsed.getExternalIdsOfGlobalId()).isEqualTo(externalIdsOfGlobalId);
    }
    
}
