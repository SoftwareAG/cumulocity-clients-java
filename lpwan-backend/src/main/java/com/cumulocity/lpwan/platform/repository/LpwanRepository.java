package com.cumulocity.lpwan.platform.repository;

import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.model.ID;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cumulocity.model.event.CumulocityAlarmStatuses.*;

@Component
@TenantScope
public class LpwanRepository {

    public static final String MICROSERVICE_ID_TYPE = "c8y_Microservice";
    public static final String ALARM_TYPE = "c8y_ProviderAccessAlarm";
    public static final String CRITICAL = "CRITICAL";

    @Autowired
    private IdentityApi identityApi;

    @Autowired
    private AlarmApi alarmApi;

    @Autowired
    private InventoryApi inventoryApi;

    public GId findOrCreateSource() {
        return findGId(MICROSERVICE_ID_TYPE, LnsConnectionDeserializer.getRegisteredAgentName()).or(new Supplier<GId>() {
            public GId get() {
                final GId source = createManagedObject(MICROSERVICE_ID_TYPE, LnsConnectionDeserializer.getRegisteredAgentName());
                createExternalId(source, MICROSERVICE_ID_TYPE, LnsConnectionDeserializer.getRegisteredAgentName());
                return source;
            }
        });
    }

    public ExternalIDRepresentation createExternalId(GId sourceId, String type, String identifier) {
        final ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(sourceId);

        final ExternalIDRepresentation externalId = new ExternalIDRepresentation();
        externalId.setManagedObject(source);
        externalId.setExternalId(identifier);
        externalId.setType(type);
        return identityApi.create(externalId);
    }

    public com.google.common.base.Optional<GId> findGId(String type, String identifier) {
        try {
            final ExternalIDRepresentation externalId = identityApi.getExternalId(new ID(type, identifier));
            return com.google.common.base.Optional.of(externalId.getManagedObject().getId());
        } catch (final SDKException ex) {
            if (ex.getHttpStatus() != 404) {
                throw ex;
            }
        }
        return com.google.common.base.Optional.absent();
    }

    public GId createManagedObject(String type, String name) {
        final ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        managedObject.setType(type);
        managedObject.setName(name);
        return inventoryApi.create(managedObject).getId();
    }

    public GId createAlarm(GId sourceId, String severity, String type, String text) {
        final ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(sourceId);

        final AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(source);
        alarm.setSeverity(severity);
        alarm.setType(type);
        alarm.setText(text);
        alarm.setDateTime(DateTime.now());
        alarm.setStatus(ACTIVE.name());

        return alarmApi.create(alarm).getId();
    }

    public void clearAlarm(GId source, String type) {
        final Iterable<AlarmRepresentation> alarmMaybe = findAlarm(source, type, ACTIVE, ACKNOWLEDGED);
        for (final AlarmRepresentation alarm : alarmMaybe) {
            alarm.setStatus(CLEARED.name());
            alarmApi.update(alarm);
        }
    }

    public Iterable<AlarmRepresentation> findAlarm(GId source, String type, CumulocityAlarmStatuses... alarmStatuses) {
        try {
            final AlarmFilter filter = new AlarmFilter().bySource(source).byType(type);
            if (alarmStatuses != null && alarmStatuses.length > 0) {
                filter.byStatus(alarmStatuses);
            }
            final AlarmCollection alarms = alarmApi.getAlarmsByFilter(filter);
            return alarms.get().allPages();
        } catch (final SDKException ex) {
            if (ex.getHttpStatus() != 404) {
                throw ex;
            }
        }
        return FluentIterable.of();
    }
}
