package com.cumulocity.lpwan.connectivity.service;

import java.util.Map;

public interface LnsInstanceService {

    void createLnsInstance(Map<String, Map<String, String>> lnsInstance);

    Map<String, Map<String, String>> getLnsInstances();

    Map<String, Map<String, String>> getAnLnsInstance(String lnsInstanceName);

    void updateAnLnsInstance(String existingLnsInstanceName, Map<String, Map<String, String>> lnsInstanceToUpdate);

    void deleteAnLnsInstance(String lnsInstanceName);
}
