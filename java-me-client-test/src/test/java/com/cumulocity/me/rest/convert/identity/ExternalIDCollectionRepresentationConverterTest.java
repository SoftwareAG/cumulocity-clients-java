/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
