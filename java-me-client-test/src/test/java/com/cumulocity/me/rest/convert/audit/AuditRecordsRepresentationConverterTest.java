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

import static com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentationBuilder.aAuditRecordsRepresentation;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.convert.TestFragment;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentationBuilder;

public class AuditRecordsRepresentationConverterTest {

    JsonConversionService conversionService = mock(JsonConversionService.class);
    AuditRecordsRepresentationConverter converter = new AuditRecordsRepresentationConverter();
    
    String auditRecordsForApplication = "for_app";
    String auditRecordsForType = "for_type";
    String auditRecordsForTypeAndApplication = "for_type_app";
    String auditRecordsForTypeAndUserAndApplication = "for_type_user_app";
    String auditRecordsForUser = "for_user";
    String auditRecordsForUserAndApplication = "for_user_app";
    String auditRecordsForUserAndType = "for_user_type";
    
    TestFragment fragment = new TestFragment();
    AuditRecordsRepresentationBuilder representation = aAuditRecordsRepresentation();
    
    @Before
    public void setUp() {
        converter.setJsonConversionService(conversionService);
    }
    
    @Test
    public void shouldParseEmptyJson() throws Exception {
        Object parsed = converter.fromJson(new JSONObject("{}"));
        
        assertThat(parsed).isInstanceOf(AuditRecordsRepresentation.class);
    }
    
    @Test
    public void shouldParseWithSimpleProps() throws Exception {
        JSONObject json = new JSONObject("{\"auditRecordsForUserAndType\":\"for_user_type\",\"auditRecordsForUserAndApplication\":\"for_user_app\",\"auditRecordsForType\":\"for_type\",\"auditRecordsForTypeAndUserAndApplication\":\"for_type_user_app\",\"auditRecordsForTypeAndApplication\":\"for_type_app\",\"auditRecordsForApplication\":\"for_app\",\"auditRecordsForUser\":\"for_user\"}");
        
        AuditRecordsRepresentation parsed = (AuditRecordsRepresentation) converter.fromJson(json);
        
        assertThat(parsed.getAuditRecordsForApplication()).isEqualTo(auditRecordsForApplication);
        assertThat(parsed.getAuditRecordsForType()).isEqualTo(auditRecordsForType);
        assertThat(parsed.getAuditRecordsForTypeAndApplication()).isEqualTo(auditRecordsForTypeAndApplication);
        assertThat(parsed.getAuditRecordsForTypeAndUserAndApplication()).isEqualTo(auditRecordsForTypeAndUserAndApplication);
        assertThat(parsed.getAuditRecordsForUser()).isEqualTo(auditRecordsForUser);
        assertThat(parsed.getAuditRecordsForUserAndApplication()).isEqualTo(auditRecordsForUserAndApplication);
        assertThat(parsed.getAuditRecordsForUserAndType()).isEqualTo(auditRecordsForUserAndType);
        
    }
}
