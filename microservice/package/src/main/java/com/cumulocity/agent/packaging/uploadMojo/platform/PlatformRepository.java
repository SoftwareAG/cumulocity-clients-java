package com.cumulocity.agent.packaging.uploadMojo.platform;

import com.cumulocity.agent.packaging.uploadMojo.platform.client.Executor;
import com.cumulocity.agent.packaging.uploadMojo.platform.client.Request;
import com.cumulocity.agent.packaging.uploadMojo.platform.model.*;
import com.google.common.base.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cumulocity.agent.packaging.uploadMojo.platform.client.Request.*;

@AllArgsConstructor
public class PlatformRepository {

    @Getter(value = AccessLevel.PRIVATE)
    private final Executor executor;

    @Getter(value = AccessLevel.PRIVATE)
    private final Log log;

    public Optional<Application> findApplicationByName(String name) {
        getLog().info("find application by name " + name);

        final Request<Applications> request = Get("application/applicationsByName/:name")
                .withUrlParam(":name", name)
                .withResponse(Applications.class);
        final Applications applications = getExecutor().execute(request);
        return applications.first();
    }

    public void uploadApplicationBinary(Application application, File content, String name) {
        getLog().info("upload application binary " + application + ", " + content.getAbsolutePath() + ", " + name);

        final Request<Map> request = Post("/application/applications/:id/binaries")
                .withUrlParam(":id", application.getId())
                .withMultipartName(name)
                .withMultipartBody(content);

        getExecutor().execute(request);
    }

    public Application createApplication(String name) {
        getLog().info("create application " + name);

        final Request<Application> request = Post("application/applications")
                .withJsonBody(new Application().withName(name).withKey(name))
                .withResponse(Application.class);
        return executor.execute(request);
    }

    public void unsubscribeApplication(Tenant tenant, Application application) {
        getLog().info("unsubscribe application " + tenant + ", " + application);

        final Request<Map> request = Delete("tenant/tenants/:tenant/applications/:application")
                .withUrlParam(":tenant", tenant.getId())
                .withUrlParam(":application", application.getId());
        executor.execute(request);
    }

    public void subscribeApplication(Tenant tenant, Application application) {
        getLog().info("subscribe application " + tenant + ", " + application);

        final Request<ApplicationReference> mapRequest = Post("tenant/tenants/:tenant/applications")
                .withUrlParam(":tenant", tenant.getId())
                .withJsonBody(new ApplicationReference().withApplication(application))
                .withResponse(ApplicationReference.class);

        executor.execute(mapRequest);
    }

    public Set<Tenant> getSubscriptions(Application application) {
        getLog().info("get subscriptions " + application);

        final Request<Tenants> request = Get("tenant/tenants?pageSize=2000")
                .withResponse(Tenants.class);
        final Tenants tenants = getExecutor().execute(request);
        return tenants.subscribed(application);
    }

    public Set<Tenant> getTenantsByNames(List<String> names) {
        getLog().info("get tenants by names " + names);

        final Request<Tenants> request = Get("tenant/tenants?pageSize=2000")
                .withResponse(Tenants.class);

        final Tenants tenants = getExecutor().execute(request);

        return tenants.byNames(names);
    }

    public void deleteApplication(Application application) {
        getLog().info("delete application " + application);

        final Request<Map> request = Delete("application/applications/:id")
                .withUrlParam(":id", application.getId());

        try {
            executor.execute(request);
        } catch (final Exception ex) {
//            todo api responds with 400 after successful deletion
            getLog().error(ex.getMessage());
        }
    }
}
