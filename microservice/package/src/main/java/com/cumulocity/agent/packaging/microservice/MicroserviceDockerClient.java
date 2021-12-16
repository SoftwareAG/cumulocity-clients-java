package com.cumulocity.agent.packaging.microservice;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface MicroserviceDockerClient {
    void saveDockerImage(final String image, final File targetFile) throws IOException;

    void buildDockerImage(String dockerDirectory, Set<String> tags, Map<String, String> buildArgs, String networkMode);

    void deleteAll(String image);
}
