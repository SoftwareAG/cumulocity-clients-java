package com.cumulocity.sdk.client.common;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.cumulocity.me.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryIT;

public class JavaSdkITBase {
    private static ApplicationCreator applicationCreator;
    private static TenantCreator tenantCreator;
    protected static PlatformImpl platform;

    @BeforeClass
    public static void createTenantWithApplication() throws Exception {
        platform = createPlatform();

        applicationCreator = new ApplicationCreator(platform);
        applicationCreator.createApplication();

        tenantCreator = new TenantCreator(platform, applicationCreator);
        tenantCreator.createTenant();
    }

    @AfterClass
    public static void removeTenantAndApplication() throws Exception {
        try {
            tenantCreator = new TenantCreator(platform, applicationCreator);
            tenantCreator.removeTenant();
        } finally {
            applicationCreator = new ApplicationCreator(platform);
            applicationCreator.removeApplication();
        }
    }

    private static PlatformImpl createPlatform() throws IOException {
        Properties cumulocityProps = new Properties();
        cumulocityProps.load(InventoryIT.class.getClassLoader().getResourceAsStream("cumulocity-test.properties"));

        SystemPropertiesOverrider p = new SystemPropertiesOverrider(cumulocityProps);
        return new PlatformImpl(
                p.get("cumulocity.host"),
                p.get("cumulocity.tenant"),
                p.get("cumulocity.user"),
                p.get("cumulocity.password"),
                p.get("cumulocity.applicationKey"));
    }

}
