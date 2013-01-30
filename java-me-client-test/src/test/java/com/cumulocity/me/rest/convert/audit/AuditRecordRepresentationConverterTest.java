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
package com.cumulocity.me.rest.convert.audit;

import static com.cumulocity.me.rest.representation.audit.AuditRecordRepresentationBuilder.aAuditRecordRepresentation;
import static com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext.CREATE;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.lang.HashSet;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.convert.TestFragment;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentationBuilder;
import com.cumulocity.me.rest.validate.RepresentationValidationException;

public class AuditRecordRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    AuditRecordRepresentationConverter converter = new AuditRecordRepresentationConverter();
    
    String application = "app";
    String severity = "sever";
    String activity = "activity";
    String user = "us";
    String auditRecordsForUser = "for_user";
    String auditRecordsForUserAndApplication = "for_user_app";
    String auditRecordsForUserAndType = "for_user_type";
    
    TestFragment fragment = new TestFragment();
    AuditRecordRepresentationBuilder representation = aAuditRecordRepresentation();
    
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
        representation
        .withApplication(application)
        .withSeverity(severity)
        .withActivity(activity)
        .withUser(user);
        
        JSONObject json = converter.toJson(representation.build());

        assertThat(json.toString()).isEqualTo("{\"user\":\"us\",\"severity\":\"sever\",\"application\":\"app\",\"activity\":\"activity\"}");
    }
    
    @Test
    public void shouldFormatWithChanges() throws Exception {
        representation
        .withApplication(application)
        .withSeverity(severity)
        .withActivity(activity)
        .withUser(user)
        .withChanges(new HashSet());
        
        JSONObject json = converter.toJson(representation.build());

        assertThat(json.toString()).isEqualTo("{\"user\":\"us\",\"severity\":\"sever\",\"application\":\"app\",\"changes\":[],\"activity\":\"activity\"}");
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(AuditRecordRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"user\":\"us\",\"severity\":\"sever\",\"application\":\"app\",\"activity\":\"activity\"}");
        
        AuditRecordRepresentation parsed = (AuditRecordRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getUser()).isEqualTo(user);
        assertThat(parsed.getSeverity()).isEqualTo(severity);
        assertThat(parsed.getActivity()).isEqualTo(activity);
        assertThat(parsed.getApplication()).isEqualTo(application);
        assertThat(parsed.getChanges()).isNull();
    }
    
    @Test
    public void shouldParseWithChanges() throws Exception {
        JSONObject json = new JSONObject("{\"user\":\"us\",\"severity\":\"sever\",\"application\":\"app\",\"changes\":[],\"activity\":\"activity\"}");
        
        AuditRecordRepresentation parsed = (AuditRecordRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getUser()).isEqualTo(user);
        assertThat(parsed.getSeverity()).isEqualTo(severity);
        assertThat(parsed.getActivity()).isEqualTo(activity);
        assertThat(parsed.getApplication()).isEqualTo(application);
        assertThat(parsed.getChanges()).isNotNull();
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNotNullActivity() throws Exception {
        representation.withSeverity(severity);

        converter.isValid(representation.build(), CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNotNullSeverity() throws Exception {
        representation.withActivity(activity);

        converter.isValid(representation.build(), CREATE);
    }

    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullChanges() throws Exception {
        representation.withActivity(activity).withSeverity(severity).withChanges(new HashSet());

        converter.isValid(representation.build(), CREATE);
    }
}
