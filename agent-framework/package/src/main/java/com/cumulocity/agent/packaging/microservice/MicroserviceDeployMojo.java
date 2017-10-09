package com.cumulocity.agent.packaging.microservice;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.cumulocity.agent.packaging.microservice.MicroservicePackageMojo.TARGET_FILENAME_PATTERN;
import static com.google.common.base.Throwables.propagate;
import static org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM;
import static org.apache.maven.plugins.annotations.LifecyclePhase.DEPLOY;

@Mojo(name = "microservice-deploy", defaultPhase = DEPLOY)
public class MicroserviceDeployMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}")
    private File build;
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "true", property = "skip.microservice.deploy")
    private boolean skip;
    @Parameter
    private String authorizationHeader;
    @Parameter
    private String deployUrl;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skipping microservice package deployment");
            return;
        }
        if (StringUtils.isBlank(deployUrl)) {
            getLog().warn("URL parameter was not configured, microservice package cannot be deployed");
            return;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final String targetFilename = String.format(TARGET_FILENAME_PATTERN, project.getArtifactId(), project.getVersion());
            final File file = new File(build, targetFilename);
            final RequestBuilder requestBuilder = RequestBuilder.post(deployUrl)
                    .setEntity(MultipartEntityBuilder.create()
                            .addBinaryBody("file", new FileInputStream(file), APPLICATION_OCTET_STREAM, file.getName())
                            .build());
            if (StringUtils.isNotBlank(authorizationHeader)) {
                requestBuilder.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }

            CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode >= 300) {
                throw new MojoExecutionException(String.format("Microservice package was not deployed properly, response details: %s", response));
            }
        } catch (IOException e) {
            propagate(e);
        }
    }

}
