package com.cumulocity.microservice.properties;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.io.File;

import static com.google.common.collect.FluentIterable.from;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

@RequiredArgsConstructor
public class ConfigurationFileProvider {
    private final String applicationName;

    public Iterable<File> find(final String... files) {
        return from(directories())
                .transformAndConcat(new Function<File, Iterable<File>>() {
                    public Iterable<File> apply(final File fileSupplier) {
                        return from(files).transform(new Function<String, File>() {
                            public File apply(String file) {
                                return new File(fileSupplier, file);
                            }
                        });
                    }
                })
                .filter(new Predicate<File>() {
                    public boolean apply(File file) {
                        return file.exists();
                    }
                });
    }

    private Iterable<File> directories() {
        return Lists.newArrayList(
                new File(getenv(applicationName.toUpperCase() + "_CONF_DIR"), "." + applicationName),
                new File(getenv(applicationName.toUpperCase() + "_CONF_DIR"), applicationName),

                new File(getProperty("user.home"), "." + applicationName),
                new File(getProperty("user.home"), applicationName),

                new File(getenv("CONF_DIR"), "." + applicationName),
                new File(getenv("CONF_DIR"), applicationName),

                new File("/etc", applicationName)
        );
    }
}
