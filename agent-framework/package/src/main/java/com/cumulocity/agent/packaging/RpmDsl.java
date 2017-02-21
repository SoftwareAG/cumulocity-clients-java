package com.cumulocity.agent.packaging;

import static java.util.Calendar.YEAR;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;
import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.util.Calendar;

import org.apache.maven.model.Plugin;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import com.google.common.collect.ObjectArrays;

public final class RpmDsl {
    public static Plugin rpm() {
        return plugin(groupId("org.codehaus.mojo"), artifactId("rpm-maven-plugin"), version("2.1.5"));
    }
    public static String currentYear() {
        return String.valueOf(Calendar.getInstance().get(YEAR));
    }

    public static MojoExecutor.Element sources(MojoExecutor.Element... elements) {
        return element(name("sources"), elements);
    }

    public static MojoExecutor.Element includes(MojoExecutor.Element... elements) {
        return element(name("includes"), elements);
    }

    public static MojoExecutor.Element include(String value) {
        return element(name("include"), value);
    }

    public static MojoExecutor.Element source(MojoExecutor.Element... elements) {
        return element(name("source"), elements);
    }

    public static MojoExecutor.Element location(String applicationDirectory) {
        return element(name("location"), applicationDirectory);
    }

    public static MojoExecutor.Element directory(String directory) {
        return element(name("directory"), directory);
    }

    public static MojoExecutor.Element directoryIncluded(boolean enabled) {
        return element(name("directoryIncluded"), String.valueOf(enabled));
    }

    public static MojoExecutor.Element isConfiguration(boolean enabled) {
        return element(name("configuration"), String.valueOf(enabled));
    }

    public static MojoExecutor.Element mappings(MojoExecutor.Element... elements) {
        return element(name("mappings"), elements);
    }

    public static MojoExecutor.Element mapping(MojoExecutor.Element... elements) {
        return element(name("mapping"),
            ObjectArrays.concat(elements, new MojoExecutor.Element[] { element(name("username"), "root"), element(name("filemode"), "777") },
                MojoExecutor.Element.class));
    }
}
