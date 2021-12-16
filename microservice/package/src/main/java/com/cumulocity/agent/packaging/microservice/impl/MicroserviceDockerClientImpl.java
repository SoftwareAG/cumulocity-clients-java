package com.cumulocity.agent.packaging.microservice.impl;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Component(role = MicroserviceDockerClient.class)
@Slf4j
public class MicroserviceDockerClientImpl extends AbstractLogEnabled implements MicroserviceDockerClient, Startable {

    DockerClient dockerClient;

    @SneakyThrows
    public void buildDockerImage(String dockerDirectory, Set<String> tags, Map<String, String> buildArgs, String networkMode) {

        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd(new File(dockerDirectory)).withTags(tags);

        for (Map.Entry<String, String> buildArgument : buildArgs.entrySet()) {
            buildImageCmd = buildImageCmd.withBuildArg(buildArgument.getKey(), buildArgument.getValue());
        }

        if (!Strings.isNullOrEmpty(networkMode)) {
            buildImageCmd = buildImageCmd.withNetworkMode(networkMode);
        }

        log.info("Building Docker image. Docker dir={},tags={}, build arguments={}", dockerDirectory, tags, buildArgs);
        ImageBuildCompletionWaiter imageBuildCompletionWaiter = new ImageBuildCompletionWaiter();
        buildImageCmd.exec(imageBuildCompletionWaiter);
        log.info("Waiting for image build to complete.");

        while ((!imageBuildCompletionWaiter.getImageBuildOperationCompleted().get())) {
            Thread.sleep(250);
        }

        if (Objects.nonNull(imageBuildCompletionWaiter.getImageBuildError())) {
            log.error("Image build failed", imageBuildCompletionWaiter.getImageBuildError());
            throw imageBuildCompletionWaiter.getImageBuildError();
        } else {
            log.info("Image build success");
        }

    }

    protected class ImageBuildCompletionWaiter extends BuildImageResultCallback {

        //As the image build is asynchronous, we use active waiting for simplicity reasons
        @Getter
        private AtomicBoolean imageBuildOperationCompleted = new AtomicBoolean(false);

        @Getter
        private Throwable imageBuildError = null;

        //this logs out the build progress
        @Override
        public void onNext(BuildResponseItem item) {
            log.info(item.getStream());
        }

        @Override
        public void onError(Throwable throwable) {
            imageBuildError=throwable;
            imageBuildOperationCompleted.set(true);
        }

        @Override
        public void onComplete() {
            imageBuildOperationCompleted.set(true);
        }
    }

    public void saveDockerImage(final String image, final File targetFile) throws IOException {


        log.info("Saving Image {} to file {}", image, targetFile.getAbsoluteFile().toString());

        try (
                SaveImageCmd saveImageCmd = dockerClient.saveImageCmd(image);

                final InputStream inputStream = saveImageCmd.exec();
                final FileOutputStream outputStream = new FileOutputStream(targetFile)
        ) {
            IOUtils.copyLarge(inputStream, outputStream);
        }
    }

    private static DockerClient newDockerClient() {

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        System.out.println(config.toString());

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();


        return DockerClientImpl.getInstance(config,httpClient);
    }

    @Override
    public void deleteAll(String imageName)  {
        log.info("Cleaning up all docker images for {} ", imageName);

        ListImagesCmd listImagesCmd = dockerClient.listImagesCmd().withImageNameFilter(imageName);
        List<Image> images = listImagesCmd.exec();
        log.info("Found the following images for deletion {}", images);

        for (Image image : images) {
            log.info("Removing {} {}", image.getId(), image.getRepoTags());

            try {
                RemoveImageCmd removeImageCmd = dockerClient.removeImageCmd(image.getId());
                removeImageCmd.exec();
                log.info(" -> Successfully removed image {} ", removeImageCmd.getImageId());
            } catch (Exception e) {
                log.error("Failed to remove image {}, reason: ", e);
            }

        }


    }

    @Override
    public void start() throws StartingException {
        getLogger().debug("Starting docker client ");
        if (dockerClient == null) {
            try {
                dockerClient = newDockerClient();
            } catch (Exception e) {
                throw new StartingException("Can't initialize docker client" + e.getMessage(), e);
            }
        }
    }

    @Override
    public void stop() throws StoppingException {
        getLogger().debug("Stopping docker client ");
        if (Objects.nonNull(dockerClient)) {
            try {
                dockerClient.close();
            } catch (IOException e) {
                log.error("Can't close docker client. Reason: {}", e);
            }
        }
    }


}
