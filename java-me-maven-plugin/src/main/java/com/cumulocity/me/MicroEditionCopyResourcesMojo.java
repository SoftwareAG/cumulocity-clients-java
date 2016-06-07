package com.cumulocity.me;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.resources.CopyResourcesMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo( name = "copy-resources", threadSafe = true )
public class MicroEditionCopyResourcesMojo  extends CopyResourcesMojo {

    @Parameter(defaultValue = "${project.build.directory}/classes")
    private File outputDirectory;

    @Parameter
    private List<Resource> resources;

    @Override
    public void execute() throws MojoExecutionException {
        if (resources == null ||  resources.isEmpty()) {
            resources = project.getResources();
        }
        super.execute();
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
