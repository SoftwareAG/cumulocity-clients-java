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
import static com.cumulocity.agent.packaging.uploadMojo.configuration.ApplicationConfiguration.normalizeName;
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

//    todo refactor it using sisu-maven-plugin
    @Parameter(name = "credentials", property = "upload.credentials")
    private CredentialsConfiguration credentials;

//    todo refactor it using sisu-maven-plugin
    @Parameter(name = "application", property = "upload.application")
    private ApplicationConfiguration application;

    @Parameter(property = "upload.application.name")
    private String applicationName;

    @Parameter(property = "upload.url")
    private String url;

    @Parameter(property = "upload.username")
    private String username;

    @Parameter(property = "upload.password")
    private String password;

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

            if (StringUtils.isNotBlank(applicationName)) {
                application.setName(applicationName);
            }
            if (StringUtils.isNotBlank(url)) {
                credentials.setUrl(url);
            }
            if (StringUtils.isNotBlank(username)) {
                credentials.setUsername(username);
            }
            if (StringUtils.isNotBlank(password)) {
                credentials.setPassword(password);
            }

            final Optional<CredentialsConfiguration> credentialsMaybe = getCredentialsSupplier().getObject();
            final Optional<ApplicationConfiguration> applicationMaybe = getApplicationSupplier().getObject();

            getLog().info("credentials configuration " + credentialsMaybe);
            getLog().info("application configuration " + applicationMaybe);

            if (!applicationMaybe.isPresent() || !credentialsMaybe.isPresent()) {
                getLog().info("Skipping");
                return;
            }

            final File file = targetFile();
            if (!file.exists()) {
                getLog().info("Skipping");
                return;
            }

            final PlatformRepository repository = getRepository();
            final ApplicationConfiguration configuration = applicationMaybe.get();
            final Optional<ApplicationWithSubscriptions> application = getOrCreateApplication(repository, configuration);
            if (application.isPresent()) {
                uploadAndSubscribe(repository, application.get(), configuration, file);
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

    private void uploadAndSubscribe(PlatformRepository repository, ApplicationWithSubscriptions application, ApplicationConfiguration configuration, File file) {
        uploadFile(repository, application.getApplication(), file);

        application.addSubscriptions(repository.getTenantsByNames(configuration.getSubscriptions()));

        for (final Tenant tenant : application.getSubscriptions()) {
            repository.subscribeApplication(tenant, application.getApplication());
        }
    }

    public void uploadFile(final PlatformRepository repository, Application application, final File file) {
        repository.uploadApplicationBinary(application, file, normalizeName(file.getName()));
    }

    private File targetFile() {
        return new File(getProject().getBuild().getDirectory(), format(TARGET_FILENAME_PATTERN, packageName, project.getVersion()));
    }
}
