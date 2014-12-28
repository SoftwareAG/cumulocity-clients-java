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
