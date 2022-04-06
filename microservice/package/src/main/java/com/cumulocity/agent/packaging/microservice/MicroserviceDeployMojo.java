package com.cumulocity.agent.packaging.microservice;

import com.google.common.base.Function;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.repository.Repository;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.util.Optional;

import static com.cumulocity.agent.packaging.BaseMicroserviceMojo.TARGET_FILENAME_PATTERN_DEFAULT_ARCH;
import static java.lang.String.format;
import static org.apache.maven.plugins.annotations.LifecyclePhase.DEPLOY;

@Mojo(name = "microservice-deploy", defaultPhase = DEPLOY)
public class MicroserviceDeployMojo extends AbstractMojo {

    private static final String SERVER_ID = "microservice";

    @Parameter(defaultValue = "${project.build.directory}")
    private File build;
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "true", property = "skip.microservice.deploy")
    private boolean skip;

    @Parameter(defaultValue = SERVER_ID)
    private String serviceId ;
    @Parameter(property = "package.name", defaultValue = "${project.artifactId}")
    private String name;

    @Component
    private WagonManager wagonManager;
    @Component
    private Settings settings;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skipping microservice package deployment");
            return;
        }
        final Optional<String> serverUrl = getServerUrl();
        if (!serverUrl.isPresent()) {
            getLog().warn(format("URL parameter was not configured, microservice package cannot be deployed. please check your '%s' server configuration in settings.xml", SERVER_ID));
            return;
        }

        final String targetFilename = format(TARGET_FILENAME_PATTERN_DEFAULT_ARCH, name, project.getVersion());
        final File file = new File(build, targetFilename);
        try {
            final Repository repository = new Repository(serviceId, serverUrl.get());
            final Wagon wagon = wagonManager.getWagon(repository);
            wagon.connect(repository, wagonManager.getAuthenticationInfo(serviceId));
            wagon.put(file, targetFilename);
            wagon.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<String> getServerUrl() {
        final Server server = settings.getServer(serviceId);
        if(server == null){
            return Optional.empty();
        }
        final Object configuration = server.getConfiguration();
        if (!(configuration instanceof Xpp3Dom)) {
            return Optional.empty();
        }
        final Xpp3Dom urlDom = ((Xpp3Dom) configuration).getChild("url");
        return Optional.ofNullable(urlDom).map((Function<Xpp3Dom, String>) Xpp3Dom::getValue);
    }

}
