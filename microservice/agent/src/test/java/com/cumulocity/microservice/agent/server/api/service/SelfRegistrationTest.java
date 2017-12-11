package com.cumulocity.microservice.agent.server.api.service;

import com.cumulocity.microservice.agent.server.api.model.MicroserviceApiRepresentation;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Suppliers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_COLLECTION;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static com.cumulocity.rest.representation.application.ApplicationUserRepresentation.applicationUserRepresentation;
import static com.cumulocity.rest.representation.user.UserMediaType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SelfRegistrationTest {

    private String baseUrl = "http://baseUrl";

    private final MicroserviceApiRepresentation api = MicroserviceApiRepresentation.of(Suppliers.ofInstance(baseUrl));

    @Mock
    public RestOperations rest;


    public SelfRegistration registration = new SelfRegistration();


    @Before
    public void setup() {
        when(rest.post(eq(api.getCollectionUrl()), eq(APPLICATION), any(ApplicationRepresentation.class))).then(new Answer<ApplicationRepresentation>() {
            private int id = 1;

            @Override
            public ApplicationRepresentation answer(InvocationOnMock invocationOnMock) throws Throwable {
                ApplicationRepresentation app = invocationOnMock.getArgumentAt(2, ApplicationRepresentation.class);

                return ApplicationRepresentation.applicationRepresentation()
                        .name(app.getName())
                        .id(String.valueOf(id++))
                        .type(app.getType())
                        .key(app.getKey())
                        .build();
            }
        });
    }

    @Test
    public void shouldRegisterApplicationWhenThereIsNoExisting() {
        givenPlatformIsUp();
        givenApplicationForName("notExistingApp");


        registration.register(baseUrl, rest, "notExistingApp");


        verify(rest).post(api.getCollectionUrl(), APPLICATION, applicationRepresentation().name("notExistingApp").type("MICROSERVICE").key("notExistingApp-application-key").build());
    }


    @Test
    public void shouldNotRegisterApplicationWhenThereIsExisting() {
        givenPlatformIsUp();
        givenApplicationForName("existingApp", applicationRepresentation().id("5").name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());


        registration.register(baseUrl, rest, "existingApp");


        verify(rest, never()).post(api.getCollectionUrl(), APPLICATION, applicationRepresentation().name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());
    }

    @Test
    public void shouldReturnBootstrapUserCredentialsForNotExisting() {
        givenPlatformIsUp();
        givenApplicationForName("notExistingApp");
        givenBootstrapUserForApp("1");


        ApplicationUserRepresentation userRepresentation = registration.register(baseUrl, rest, "notExistingApp");

        assertThat(userRepresentation).isNotNull();


    }

    private void givenBootstrapUserForApp(String id) {
        when(rest.get(api.getBootstrapUserUrl(id), USER, ApplicationUserRepresentation.class))
                .thenReturn(applicationUserRepresentation()
                        .tenant("any-tenant")
                        .name("someuser")
                        .password("pass")
                        .build());
    }


    @Test
    public void shouldReturnApplicationUserForExistingApplication() {
        givenPlatformIsUp();
        givenApplicationForName("existingApp", applicationRepresentation().id("5").name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());
        givenBootstrapUserForApp("5");


        ApplicationUserRepresentation userRepresentation = registration.register(baseUrl, rest, "existingApp");

        assertThat(userRepresentation).isNotNull();
    }

    private void givenApplicationForName(String notExistingApp, ApplicationRepresentation... applications) {

        ApplicationCollectionRepresentation collection = new ApplicationCollectionRepresentation();
        collection.setApplications(Arrays.asList(applications));
        when(rest.get(api.getFindByNameUrl(notExistingApp), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class)).thenReturn(collection);
    }

    private void givenPlatformIsUp() {
        when(rest.get(api.getCollectionUrl(), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class)).thenReturn(new ApplicationCollectionRepresentation());
    }
}
