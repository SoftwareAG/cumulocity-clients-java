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
package com.cumulocity.me.rest.convert.devicecontrol;

import static com.cumulocity.me.rest.convert.JSONMatcher.jsonObject;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.representation.devicecontrol.OperationRepresentationBuilder.aOperationRepresentation;
import static com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext.CREATE;
import static com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext.UPDATE;
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
import com.cumulocity.me.rest.convert.operation.OperationRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.devicecontrol.OperationRepresentationBuilder;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;
import com.cumulocity.me.rest.validate.RepresentationValidationException;

public class OperationRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    OperationRepresentationConverter converter = new OperationRepresentationConverter();

    GId id = new GId("testid");
    GId deviceId = new GId("deviceId");
    String status = "some_status";
    String failureReason = "some_failure_reason";
    Date creationTime = new Date(978307201000L); // 2001-01-01 00:00:01
    
    OperationRepresentation source = new OperationRepresentation();
    TestFragment fragment = new TestFragment();
    OperationRepresentationBuilder representation = aOperationRepresentation();

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
        representation.withID(id).withDeviceId(deviceId).withFailureReason(failureReason).withStatus(status).withCreationTime(creationTime);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"status\":\"some_status\",\"creationTime\":\"2001-01-01T00:00:01.0Z\",\"deviceId\":\"deviceId\",\"failureReason\":\"some_failure_reason\",\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(OperationRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"creationTime\":\"2001-01-01T00:00:01.0Z\",\"status\":\"some_status\",\"deviceId\":\"deviceId\",\"failureReason\":\"some_failure_reason\",\"id\":\"testid\"}");
        
        OperationRepresentation parsed = (OperationRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getDeviceId()).isEqualTo(deviceId);
        assertThat(parsed.getStatus()).isEqualTo(status);
        assertThat(parsed.getFailureReason()).isEqualTo(failureReason);
        assertThat(parsed.getCreationTime()).isEqualTo(creationTime);
    }
    
    @Test
    public void shouldFormatWithFragments() throws Exception {
        when(conversionService.toJson(fragment)).thenReturn(aJSONObject().withProperty("type", "test").build());
        representation.withID(id).with(fragment);
        
        JSONObject json = converter.toJson(representation.build());
        
        assertThat(json.toString()).isEqualTo("{\"com_cumulocity_me_rest_convert_TestFragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
    }
    
    @Test
    public void shouldParseWithDeviceIds() throws Exception {
        JSONObject json = new JSONObject("{\"creationTime\":\"2001-01-01T00:00:01.0Z\",\"status\":\"some_status\",\"deviceId\":\"deviceId\",\"failureReason\":\"some_failure_reason\",\"id\":\"testid\"}");
        
        OperationRepresentation parsed = (OperationRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getDeviceId()).isEqualTo(deviceId);
        assertThat(parsed.getStatus()).isEqualTo(status);
        assertThat(parsed.getFailureReason()).isEqualTo(failureReason);
        assertThat(parsed.getCreationTime()).isEqualTo(creationTime);
    }
    
    @Test
    public void shouldParseWithFragment() throws Exception {
        when(conversionService.fromJson(jsonObject("{\"type\":\"test\"}"), same(TestFragment.class))).thenReturn(fragment);
        JSONObject json = new JSONObject("{\"com_cumulocity_me_rest_convert_TestFragment\":{\"type\":\"test\"},\"id\":\"testid\"}");
        
        OperationRepresentation parsed = (OperationRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getId()).isEqualTo(id);
        assertThat(parsed.getAttrs().size()).isEqualTo(1);
        assertThat(parsed.get(TestFragment.class)).isSameAs(fragment);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNotNullDeviceId() throws Exception {
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullCreationTime() throws Exception {
        representation.withCreationTime(creationTime);
        
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullStatus() throws Exception {
        representation.withStatus(status);
        
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullFailureReason() throws Exception {
        representation.withFailureReason(failureReason);
        
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullId() throws Exception {
        representation.withID(new GId());
        
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullDeviceExternalIDs() throws Exception {
        representation.with(new ExternalIDCollectionRepresentation());
        
        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullIdUpdate() throws Exception {
        representation.withID(new GId());

        converter.isValid(representation.build(), UPDATE);
    }

    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullDeviceIdUpdate() throws Exception {
        representation.withDeviceId(new GId());

        converter.isValid(representation.build(), UPDATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullCreationTimeUpdate() throws Exception {
        representation.withCreationTime(creationTime);

        converter.isValid(representation.build(), UPDATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullDeviceExtIdsUpdate() throws Exception {
        representation.with(new ExternalIDCollectionRepresentation());

        converter.isValid(representation.build(), UPDATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNotNullStatusUpdate() throws Exception {
        converter.isValid(representation.build(), UPDATE);
    }

}
