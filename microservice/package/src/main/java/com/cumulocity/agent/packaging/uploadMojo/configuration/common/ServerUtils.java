package com.cumulocity.agent.packaging.uploadMojo.configuration.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.settings.Server;

import java.util.List;

import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.XmlNodeUtils.getListValue;
import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.XmlNodeUtils.getPropertyNode;

@UtilityClass
public class ServerUtils {
    public static Function<Server, String> getConfigurationString(final String ... path) {
        return new Function<Server, String>() {
            public String apply(Server input) {
                return getPropertyNode(input.getConfiguration(), path)
                        .transform(XmlNodeUtils.getStringValue())
                        .orNull();
            }
        };
    }

    public static <T> Function<Server, List<T>> getConfigurationList(final Class<T> target, final String ... path) {
        return new Function<Server, List<T>>() {
            public List<T> apply(Server input) {
                return getPropertyNode(input.getConfiguration(), path)
                        .transform(getListValue(target))
                        .orNull();
            }
        };
    }

    public static Function<Server, String> getServerPassword() {
        return new Function<Server, String>() {
            public String apply(Server input) {
                return input.getPassword();
            }
        };
    }

    public static Function<Server, String> getServerUsername() {
        return new Function<Server, String>() {
            public String apply(Server input) {
                return input.getUsername();
            }
        };
    }

    public static Predicate<String> isNotBlank() {
        return new Predicate<String>() {
            public boolean apply(String input) {
                return StringUtils.isNotBlank(input);
            }
        };
    }
}
