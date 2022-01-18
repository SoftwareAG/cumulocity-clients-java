/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.customdecoders.api.util;

import java.util.Optional;
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
