package com.cumulocity.microservice.properties;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.util.List;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.FluentIterable.from;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

@RequiredArgsConstructor
public class ConfigurationFileProvider {

    private final ConfigurableEnvironment environment;

    public Iterable<File> find(String... suffix) {
        final String packageDirectory = fromNullable(environment.getProperty("package.directory"))
                .or(fromNullable(environment.getProperty("application.name")))
                .or("application");

        final String packageName = fromNullable(environment.getProperty("package.name"))
                .or(packageDirectory);

        final List<String> files = FluentIterable.from(suffix)
                .transform(new Function<String, String>() {
                    public String apply(String input) {
                        return packageName + input;
                    }
                })
                .toList();

        return from(directories(packageDirectory))
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

    private Iterable<File> directories(String directory) {
        return Lists.newArrayList(
                new File(getenv(directory.toUpperCase() + "_CONF_DIR"), "." + directory),
                new File(getenv(directory.toUpperCase() + "_CONF_DIR"), directory),

                new File(getProperty("user.home"), "." + directory),
                new File(getProperty("user.home"), directory),

                new File(getenv("CONF_DIR"), "." + directory),
                new File(getenv("CONF_DIR"), directory),

                new File("/etc", directory)
        );
    }
}