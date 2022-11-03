package com.cumulocity.rest.representation;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.junit.jupiter.api.Test;

import static com.cumulocity.model.idtype.GId.asGId;
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

    @Test
    public void alarmsWithSameSourceIdAreEqual(){
        //given
        AlarmRepresentation alarm1 = alarmWithSource(asGId("11"));
        AlarmRepresentation alarm2 = alarmWithSource(asGId("11"));

        //when-then
        assertThat(alarm1).isEqualTo(alarm2);
    }

    @Test
    public void alarmsWithDifferentSourceIdAreNotEqual(){
        //given
        AlarmRepresentation alarm1 = alarmWithSource(asGId("11"));
        AlarmRepresentation alarm2 = alarmWithSource(asGId("22"));
        AlarmRepresentation alarm3 = new AlarmRepresentation(); //verify null source

        //when-then
        assertThat(alarm1).isNotEqualTo(alarm2);
        assertThat(alarm1).isNotEqualTo(alarm3);
        assertThat(alarm3).isNotEqualTo(alarm2);
    }

    private AlarmRepresentation alarmWithSource(GId sourceId) {
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(sourceId);
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(source);
        return alarm;
    }

    @Test
    public void auditsWithSameSourceIdAreEqual(){
        //given
        AuditRecordRepresentation audit1 = auditWithSource(asGId("11"));
        AuditRecordRepresentation audit2 = auditWithSource(asGId("11"));

        //when-then
        assertThat(audit1).isEqualTo(audit2);
    }

    @Test
    public void auditsWithDifferentSourceIdAreNotEqual(){
        //given
        AuditRecordRepresentation audit1 = auditWithSource(asGId("11"));
        AuditRecordRepresentation audit2 = auditWithSource(asGId("22"));
        AuditRecordRepresentation audit3 = new AuditRecordRepresentation(); //verify null source

        //when-then
        assertThat(audit1).isNotEqualTo(audit2);
        assertThat(audit1).isNotEqualTo(audit3);
        assertThat(audit3).isNotEqualTo(audit2);
    }

    private AuditRecordRepresentation auditWithSource(GId sourceId) {
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(sourceId);
        AuditRecordRepresentation audit = new AuditRecordRepresentation();
        audit.setSource(source);
        return audit;
    }

}
