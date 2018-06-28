package com.cumulocity.microservice.security.service;

public interface SecurityExpressionService {

    boolean isFeatureEnabled(String featureName);

    boolean isServiceUser(String service);
}
