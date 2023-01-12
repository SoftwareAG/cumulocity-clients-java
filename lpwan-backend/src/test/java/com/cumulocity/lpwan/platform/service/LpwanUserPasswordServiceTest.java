package com.cumulocity.lpwan.platform.service;

import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.lpwan.sample.connection.model.SampleConnection;
import com.cumulocity.lpwan.util.ImmediateMicroserviceSubscriptionsService;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LpwanUserPasswordServiceTest {

    private static final String APP_NAME = "loriot";

    private static final String LPWAN_USER_PASSWORD_KEY = "credentials.loriot.password";

    @Mock
    private TenantOptionApi tenantOptionApi;

    private MicroserviceSubscriptionsService subscriptionsService = new ImmediateMicroserviceSubscriptionsService("tenant");

    private LpwanUserPasswordService lpwanUserPasswordService;


    @Before
    public void setup() {
        lpwanUserPasswordService = new LpwanUserPasswordService(tenantOptionApi, subscriptionsService);
        lpwanUserPasswordService.setAppName(APP_NAME);
        LnsConnectionDeserializer.registerLnsConnectionConcreteClass("Loriot", SampleConnection.class);
    }


    @Test
    public void testGeneratePasswordAndSave() {
        String generatedPassword = lpwanUserPasswordService.generatePasswordAndSave("testUserName");
        verify(tenantOptionApi, times(1))
                .save(argThat(
                        object -> {
                            final OptionRepresentation optionRepresentation = object;
                            return (APP_NAME.equals(optionRepresentation.getCategory())
                                    && LPWAN_USER_PASSWORD_KEY.equals(optionRepresentation.getKey())
                                    && generatedPassword.equals(optionRepresentation.getValue()));
                        }
                ));
    }

    @Test
    @SneakyThrows
    public void testGetPassword() {
        when(tenantOptionApi.getOption(new OptionPK(APP_NAME, LPWAN_USER_PASSWORD_KEY))).then(invocationOnMock -> {
            return OptionRepresentation.asOptionRepresentation(APP_NAME, LPWAN_USER_PASSWORD_KEY, "test");
        });
        Optional<String> password = lpwanUserPasswordService.get();

        verify(tenantOptionApi, times(1))
                .getOption(argThat(
                        object -> {
                            final OptionPK optionPK = object;
                            return (APP_NAME.equals(optionPK.getCategory())
                                    && LPWAN_USER_PASSWORD_KEY.equals(optionPK.getKey())
                                    && password.get().equals("test"));
                        }
                ));
    }

    @Test
    @SneakyThrows
    public void testPasswordNotFound() {
        when(tenantOptionApi.getOption(any(OptionPK.class))).then(invocationOnMock -> {
            throw new SDKException(404, "");
        });
        Optional<String> password = lpwanUserPasswordService.get();
        verify(tenantOptionApi, times(1))
                .getOption(argThat(
                        object -> {
                            final OptionPK optionPK = object;
                            return (APP_NAME.equals(optionPK.getCategory())
                                    && LPWAN_USER_PASSWORD_KEY.equals(optionPK.getKey()));
                        }
                ));
        assertFalse(password.isPresent());
    }
}
