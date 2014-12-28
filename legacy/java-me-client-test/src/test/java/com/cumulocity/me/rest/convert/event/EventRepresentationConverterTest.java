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
package com.cumulocity.me.rest.convert.event;

import static com.cumulocity.me.rest.convert.JSONMatcher.jsonObject;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.representation.event.EventRepresentationBuilder.aEventRepresentation;
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
import com.cumulocity.me.rest.representation.event.EventRepresentation;
import com.cumulocity.me.rest.representation.event.EventRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class EventRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    EventRepresentationConverter converter = new EventRepresentationConverter();
    
    GId id = new GId("testid");
    String type = "some_type";
    Date time = new Date(978307201000L); // 2001-01-01 00:00:01
    String text = "sample text";
    
    ManagedObjectRepresentation source = new ManagedObjectRepresentation();
    TestFragment fragment = new TestFragment();
    EventRepresentationBuilder representation = aEventRepresentation();
    
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
        representation.withID(id).withType(type).withTime(time).withCreationTime(time).withText(text);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"type\":\"some_type\",\"creationTime\":\"2001-01-01T00:00:01.0Z\",\"time\":\"2001-01-01T00:00:01.0Z\",\"text\":\"sample text\",\"id\":\"testid\"}");
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
    public void shouldFormatWithStringFragments() throws Exception {
        representation.withID(id).with("Test_Fragment", "{\"type\":\"test\"}");
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"Test_Fragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(EventRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"type\":\"some_type\",\"creationTime\":\"2001-01-01T00:00:01.0Z\",\"time\":\"2001-01-01T00:00:01.0Z\",\"text\":\"sample text\",\"id\":\"testid\"}");
        
        EventRepresentation parsed = (EventRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getType()).isEqualTo(type);
        assertThat(parsed.getTime()).isEqualTo(time);
        assertThat(parsed.getCreationTime()).isEqualTo(time);
        assertThat(parsed.getText()).isEqualTo(text);
    }
    
    @Test
    public void shouldParseWithComplexProps() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"id\":\"src_id\"}"), same(ManagedObjectRepresentation.class))).thenReturn(source);
        JSONObject json = new JSONObject("{\"source\":{\"id\":\"src_id\"},\"id\":\"testid\"}");
        
        EventRepresentation parsed = (EventRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getSource()).isSameAs(source);
    }
    
    @Test
    public void shouldParseWithFragment() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"type\":\"test\"}"), same(TestFragment.class))).thenReturn(fragment);
        JSONObject json = new JSONObject("{\"com_cumulocity_me_rest_convert_TestFragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
        
        EventRepresentation parsed = (EventRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getAttrs().size()).isEqualTo(1);
        assertThat(parsed.get(TestFragment.class)).isSameAs(fragment);
    }
    
    @Test
    public void shouldParseWithStringFragment() throws Exception {
        JSONObject json = new JSONObject("{\"test_fragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
        
        EventRepresentation parsed = (EventRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getAttrs().size()).isEqualTo(1);
        assertThat(parsed.get("test_fragment")).isEqualTo("{\"type\":\"test\"}");
    }
}
