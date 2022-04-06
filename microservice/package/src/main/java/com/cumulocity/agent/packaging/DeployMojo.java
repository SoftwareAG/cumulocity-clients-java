package com.cumulocity.agent.packaging;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.maven.plugins.annotations.LifecyclePhase.DEPLOY;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;

@Mojo(name = "push", defaultPhase = DEPLOY, requiresDependencyResolution = RUNTIME, threadSafe = true)
@Slf4j
public class DeployMojo extends BaseMicroserviceMojo {

    @Component
    private MicroserviceDockerClient dockerClient;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip || containerSkip) {
            getLog().info("skipping agent deploy");
            return;
        }
        if (isNullOrEmpty(registry)) {
            getLog().warn("docker registry is undefined skipping push");
            return;
        }

        final ImmutableList<String> tags = ImmutableList.of("latest", project.getVersion());
        for (String tag : tags) {
            publish(tag);
        }
        final DockerImage image = DockerImage.ofName(name);

        cleanup(image);
        cleanup(image.withRegistry(registry));

    }

    private void publish(String tag) throws MojoExecutionException {
        log.info("Publishing image to registry");
        final DockerImage source = DockerImage.ofName(name).withTag(tag);
        final DockerImage pushed = source.withRegistry(registry);

        //@formatter:off

        dockerClient.tagImage(source.toString(), pushed.toString(),tag);
        dockerClient.pushImage(pushed.toString());


    }

    public void cleanup(DockerImage image) throws MojoExecutionException {
        log.info("Running cleanup on {} with tags", image.toString());
        dockerClient.deleteAll(image.toString(), true);
    }


}
