package com.cumulocity.rest.representation;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RepresentationModelTest {

    private final String TYPE_ONE = "TYPE_ONE";
    private final String TYPE_TWO = "TYPE_TWO";

    @Test
    public void shouldAlarmRepresentationBeEqual(){
        //given
        AlarmRepresentation firstAlarmRepresentation = new AlarmRepresentation();
        AlarmRepresentation secondAlarmRepresentation = new AlarmRepresentation();

        //when
        boolean isAlarmRepresentationEquals = firstAlarmRepresentation.equals(secondAlarmRepresentation);

        //then
        assertThat(isAlarmRepresentationEquals).isTrue();
    }

    @Test
    public void shouldAuditRepresentationBeEqual(){
        //given
        AuditRecordRepresentation firstAuditRecordRepresentation = new AuditRecordRepresentation();
        AuditRecordRepresentation secondAuditRecordRepresentation = new AuditRecordRepresentation();

        //when
        boolean isAuditRecordRepresentationEquals = firstAuditRecordRepresentation.equals(secondAuditRecordRepresentation);

        //then
        assertThat(isAuditRecordRepresentationEquals).isTrue();
    }

    @Test
    public void shouldAlarmRepresentationBeEqualWhenSuperTypeEqual(){
        //given
        AlarmRepresentation firstAlarmRepresentation = new AlarmRepresentation();
        AlarmRepresentation secondAlarmRepresentation = new AlarmRepresentation();

        firstAlarmRepresentation.setType(TYPE_ONE);
        secondAlarmRepresentation.setType(TYPE_ONE);

        //when
        boolean isAlarmRepresentationEquals = firstAlarmRepresentation.equals(secondAlarmRepresentation);

        //then
        assertThat(isAlarmRepresentationEquals).isTrue();
    }

    @Test
    public void shouldAlarmRepresentationBeNotEqualWhenSuperTypeNotEqual(){
        //given
        AlarmRepresentation firstAlarmRepresentation = new AlarmRepresentation();
        AlarmRepresentation secondAlarmRepresentation = new AlarmRepresentation();

        firstAlarmRepresentation.setType(TYPE_ONE);
        secondAlarmRepresentation.setType(TYPE_TWO);

        //when
        boolean isAlarmRepresentationEquals = firstAlarmRepresentation.equals(secondAlarmRepresentation);

        //then
        assertThat(isAlarmRepresentationEquals).isFalse();
    }

    @Test
    public void shouldAuditRepresentationBeEqualWhenSuperTypeEqual(){
        //given
        AuditRecordRepresentation firstAuditRecordRepresentation = new AuditRecordRepresentation();
        AuditRecordRepresentation secondAuditRecordRepresentation = new AuditRecordRepresentation();

        firstAuditRecordRepresentation.setType(TYPE_ONE);
        secondAuditRecordRepresentation.setType(TYPE_ONE);

        //when
        boolean isAuditRecordRepresentationEquals = firstAuditRecordRepresentation.equals(secondAuditRecordRepresentation);

        //then
        assertThat(isAuditRecordRepresentationEquals).isTrue();
    }

    @Test
    public void shouldAuditRepresentationBeNotEqualWhenSuperTypeNotEqual(){
        // given
        AuditRecordRepresentation firstAuditRecordRepresentation = new AuditRecordRepresentation();
        AuditRecordRepresentation secondAuditRecordRepresentation = new AuditRecordRepresentation();

        firstAuditRecordRepresentation.setType(TYPE_ONE);
        secondAuditRecordRepresentation.setType(TYPE_TWO);

        //when
        boolean isAuditRecordRepresentationEquals = firstAuditRecordRepresentation.equals(secondAuditRecordRepresentation);

        //then
        assertThat(isAuditRecordRepresentationEquals).isFalse();
    }
}
