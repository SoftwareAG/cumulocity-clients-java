package com.cumulocity.agent.packaging.microservice;

import com.google.common.base.Optional;
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

import static com.cumulocity.agent.packaging.microservice.MicroservicePackageMojo.TARGET_FILENAME_PATTERN;
import static com.google.common.base.Throwables.propagate;
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

        final String targetFilename = format(TARGET_FILENAME_PATTERN, project.getArtifactId(), project.getVersion());
        final File file = new File(build, targetFilename);
        try {
            final Repository repository = new Repository(SERVER_ID, serverUrl.get());
            final Wagon wagon = wagonManager.getWagon(repository);
            wagon.connect(repository, wagonManager.getAuthenticationInfo(SERVER_ID));
            wagon.put(file, targetFilename);
            wagon.disconnect();
        } catch (Exception e) {
            propagate(e);
        }
    }

    private Optional<String> getServerUrl() {
        final Server server = settings.getServer(SERVER_ID);
        final Object configuration = server.getConfiguration();
        if (!(configuration instanceof Xpp3Dom)) {
            return Optional.absent();
        }
        final Xpp3Dom urlDom = ((Xpp3Dom) configuration).getChild("url");
        if (urlDom == null) {
            return Optional.absent();
        }
        return Optional.of(urlDom.getValue());
    }

}
