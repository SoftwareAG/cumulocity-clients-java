package com.cumulocity.agent.packaging.microservice;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import java.io.File;
import java.io.IOException;

public interface MicroserviceDockerClient {
    void saveDockerImage(final String image, final File targetFile) throws DockerCertificateException, InterruptedException, DockerException, IOException;
}
