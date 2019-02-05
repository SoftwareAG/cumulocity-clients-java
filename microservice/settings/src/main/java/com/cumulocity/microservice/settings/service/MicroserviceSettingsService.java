package com.cumulocity.microservice.settings.service;

import lombok.NonNull;

import java.util.Map;

public interface MicroserviceSettingsService {

    Map<String, String> getAll();

    <T> T getAs(Class<T> clazz);

    String getCredential(String key);

    String get(@NonNull String key);
}
