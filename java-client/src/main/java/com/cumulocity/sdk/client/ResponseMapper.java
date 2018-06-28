package com.cumulocity.sdk.client;

import java.io.InputStream;

public interface ResponseMapper {
    CharSequence write(Object object);
    <T> T read(InputStream stream, Class<T> clazz);
}
