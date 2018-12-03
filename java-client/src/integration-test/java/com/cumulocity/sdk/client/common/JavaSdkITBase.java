/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.common;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryIT;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Properties;

public class JavaSdkITBase {
    private static TenantCreator tenantCreator;
    protected static PlatformImpl platform;
    protected static PlatformImpl bootstrapPlatform;

    @BeforeClass
    public static void createTenantWithApplication() throws Exception {
        platform = createPlatform(false);
        bootstrapPlatform = createPlatform(true);

        tenantCreator = new TenantCreator(platform);
        tenantCreator.createTenant();

    }

    @AfterClass
    public static void removeTenantAndApplication() throws Exception {
        tenantCreator = new TenantCreator(platform);
        tenantCreator.removeTenant();
    }

    private static PlatformImpl createPlatform(boolean bootstrap) throws IOException {
        Properties cumulocityProps = new Properties();
        cumulocityProps.load(InventoryIT.class.getClassLoader().getResourceAsStream("cumulocity-test.properties"));

        String userKey = bootstrap ? "cumulocity.bootstrap.user" : "cumulocity.user";
        String userPassword = bootstrap ? "cumulocity.bootstrap.password" : "cumulocity.password";
        SystemPropertiesOverrider p = new SystemPropertiesOverrider(cumulocityProps);
        return new PlatformImpl(
                p.get("cumulocity.host"),
                CumulocityCredentials.builder()
                        .tenantId(p.get("cumulocity.tenant"))
                        .username(p.get(userKey))
                        .password(p.get(userPassword))
                        .buildBasic());
    }
}
