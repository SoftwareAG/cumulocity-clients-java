package com.cumulocity.agent.packaging.uploadMojo;

import com.cumulocity.agent.packaging.uploadMojo.configuration.ApplicationConfiguration;
import com.cumulocity.agent.packaging.uploadMojo.configuration.ApplicationConfigurationSupplier;
import com.cumulocity.agent.packaging.uploadMojo.configuration.CredentialsConfiguration;
import com.cumulocity.agent.packaging.uploadMojo.configuration.CredentialsConfigurationSupplier;
import com.cumulocity.agent.packaging.uploadMojo.platform.PlatformRepository;
import com.cumulocity.agent.packaging.uploadMojo.platform.client.impl.ApacheHttpClientExecutor;
import com.cumulocity.agent.packaging.uploadMojo.platform.model.Application;
import com.cumulocity.agent.packaging.uploadMojo.platform.model.ApplicationWithSubscriptions;
import com.cumulocity.agent.packaging.uploadMojo.platform.model.Tenant;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import java.io.File;
import java.util.Set;

import static com.cumulocity.agent.packaging.PackageMojo.TARGET_FILENAME_PATTERN;
import static java.lang.String.format;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Mojo(name = "upload", defaultPhase = PACKAGE)
public class MicroserviceUploadMojo extends AbstractMojo {

    @Parameter(defaultValue = "microservice")
    private String serviceId ;

    @Parameter(property = "microservice-upload.skip", defaultValue = "true")
    protected boolean skip;

    @Parameter(name = "credentials", property = "microservice-upload.credentials")
    private CredentialsConfiguration credentials;

    @Parameter(name = "application", property = "microservice-upload.application")
    private ApplicationConfiguration application;

    @Getter
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Getter
    @Parameter(property = "package.name", defaultValue = "${project.artifactId}", readonly = true)
    private String packageName;

    @Parameter(defaultValue = "true", property = "skip.microservice.upload")
    protected boolean skipMicroserviceUpload;

    @Component
    private Settings settings;

    @Getter(lazy = true)
    private final ApplicationConfigurationSupplier applicationSupplier = new ApplicationConfigurationSupplier(serviceId,  settings, application, packageName, project);

    @Getter(lazy = true)
    private final CredentialsConfigurationSupplier credentialsSupplier = new CredentialsConfigurationSupplier(serviceId, settings, credentials);

    @Getter(lazy = true)
    private final PlatformRepository repository = new PlatformRepository(new ApacheHttpClientExecutor(getCredentialsSupplier().getObject().get(), getLog()), getLog());

    @Override
    public void execute() {
        try {
            if (skipMicroserviceUpload) {
                getLog().info("Skipping");
                return;
            }

            getLog().info("credentials configuration " + getCredentialsSupplier().getObject());
            getLog().info("application configuration " + getApplicationSupplier().getObject());

            if (!getApplicationSupplier().getObject().isPresent()) {
                getLog().info("Skipping");
                return;
            }

            final PlatformRepository repository = getRepository();
            final ApplicationConfiguration configuration = getApplicationSupplier().getObject().get();
            final Optional<ApplicationWithSubscriptions> application = getOrCreateApplication(repository, configuration);
            if (application.isPresent()) {
                uploadAndSubscribe(repository, application.get(), configuration);
            }
        } catch (final Exception ex) {
            getLog().error(ex);
        }
    }

    private Optional<ApplicationWithSubscriptions> getOrCreateApplication(PlatformRepository repository, ApplicationConfiguration configuration) {
        final Optional<Application> applicationMaybe = repository.findApplicationByName(configuration.getName());

        if (configuration.getDelete()) {
            Set<Tenant> subscriptions = Sets.newHashSet();
            if (applicationMaybe.isPresent()) {
//                delete application
                final Application application = applicationMaybe.get();
                subscriptions = repository.getSubscriptions(application);
                for (final Tenant tenant : subscriptions) {
                    repository.unsubscribeApplication(tenant, application);
                }
                repository.deleteApplication(application);
            }
            if (configuration.getCreate()) {
//                create application
                return Optional.of(new ApplicationWithSubscriptions(repository.createApplication(configuration.getName()), subscriptions));
            }
        } else {
            if (applicationMaybe.isPresent()) {
//                return existing application
                return Optional.of(new ApplicationWithSubscriptions(applicationMaybe.get()));
            } else {
//                create application
                if (configuration.getCreate()) {
                    return Optional.of(new ApplicationWithSubscriptions(repository.createApplication(configuration.getName())));
                }
            }
        }
        return Optional.absent();
    }

    private void uploadAndSubscribe(PlatformRepository repository, ApplicationWithSubscriptions application, ApplicationConfiguration configuration) {
        uploadFile(repository, application.getApplication());

        application.addSubscriptions(repository.getTenantsByNames(configuration.getSubscriptions()));

        for (final Tenant tenant : application.getSubscriptions()) {
            repository.subscribeApplication(tenant, application.getApplication());
        }
    }

    public void uploadFile(final PlatformRepository repository, Application application) {
        final File file = new File(createFinalName());
        repository.uploadApplicationBinary(application, file, normalizeName(file));
    }

    private String normalizeName(File file) {
        final String lowerCase = file.getName().toLowerCase();
        final String noDots = StringUtils.replace(lowerCase, ".", "");
        final String noColons = StringUtils.replace(noDots, ":", "");
        final String result = StringUtils.replace(noColons, "-", "");
        if (result.length() > 10) {
            return result.substring(0, 10);
        }
        return result;
    }

    private String createFinalName() {
        return getProject().getBuild().getDirectory() + "/" + format(TARGET_FILENAME_PATTERN, getPackageName(), getProject().getVersion());
    }
}
