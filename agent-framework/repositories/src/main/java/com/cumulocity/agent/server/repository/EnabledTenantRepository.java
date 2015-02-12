package com.cumulocity.agent.server.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceCredentials;

@Component
public class EnabledTenantRepository {
    private final Environment environment;

    @Autowired
    public EnabledTenantRepository(Environment environment) {
        this.environment = environment;
    }

    public List<DeviceCredentials> findAll() {
        List<DeviceCredentials> tenants = new LinkedList<DeviceCredentials>();
        int count = 1;
        while (environment.containsProperty("C8Y." + count + ".tenant")) {
            String tenant = environment.getProperty("C8Y." + count + ".tenant");
            String username = environment.getProperty("C8Y." + count + ".username");
            String password = environment.getProperty("C8Y." + count + ".password");
            String appKey = environment.getProperty("C8Y." + count + ".appkey");
            tenants.add(new DeviceCredentials(tenant, username, password, appKey, null));
            count++;
        }

        return tenants;
    }
}
