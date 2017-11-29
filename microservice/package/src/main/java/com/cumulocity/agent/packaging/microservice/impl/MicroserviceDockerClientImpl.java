package com.cumulocity.agent.packaging.microservice.impl;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component(role = MicroserviceDockerClient.class)
public class MicroserviceDockerClientImpl implements MicroserviceDockerClient {

    public void saveDockerImage(final String image, final File targetFile) throws DockerCertificateException, InterruptedException, DockerException, IOException {
        final DockerClient docker = DefaultDockerClient.fromEnv().build();
        final InputStream inputStream = docker.save(image);
        final FileOutputStream outputStream = new FileOutputStream(targetFile);

        IOUtils.copyLarge(inputStream, outputStream);

        outputStream.close();
        inputStream.close();
        docker.close();
    }

}
