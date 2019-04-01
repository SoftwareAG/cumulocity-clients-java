package com.cumulocity.microservice.properties;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static java.nio.file.Files.exists;

@RequiredArgsConstructor
public class ConfigurationFileProvider {

    public static final String PACKAGE_DIRECTORY = "${package.directory:${application.name:'application'}}";
    private final Environment environment;

    public Iterable<Path> find(String... suffix) {
        final String packageDirectory = resolveDirectory();

        final String fileName = environment.getProperty("package.name", packageDirectory);

        return find(new String[]{fileName}, suffix);
    }

    private String resolveDirectory() {
        return environment.resolvePlaceholders(PACKAGE_DIRECTORY);
    }

    public Iterable<Path> find(final String[] fileNames, String... suffix) {
        final List<String> files = FluentIterable.from(suffix)
                .transformAndConcat(new Function<String, Iterable<String>>() {
                    public Iterable<String> apply(final String suffix) {
                        return FluentIterable.from(fileNames)
                                .transform(new Function<String, String>() {
                                    @Override
                                    public String apply(String fileName) {
                                        return fileName + suffix;
                                    }
                                });
                    }
                })
                .toList();

        return from(directories())
                .transformAndConcat(new Function<Path, Iterable<Path>>() {
                    public Iterable<Path> apply(final Path fileSupplier) {
                        return from(files).transform(new Function<String, Path>() {
                            public Path apply(String file) {
                                return fileSupplier.resolve(file);
                            }
                        });
                    }
                })
                .filter(new Predicate<Path>() {
                    public boolean apply(Path file) {
                        return exists(file);
                    }
                });
    }

    private Iterable<Path> directories() {
        String packageDirectory = resolveDirectory();
        return FluentIterable.from(new String[]{
                "${" + packageDirectory + ".config.dir}/." + packageDirectory,
                "${" + packageDirectory + ".config.dir}/" + packageDirectory,
                "${user.home}/." + packageDirectory,
                "${user.home}/" + packageDirectory,
                "${conf.dir}/." + packageDirectory,
                "${conf.dir}/" + packageDirectory,
                "/etc/" + packageDirectory
        }).transform(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return environment.resolvePlaceholders(s);
            }
        }).transform(new Function<String, Path>() {
            @Override
            public Path apply(String s) {
                return Paths.get(s);
            }
        });

    }
}
