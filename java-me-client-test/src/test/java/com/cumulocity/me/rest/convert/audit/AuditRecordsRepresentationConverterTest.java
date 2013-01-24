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
