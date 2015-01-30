package com.cumulocity.agent.server.devicecontrol;

import static com.cumulocity.model.event.CumulocityAlarmStatuses.ACTIVE;
import static com.cumulocity.model.event.CumulocitySeverities.CRITICAL;
import static com.cumulocity.sdk.client.inventory.InventoryFilter.searchInventory;
import static com.google.common.collect.FluentIterable.from;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.repository.AlarmRepository;
import com.cumulocity.agent.server.repository.InventoryRepository;
import com.cumulocity.agent.server.repository.ManagedObjects;
import com.cumulocity.model.Agent;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;

@Component
public class DeviceControlListener implements SubscriptionListener<GId, OperationRepresentation> {

    private final Logger log = LoggerFactory.getLogger(DeviceControlListener.class);

    private final OperationsDispatcher dispatcher;

    private final AlarmRepository alarmRepository;

    private final InventoryRepository inventoryRepository;

    private final String applicationId;

    @Autowired
    public DeviceControlListener(OperationsDispatcher dispatcher, AlarmRepository alarmRepository, InventoryRepository inventoryRepository,
            @Value("${server.id}") String applicationId) {
        this.dispatcher = dispatcher;
        this.alarmRepository = alarmRepository;
        this.inventoryRepository = inventoryRepository;
        this.applicationId = applicationId;
    }

    @Override
    public void onNotification(Subscription<GId> subscription, OperationRepresentation notification) {
        if (log.isDebugEnabled()) {
            log.debug("recived operation for {} -> {}", subscription.getObject(), notification.toJSON());
        }
        dispatcher.dispatch(notification);
    }

    @Override
    public void onError(Subscription<GId> subscription, Throwable ex) {
        log.error("agent unable to subscribe for opeartion ", ex);
        final AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setType("c8y_agent_Connection");
        alarm.setTime(new Date());
        alarm.setText(String.format("Agent %s unable to subscribe for operations. Cause : %s", applicationId, ex.getMessage()));
        alarm.setSeverity(CRITICAL.name());
        alarm.setStatus(ACTIVE.name());
        alarm.setSource(findAgent());
        alarmRepository.save(alarm);
    }

    public ManagedObjectRepresentation findAgent() {
        return ManagedObjects.asManagedObject(from(inventoryRepository.findAllByFilter(searchInventory().byFragmentType(Agent.class)))
                .first().get().getId());
    }
}
