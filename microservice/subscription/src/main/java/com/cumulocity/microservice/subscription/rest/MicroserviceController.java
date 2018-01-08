package com.cumulocity.microservice.subscription.rest;

import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("microservice")
public class MicroserviceController {

    @Autowired(required = false)
    private MicroserviceSubscriptionsService microserviceSubscriptionsService;

    @GetMapping(
            value = "/refreshSubscriptions")
    public RefreshStatus refreshSubscriptions() {
        if (microserviceSubscriptionsService == null) {
            return RefreshStatus.DISABLED;
        }
        try {
            microserviceSubscriptionsService.subscribe();
            return RefreshStatus.OK;
        } catch (Exception ex) {
            log.error("Cannot refresh microservice subscriptions", ex);
            return RefreshStatus.FAILED;
        }
    }



    public static class RefreshStatus{
        private static RefreshStatus DISABLED = new RefreshStatus("DISABLED");
        private static RefreshStatus OK = new RefreshStatus("OK");
        private static RefreshStatus FAILED = new RefreshStatus("FAILED");




        private  String status;

        public RefreshStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public RefreshStatus setStatus(String status) {
            this.status = status;
            return this;
        }
    }
}
