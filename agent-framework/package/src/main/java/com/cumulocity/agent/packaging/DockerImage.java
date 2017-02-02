package com.cumulocity.agent.packaging;

import static com.google.common.base.Strings.isNullOrEmpty;

public class DockerImage {
    private final String registry;

    private final String name;

    private final String tag;

    public DockerImage(String registry, String name, String tag) {
        this.registry = registry;
        this.name = name;
        this.tag = tag;
    }

    public DockerImage withName(String name) {
        return new DockerImage(registry, name, tag);
    }

    public DockerImage withRegistry(String registry) {
        return new DockerImage(registry, name, tag);
    }

    public DockerImage withTag(String tag) {
        return new DockerImage(registry, name, tag);
    }

    public static DockerImage of(String registry) {
        return new DockerImage(registry, null, null);
    }

    public static DockerImage ofName(String name) {
        return new DockerImage(null, name, null);
    }

    @Override
    public String toString() {
        StringBuilder image = new StringBuilder();
        if (!isNullOrEmpty(registry)) {
            image.append(registry).append("/");
        }
        image.append(name);

        if (!isNullOrEmpty(tag)) {
            image.append(":").append(tag);
        }

        return image.toString();
    }
}
