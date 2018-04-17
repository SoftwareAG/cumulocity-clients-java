package com.cumulocity.agent.packaging.uploadMojo.configuration.common;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Optional.absent;

@UtilityClass
public class XmlNodeUtils {
    public static Function<Xpp3Dom, String> getStringValue() {
        return new Function<Xpp3Dom, String>() {
            public String apply(Xpp3Dom input) {
                return input.getValue();
            }
        };
    }

    @SneakyThrows
    public static <T> Function<Xpp3Dom, List<T>> getListValue(final Class<T> target) {
        return new Function<Xpp3Dom, List<T>>() {
            public List<T> apply(Xpp3Dom input) {
                return getListValue(input, target);
            }
        };
    }

    public static Optional<Xpp3Dom> getPropertyNode(Object root, String... path) {
        if (root instanceof Xpp3Dom) {
            Xpp3Dom node = (Xpp3Dom) root;
            for (final String element : path) {
                node = node.getChild(element);
                if (node == null) {
                    return absent();
                }
            }
            return Optional.of(node);
        }
        return absent();
    }

    private <T> List<T> getListValue(Xpp3Dom input, final Class<T> target) {
        if (input == null) {
            return null;
        }
        final List<T> result = new ArrayList<>();
        for (final Xpp3Dom child : input.getChildren()) {
            result.add(getObjectValue(child, target));
        }
        return result;
    }

    @SneakyThrows
    private <T> T getObjectValue(Xpp3Dom child, final Class<T> target) {
        if (String.class.equals(target)) {
            return (T) child.getValue();
        }

        final T element = target.newInstance();

        for (final Field field : target.getDeclaredFields()) {
            field.setAccessible(true);

            final Xpp3Dom propertyNode = child.getChild(field.getName());
            if (propertyNode != null) {
                final Class<?> fieldType = field.getType();

                if (String.class.equals(fieldType)) {
                    field.set(element, propertyNode.getValue());
                }

                if (Boolean.class.equals(fieldType)) {
                    field.set(element, Boolean.valueOf(propertyNode.getValue()));
                }

                if (List.class.equals(fieldType)) {
                    Type genericType = field.getGenericType();

                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) genericType;
                        Type type = pType.getActualTypeArguments()[0];

                        field.set(element, getListValue(propertyNode, (Class<Object>) type));
                    }
                }
            }
        }
        return element;
    }
}
