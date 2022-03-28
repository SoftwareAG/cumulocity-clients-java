package com.cumulocity.agent.packaging.microservice;


import org.apache.maven.MavenExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface MicroserviceDockerClient {
    void saveDockerImage(final String image, final File targetFile) throws IOException, MavenExecutionException;

    void buildDockerImage(String dockerDirectory, Set<String> tags, Map<String, String> buildArgs,  String networkMode, Integer dockerClientTimeout);

    void deleteAll(String image, boolean withForce);

    void tagImage(String image, String imageNameWithRepository, String tag);

    void pushImage(String name);

    void waitForImageInRegistry(String image, int timeOutSeconds);
}
