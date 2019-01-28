package com.cumulocity.microservice.settings.service;

import java.util.Map;

public interface MicroserviceSettingsService {

    Map<String, String> getAll();

    <T> T getAs(Class<T> clazz);

    String decryptAndGet(String key);
}
