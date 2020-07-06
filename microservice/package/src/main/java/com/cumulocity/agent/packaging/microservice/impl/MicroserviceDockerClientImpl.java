package com.cumulocity.agent.packaging.microservice.impl;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.RemovedImage;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.StartingException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.StoppingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.spotify.docker.client.DockerClient.ListImagesParam.byName;

@Component(role = MicroserviceDockerClient.class)
public class MicroserviceDockerClientImpl extends AbstractLogEnabled implements MicroserviceDockerClient, Startable {
    private DockerClient docker;

    public void saveDockerImage(final String image, final File targetFile) throws InterruptedException, DockerException, IOException {
        try (
                final InputStream inputStream = docker.save(image);
                final FileOutputStream outputStream = new FileOutputStream(targetFile)
        ) {
            IOUtils.copyLarge(inputStream, outputStream);
        }
    }

    private DefaultDockerClient newDockerClient() throws DockerCertificateException {
        return DefaultDockerClient.fromEnv().build();
    }

    @Override
    public void deleteAll(String imageName) throws DockerException, InterruptedException {
        getLogger().info("Cleaning up all docker images for " + imageName);
        List<Image> images = docker.listImages(byName(imageName));
        for (Image image : images) {
            getLogger().info("Removing " + image.id() + " " + image.repoTags());
            for (String tag : image.repoTags()) {
                getLogger().info("Removing tag " + image.id() + " " + tag);
                try {
                    for (RemovedImage removed : docker.removeImage(tag, false, true)) {
                        getLogger().info("Removed " + removed.imageId() + " " + removed.type());
                    }
                } catch (Exception ex) {
                    getLogger().warn("Failed to remove tag : " + tag, ex);
                }
            }

        }
    }

    @Override
    public void start() throws StartingException {
        getLogger().debug("Starting docker client ");
        if (docker == null) {
            try {
                docker = newDockerClient();
            } catch (DockerCertificateException e) {
                throw new StartingException("Can't start docker client" + e.getMessage(), e);
            }
        }
    }

    @Override
    public void stop() throws StoppingException {
        getLogger().debug("Stopping docker client ");
        if (docker != null) {
            docker.close();
        }
    }
}
