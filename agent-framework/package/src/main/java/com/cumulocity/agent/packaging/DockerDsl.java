package com.cumulocity.agent.packaging;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import org.apache.maven.model.Plugin;

public final class DockerDsl {
    public static Plugin docker() {
        return plugin(groupId("com.spotify"), artifactId("docker-maven-plugin"), version("0.4.13"));
    }
}
