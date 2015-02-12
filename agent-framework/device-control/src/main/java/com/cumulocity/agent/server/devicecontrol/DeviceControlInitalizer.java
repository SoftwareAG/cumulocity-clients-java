package com.cumulocity.agent.server.devicecontrol;

import static com.cumulocity.model.idtype.GId.asGId;
import static com.cumulocity.model.operation.OperationStatus.PENDING;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.agent.server.repository.EnabledTenantRepository;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;
import com.cumulocity.sdk.client.notification.Subscription;

@Component
public class DeviceControlInitalizer {

    private final Logger log = LoggerFactory.getLogger(DeviceControlInitalizer.class);

    private final EnabledTenantRepository tenantsConfig;

    private final DeviceControlRepository deviceControlRepository;

    private final DeviceContextService contextService;

    private final DeviceControlListener listener;

    @Autowired
    public DeviceControlInitalizer(EnabledTenantRepository tenantsConfig, DeviceControlRepository deviceControlRepository,
            DeviceContextService contextService, DeviceControlListener listener) {
        this.tenantsConfig = tenantsConfig;
        this.deviceControlRepository = deviceControlRepository;
        this.contextService = contextService;
        this.listener = listener;
    }

    @PostConstruct
    public void initalize() {
        forEeach(new Runnable() {

            @Override
            public void run() {
                subscribeForNewOperations();
                loadPendingOperations();
            }

        });

    }

    private void forEeach(final Runnable task) {
        for (DeviceCredentials cumulocityLogin : tenantsConfig.findAll()) {
            log.info("device control initialization for {}", cumulocityLogin);
            contextService.runWithinContext(new DeviceContext(cumulocityLogin), task);
        }
    }

    private void subscribeForNewOperations() {
        log.info("start for listening for operations");
        deviceControlRepository.subscribe(asGId("*"), listener);
    }

    private void loadPendingOperations() {
        log.info("dispatching pending operations");
        for (OperationRepresentation operation : deviceControlRepository.findAllByFilter(pendingStatus())) {
            dispatchOperation(operation);
        }
    }

    private void dispatchOperation(final OperationRepresentation operation) {
        listener.onNotification(new Subscription<GId>() {

            @Override
            public void unsubscribe() {
            }

            @Override
            public GId getObject() {
                return operation.getDeviceId();
            }
        }, operation);
    }

    private OperationFilter pendingStatus() {
        return new OperationFilter().byStatus(PENDING);
    }

}
