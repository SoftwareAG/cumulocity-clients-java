package com.cumulocity.rest.representation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.GroupRepresentation;
import com.cumulocity.rest.representation.user.RoleRepresentation;
import com.cumulocity.rest.representation.user.UserRepresentation;

public class RepresentationToJSONParsingTest {

    @Test
    public void shouldNotIncludeNullProperties() {
        //given
        BaseResourceRepresentation[] representations = {
                new AlarmRepresentation(), new EventRepresentation(),
                new ManagedObjectRepresentation(), new AuditRecordRepresentation(),
                new MeasurementRepresentation(), new ExternalIDRepresentation(),
                new OperationRepresentation(), new UserRepresentation(),
                new RoleRepresentation(), new CurrentUserRepresentation(),
                new GroupRepresentation(), new TestCollectionRepresentation(),
                new OptionRepresentation()};

        // when & then
        assertThat(representations).allSatisfy(rep -> assertThat(rep.toJSON()).doesNotContain("null"));
    }
}
