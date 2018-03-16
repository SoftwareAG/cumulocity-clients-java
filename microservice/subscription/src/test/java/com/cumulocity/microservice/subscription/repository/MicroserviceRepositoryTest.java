package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.repository.application.ApplicationApiRepresentation;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import com.google.common.collect.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;

import static com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static lombok.AccessLevel.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.http.HttpMethod.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceRepositoryTest {


    public static final String BASE_URL = "http://c8y.com";
    private MicroserviceRepository repository;


    @Builder
    @Value
    public static class Request {
        private final String path;
        private final HttpMethod method;
        private final MediaType mediaType;
        private final Object body;
    }

    FakeCredentialsSwitchingPlatform platform = new FakeCredentialsSwitchingPlatform();


    ApplicationApiRepresentation api = ApplicationApiRepresentation.of(Suppliers.ofInstance(BASE_URL));

    @Before
    public void setup() {
        givenSelfRegistrationEnabled(true);
    }

    private MicroserviceRepository givenSelfRegistrationEnabled(boolean register) {
        return repository = MicroserviceRepository.microserviceApi()
                .baseUrl(Suppliers.ofInstance(BASE_URL))
                .connector(platform)
                .register(register)
                .build();
    }

    @Test
    public void shouldFailWhenNoCurrentApplication() {
        givenSelfRegistrationEnabled(false);

        Throwable exception = catchThrowable(new ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                repository.register("cep", microserviceMetadataRepresentation().build());
            }
        });

        assertThat(exception)
                .isInstanceOf(SDKException.class)
                .hasMessageContaining("No microservice with name cep registered.");


    }

    @Test
    public void shouldNotUpdateApplicationWhenNotRequired() {
        givenSelfRegistrationEnabled(false);

        ApplicationRepresentation existing = applicationRepresentation()
                .type(MICROSERVICE)
                .name("cep")
                .build();
        platform.addApplication(existing);
        platform.switchTo(asCredentials(platform.bootstrapUserFor(existing)));


        ApplicationRepresentation cep = repository.register("cep", microserviceMetadataRepresentation().build());

        assertThat(cep).isSameAs(existing);
        assertThat(platform.take(byMethod(PUT))).isEmpty();
        assertThat(platform.take(byMethod(POST))).isEmpty();
    }


    @Test
    public void shouldUpdateApplicationWhenAlreadyExistsAndRegistrationIsEnabled() {

        ApplicationRepresentation existing = applicationRepresentation()
                .id("3")
                .type(MICROSERVICE)
                .name("cep")
                .build();
        givenApplications(existing);


        //  when(restOperations.get("http://c8y.com/application/currentApplication", APPLICATION, ApplicationRepresentation.class)).thenReturn(existing);
        //  when(restOperations.put(eq("http://c8y.com/application/currentApplication"), eq(APPLICATION), any(ApplicationRepresentation.class))).thenReturn(existing);

        ApplicationRepresentation cep = repository.register("cep", microserviceMetadataRepresentation()
                .role("TEST").requiredRole("TEST").build());

        assertThat(cep).isEqualTo(existing);

        assertThat(platform.take(byMethod(PUT)))
                .hasSize(1)
                .extracting("body")
                .have(new Condition<Object>() {
                    @Override
                    public boolean matches(Object value) {
                        if (value instanceof ApplicationRepresentation) {
                            assertThat(((ApplicationRepresentation) value).getRequiredRoles()).contains("TEST");
                            assertThat(((ApplicationRepresentation) value).getRoles()).contains("TEST");
                            return true;
                        }
                        return false;
                    }
                });
        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldRegisterApplicationWhenThereIsNoExisting() {
        givenApplications();
        repository.register("notExistingApp", microserviceMetadataRepresentation().build());

        Collection<Request> posts = platform.take(byMethod(POST));
        assertThat(posts).isNotEmpty();
        assertThat(posts)
                .hasSize(1)
                .extracting("body")
                .containsExactly(applicationRepresentation()
                        .id("0")
                        .name("notExistingApp")
                        .type("MICROSERVICE")
                        .key("notExistingApp-application-key")
                        .requiredRoles(ImmutableList.<String>of())
                        .roles(ImmutableList.<String>of())
                        .build());
    }

    private Predicate<Request> byMethod(final HttpMethod method) {
        return new Predicate<Request>() {
            @Override
            public boolean apply(Request request) {
                return request.getMethod().equals(method);
            }
        };
    }


    @Test
    public void shouldNotRegisterApplicationWhenThereIsExisting() {
        givenApplications(applicationRepresentation().id("5").name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());


        repository.register("existingApp", microserviceMetadataRepresentation().build());


        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldNotFailOnMultipleCalls() {
        givenApplications();

        assertThat(repository.register("existingApp", microserviceMetadataRepresentation().build())).isNotNull();
        assertThat(platform.take(byMethod(POST))).isNotEmpty();

        for (int i = 0; i < 10; ++i) {
            assertThat(repository.register("existingApp", microserviceMetadataRepresentation().build())).isNotNull();
            assertThat(platform.take(byMethod(POST))).isEmpty();
        }
    }

    @Test
    public void shouldSwitchCredentialsForNotExisting() {
        givenApplications();


        repository.register("notExistingApp", microserviceMetadataRepresentation().build());


        assertThat(platform.getCredentials().getUsername()).isEqualTo("service_notExistingApp");

    }


    @Test
    public void shouldSwitchBootstrapForExistingApplication() {
        givenApplications(applicationRepresentation()
                .id("5")
                .name("existingApp")
                .type("MICROSERVICE")
                .key("existingApp-application-key")
                .build());


        repository.register("existingApp", microserviceMetadataRepresentation().build());

        assertThat(platform.getCredentials().getUsername()).isEqualTo("service_existingApp");
    }

    private void givenApplications(ApplicationRepresentation... applications) {

        ApplicationCollectionRepresentation collection = new ApplicationCollectionRepresentation();
        collection.setApplications(Arrays.asList(applications));
        for (ApplicationRepresentation app : applications) {
            platform.addApplication(app);
        }
    }


    private CumulocityCredentials asCredentials(ApplicationUserRepresentation user) {
        return new CumulocityCredentials.Builder(user.getName(), user.getPassword())
                .withTenantId(user.getTenant())
                .build();
    }

    @Getter
    @Slf4j
    private static class FakeCredentialsSwitchingPlatform implements CredentialsSwitchingPlatform {
        private CumulocityCredentials credentials = new CumulocityCredentials.Builder("servicebootstrap", "").withTenantId("management").build();
        private final Queue<Request> requests = Queues.newArrayDeque();

        private final Map<String, ApplicationRepresentation> applications = Maps.newHashMap();
        @Getter(NONE)
        private long generateId = 0;


        public Request takeRequest() {
            return requests.poll();
        }

        public Collection<Request> take(Predicate<Request> by) {
            Collection<Request> filtered = FluentIterable.from(this.requests)
                    .filter(by)
                    .toList();

            requests.removeAll(filtered);

            return filtered;
        }


        @Override
        public RestOperations get() {
            return new RestOperations() {

                @Override
                public <T extends ResourceRepresentation> T get(String path, CumulocityMediaType mediaType, Class<T> responseType) throws SDKException {
                    Request build = Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(GET)
                            .build();
                    pushRequest(build);
                    checkAccessTo(path);
                    if (responseType.equals(ApplicationRepresentation.class)) {
                        String id = Iterables.getLast(Splitter.on('/').split(path));
                        return getById(id);
                    } else if (responseType.equals(ApplicationCollectionRepresentation.class)) {
                        return getByName(path);

                    } else if (responseType.equals(ApplicationUserRepresentation.class)) {
                        String id = Splitter.on('/').splitToList(path).get(5);
                        if (applications.containsKey(id)) {
                            ApplicationRepresentation app = applications.get(id);
                            return (T) bootstrapUserFor(app);
                        }

                    }
                    throw new SDKException(404, "Not found");
                }

                private <T extends ResourceRepresentation> T getById(String id) {
                    if (id.equals("currentApplication") && credentials.getUsername().startsWith("service_")) {
                        ApplicationCollectionRepresentation byName = getByName(Splitter.on('_').splitToList(credentials.getUsername()).get(1));
                        return (T) Iterables.getFirst(byName.getApplications(), null);
                    }
                    return (T) applications.get(id);
                }

                private <T extends ResourceRepresentation> T getByName(String path) {
                    final String name = Iterables.getLast(Splitter.on('/').split(path));

                    ApplicationCollectionRepresentation byName = new ApplicationCollectionRepresentation();

                    FluentIterable<ApplicationRepresentation> filtered = FluentIterable.from(applications.values()).filter(new Predicate<ApplicationRepresentation>() {
                        @Override
                        public boolean apply(ApplicationRepresentation app) {
                            return app.getName().equals(name);

                        }
                    });
                    byName.setApplications(filtered.toList());
                    return (T) byName;
                }

                private boolean pushRequest(Request request) {
                    log.debug("Request {}", request);
                    return requests.add(request);
                }

                private void checkAccessTo(String path) {
                    if (path.contains("application/currentApplication") && credentials.getUsername().equals("servicebootstrap")) {
                        throw new SDKException(403, "Unauthorized");
                    }
                    if (!path.contains("application/currentApplication") && !credentials.getUsername().equals("servicebootstrap")) {
                        throw new SDKException(403, "Unauthorized");
                    }
                }

                @Override
                public <T> T get(String path, MediaType mediaType, Class<T> responseType) throws SDKException {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(GET)
                            .build());
                    if (responseType.equals(ApplicationRepresentation.class)) {
                        String id = Iterables.getLast(Splitter.on('/').split(path));
                        return (T) applications.get(id);
                    }
                    return null;
                }

                @Override
                public Response.Status getStatus(String path, CumulocityMediaType mediaType) throws SDKException {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T postStream(String path, CumulocityMediaType mediaType, InputStream content, Class<T> responseClass) throws SDKException {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T postText(String path, String content, Class<T> responseClass) {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T putText(String path, String content, Class<T> responseClass) {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T putStream(String path, String contentType, InputStream content, Class<T> responseClass) {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T putStream(String path, MediaType mediaType, InputStream content, Class<T> responseClass) {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T postFile(String path, T representation, byte[] bytes, Class<T> responseClass) {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentationWithId> T put(String path, MediaType mediaType, T representation) throws SDKException {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(PUT)
                            .body(representation)
                            .build());
                    checkAccessTo(path);
                    if (mediaType.equals(APPLICATION)) {
                        String id = Iterables.getLast(Splitter.on('/').split(path));
                        ApplicationRepresentation app = getById(id);
                        merge((ApplicationRepresentation) representation, app);

                        return (T) app;
                    }
                    return null;
                }

                private void merge(ApplicationRepresentation source, ApplicationRepresentation dest) {
                    if (source.getRoles() != null) {
                        dest.setRoles(source.getRoles());
                    }
                    if (source.getRequiredRoles() != null) {
                        dest.setRequiredRoles(source.getRequiredRoles());
                    }

                }

                @Override
                public <T extends ResourceRepresentation> Future postAsync(String path, CumulocityMediaType mediaType, T representation) throws SDKException {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> Future putAsync(String path, CumulocityMediaType mediaType, T representation) throws SDKException {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <T extends ResourceRepresentation> T post(String path, MediaType mediaType, T representation) throws SDKException {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(POST)
                            .body(representation)
                            .build());
                    checkAccessTo(path);
                    if (mediaType.equals(APPLICATION)) {
                        String id = String.valueOf(generateId++);

                        applications.put(id, (ApplicationRepresentation) representation);
                        ((ApplicationRepresentation) representation).setId(id);

                        return representation;
                    }
                    return null;
                }

                @Override
                public <T extends ResourceRepresentationWithId> T post(String path, MediaType mediaType, T representation) throws SDKException {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(POST)
                            .body(representation)
                            .build());
                    checkAccessTo(path);
                    if (mediaType.equals(APPLICATION)) {
                        String id = String.valueOf(generateId++);

                        applications.put(id, (ApplicationRepresentation) representation);
                        ((ApplicationRepresentation) representation).setId(id);

                        return representation;
                    }
                    return null;
                }

                @Override
                public <T extends ResourceRepresentation> void postWithoutResponse(String path, MediaType mediaType, T representation) throws SDKException {
                    throw new UnsupportedOperationException("unsuported");
                }

                @Override
                public <Result extends ResourceRepresentation, Param extends ResourceRepresentation> Result post(String path, CumulocityMediaType contentType, CumulocityMediaType accept, Param representation, Class<Result> clazz) {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(contentType)
                            .method(POST)
                            .body(representation)
                            .build());
                    checkAccessTo(path);
                    if (contentType.equals(APPLICATION)) {
                        String id = String.valueOf(generateId++);

                        applications.put(id, (ApplicationRepresentation) representation);
                        ((ApplicationRepresentation) representation).setId(id);

                        return (Result) representation;
                    }
                    return null;
                }

                @Override
                public <T extends ResourceRepresentation> T put(String path, MediaType mediaType, T representation) throws SDKException {
                    pushRequest(Request.builder()
                            .path(path)
                            .mediaType(mediaType)
                            .method(PUT)
                            .body(representation)
                            .build());
                    checkAccessTo(path);
                    if (mediaType.equals(APPLICATION)) {
                        String id = Iterables.getLast(Splitter.on('/').split(path));
                        ApplicationRepresentation app = getById(id);
                        merge((ApplicationRepresentation) representation, app);

                        return (T) app;
                    }
                    return null;
                }

                @Override
                public void delete(String path) throws SDKException {

                }
            };
        }

        public ApplicationUserRepresentation bootstrapUserFor(ApplicationRepresentation app) {
            return ApplicationUserRepresentation.applicationUserRepresentation()
                    .tenant("any")
                    .name("service_" + app.getName())
                    .password("anypass")
                    .build();
        }

        @Override
        public CredentialsSwitchingPlatform switchTo(CumulocityCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public void addApplication(ApplicationRepresentation app) {
            applications.put(app.getId(), app);
        }
    }
}
