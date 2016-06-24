package com.cumulocity.me.common;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.antrun.AntRunMojo;
import org.codehaus.plexus.configuration.DefaultPlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

public abstract class MicroEditionAntRunMojo extends AntRunMojo {

    private PlexusConfiguration ant;

    @Override
    public final void execute() throws MojoExecutionException {
        copyFile("ant/build.xml");
        copyFile("ant/build-impl.xml");
        copyFile("ant/lib/org-netbeans-modules-classfile.jar");
        copyFile("ant/lib/org-netbeans-modules-j2me-common-ant.jar");
        copyFile("ant/lib/org-netbeans-modules-mobility-antext.jar");
        beforeExecute();
        super.execute();
    }

    public abstract void beforeExecute();

    private void copyFile(final String fileName) {
        try {
            final File buildXml = getBuildXml(fileName);
            if (!buildXml.exists()) {
                buildXml.getParentFile().mkdirs();
                buildXml.createNewFile();
                try (final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
                    try (final FileOutputStream output = new FileOutputStream(buildXml)) {
                        IOUtils.copy(resourceAsStream, output);
                    }
                }
            }
        } catch (final Exception e) {
            getLog().error(e);
        }
    }

    public void addTarget(String targetName) {
        DefaultPlexusConfiguration tasks = getTasks();
        if (tasks == null) {
            ant = new DefaultPlexusConfiguration("ant");
            ant.setAttribute("antfile", getBuildXml("ant/build.xml").getAbsolutePath());
            tasks = new DefaultPlexusConfiguration("tasks");
            tasks.addChild(ant);
            setTasks(tasks);
        }

        final DefaultPlexusConfiguration target = new DefaultPlexusConfiguration("target");
        target.setAttribute("name", targetName);
        ant.addChild(target);
    }

    public File getBuildXml(String fileName) {
        return new File(getMavenProject().getBuild().getDirectory(), fileName);
    }

    public DefaultPlexusConfiguration getTasks() {
        try {
            final Field targetField = AntRunMojo.class.getDeclaredField("tasks");
            targetField.setAccessible(true);
            return (DefaultPlexusConfiguration) targetField.get(this);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void setTasks(PlexusConfiguration tasks) {
        try {
            final Field targetField = AntRunMojo.class.getDeclaredField("tasks");
            targetField.setAccessible(true);
            targetField.set(this, tasks);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
