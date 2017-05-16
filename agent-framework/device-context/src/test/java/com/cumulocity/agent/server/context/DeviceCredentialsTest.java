package com.cumulocity.agent.server.context;

import com.sun.jersey.core.util.Base64;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static com.cumulocity.agent.server.context.DeviceCredentials.AUTH_PREFIX;
import static com.cumulocity.agent.server.context.DeviceCredentials.PAYPAL_AUTH_PREFIX;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by dombiel on 12.05.17.
 */
public class DeviceCredentialsTest {

    public static final String APP_KEY = null;
    public static final int PAGE_SIZE = 25;

    @Test
    public void parseBasicAuthorization() throws UnsupportedEncodingException {
        String auth = "tenantId/user:password";

        DeviceCredentials actual = DeviceCredentials.from("BASIC " + encode(auth), APP_KEY, PAGE_SIZE);

        DeviceCredentials expected = new DeviceCredentials("tenantId", "user", "password", APP_KEY, null, PAGE_SIZE, AUTH_PREFIX);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void parsePaypalAuthorization() throws UnsupportedEncodingException {
        String auth = "tenantId:locationId:tabId";

        DeviceCredentials actual = DeviceCredentials.from("Paypal " + encode(auth), APP_KEY, PAGE_SIZE);

        DeviceCredentials expected = new DeviceCredentials("tenantId", "locationId", "tabId", APP_KEY, null, PAGE_SIZE, PAYPAL_AUTH_PREFIX);
        assertThat(actual).isEqualTo(expected);
    }

    private String encode(String auth) {
        Base64.encode(auth);
        Charset ascii = Charset.forName("US-ASCII");
        return new String(Base64.encode(auth), ascii);
    }

}