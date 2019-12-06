package com.cumulocity.agent.packaging;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import org.apache.maven.model.Plugin;

public final class DockerDsl {
    public static Plugin docker() {
        // FIXME: upgrade to the newest to make it compatible with Java 11
        return plugin(groupId("com.spotify"), artifactId("docker-maven-plugin"), version("1.2.1"));
    }
}
