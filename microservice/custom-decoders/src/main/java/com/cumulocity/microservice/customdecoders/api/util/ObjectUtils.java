package com.cumulocity.microservice.customdecoders.api.util;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <T, S> S emptyOrElse(Optional<T> optional, Supplier<S> supplier) {
        if(!optional.isPresent()) {
            return null;
        }
        return supplier.get();
    }

    public static boolean isEmpty(String charSequence) {
        return StringUtils.isEmpty(charSequence);
    }
}
