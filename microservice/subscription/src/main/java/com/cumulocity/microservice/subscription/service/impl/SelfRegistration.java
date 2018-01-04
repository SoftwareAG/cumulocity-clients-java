package com.cumulocity.microservice.subscription.service.impl;

import com.cumulocity.microservice.subscription.repository.MicroserviceApiRepresentation;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Suppliers;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static java.lang.System.currentTimeMillis;

public class SelfRegistration {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SelfRegistration.class);

    public ApplicationUserRepresentation register(String baseUrl, RestOperations platform, String name) {
        try {
            log.info("Attempt to self register with name {}", name);
            ApplicationApi applicationApi = new ApplicationApi(platform, MicroserviceApiRepresentation.of(Suppliers.ofInstance(baseUrl)));
            awaitPlatform(applicationApi);
            ApplicationRepresentation application = applicationApi.getByName(name);
            if (application == null) {
                log.info("Application not registered creating");
                application = new ApplicationRepresentation();
                application.setName(name);
                application.setKey(name + "-application-key");
                application.setType(MICROSERVICE);
                application = applicationApi.create(application);
            } else {
                log.info("Application already registered");
            }

            return applicationApi.getBootstrapUser(application.getId());
        } catch (Exception ex) {
            log.error("Self registration process failed ", ex);
            throw ex;
        }
    }

    private void awaitPlatform(ApplicationApi resource) {
        long started = currentTimeMillis();
        while (true) {
            log.info("waiting for platform to be considered up");
            try {
                ApplicationCollectionRepresentation applications = resource.list();
                if (applications != null) {
                    return;
                }

            } catch (Exception ex) {
                if ((currentTimeMillis() - started) > TimeUnit.MINUTES.toMillis(5)) {
                    log.error("awaiting for platform took very long time. Please verify connection.");
                    throw ex;
                } else {
                    log.info("platform connection problem {}", ex.getMessage());
                }
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        }
    }


}
