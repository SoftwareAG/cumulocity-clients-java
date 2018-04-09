package com.cumulocity.agent.packaging.upload;

import com.cumulocity.agent.packaging.platform.PlatformRepository;
import com.cumulocity.agent.packaging.platform.client.impl.ApacheHttpClientExecutor;
import com.cumulocity.agent.packaging.platform.model.Application;
import com.cumulocity.agent.packaging.platform.model.ApplicationWithSubscriptions;
import com.cumulocity.agent.packaging.platform.model.CredentialsConfiguration;
import com.cumulocity.agent.packaging.platform.model.Tenant;
import com.cumulocity.agent.packaging.upload.configuration.ApplicationConfiguration;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Set;

import static com.cumulocity.agent.packaging.microservice.MicroservicePackageMojo.TARGET_FILENAME_PATTERN;
import static java.lang.String.format;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

// todo completely not tested with command line parameters
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Mojo(name = "microservice-upload", defaultPhase = PACKAGE)
public class MicroserviceUploadMojo extends AbstractMojo {

    @Parameter(property = "microservice-upload.skip", defaultValue = "true")
    protected boolean skip;

    //    todo should be possible to upload to more than one place at the same time?
    @Parameter(name = "credentials", property = "microservice-upload.credentials")
    private CredentialsConfiguration credentials;

    @Parameter(name = "application", property = "microservice-upload.application")
    private ApplicationConfiguration application;

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter(property = "package.name", defaultValue = "${project.artifactId}")
    private String packageName;

    @Getter(lazy = true)
    private final PlatformRepository repository = new PlatformRepository(new ApacheHttpClientExecutor(credentials, getLog()), getLog());

    @Override
    public void execute() {
        try {
//            todo validate configuration and provide defaults in reasonable places
            setConfigurationDefaults();

            getLog().info("credentials configuration " + credentials);
            getLog().info("application configuration " + application);

            final PlatformRepository repository = getRepository();

            final Optional<ApplicationWithSubscriptions> application = getOrCreateApplication(repository);

            if (application.isPresent()) {
                uploadAndSubscribe(repository, application.get());
            }
        } catch (final Exception ex) {
            if (getApplication().getFailOnError()) {
                throw ex;
            }
            getLog().error(ex);
        }
    }

    private void setConfigurationDefaults() {
        if (StringUtils.isBlank(application.getName())) {
            application.setName(packageName);
        }
//        todo divide into two separate goals!!!
        if (application.getDelete() == null) {
            application.setDelete(true);
        }
        if (application.getCreate() == null) {
            application.setCreate(true);
        }
        if (application.getFailOnError() == null) {
            application.setFailOnError(true);
        }
        if (application.getSubscriptions() == null) {
            application.setSubscriptions(Lists.<String>newArrayList());
        }
    }

    private Optional<ApplicationWithSubscriptions> getOrCreateApplication(PlatformRepository repository) {
        final Optional<Application> applicationMaybe = repository.findApplicationByName(application.getName());
        if (application.getDelete()) {
            final Set<Tenant> subscriptions;
            if (applicationMaybe.isPresent()) {
                subscriptions = repository.getSubscriptions(applicationMaybe.get());
                for (final Tenant tenant : subscriptions) {
                    repository.unsubscribeApplication(tenant, applicationMaybe.get());
                }
                try {
                    repository.deleteApplication(applicationMaybe.get());
                } catch (final Exception ex) {
//                    todo api responds with 400 after successful deletion
                    getLog().error(ex.getMessage());
                }
            } else {
                subscriptions = Sets.newHashSet();
            }
            if (application.getCreate()) {
                return Optional.of(new ApplicationWithSubscriptions(repository.createApplication(application.getName()), subscriptions));
            }
        } else {
            if (applicationMaybe.isPresent()) {
                return Optional.of(new ApplicationWithSubscriptions(applicationMaybe.get()));
            } else {
                if (application.getCreate()) {
                    return Optional.of(new ApplicationWithSubscriptions(repository.createApplication(application.getName())));
                }
            }
        }
        return Optional.absent();
    }

    private void uploadAndSubscribe(PlatformRepository repository, ApplicationWithSubscriptions application) {
        uploadFile(repository, application.getApplication());

        application.addSubscriptions(repository.getTenantsByNames(this.application.getSubscriptions()));

        for (final Tenant tenant : application.getSubscriptions()) {
            repository.subscribeApplication(tenant, application.getApplication());
        }
    }

    public void uploadFile(final PlatformRepository repository, Application application) {
        final File file = new File(createFinalName());
        repository.uploadApplicationBinary(application, file, normalizeName(file));

//        final String url = baseUrl + "/application/applications/" + application.getId() + "/binaries";
//        final String user = tenant + "/" + username + ":" + replace(password, "!", "\\!");
//        final String curl = "curl --progress-bar --verbose -u " + user + " -F file=@" + finalFileName + " " + url;
//        getLog().info(curl);
    }

    private String normalizeName(File file) {
        final String lowerCase = file.getName().toLowerCase();
        final String noDots = StringUtils.replace(lowerCase, ".", "");
        final String noColonts = StringUtils.replace(noDots, ":", "");
        final String result = StringUtils.replace(noColonts, "-", "");
        return result;
    }

    private String createFinalName() {
        return project.getBuild().getDirectory() + "/" + format(TARGET_FILENAME_PATTERN, packageName, project.getVersion());
    }
}
