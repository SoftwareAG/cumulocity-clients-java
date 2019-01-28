package com.cumulocity.microservice.settings.service;

public interface EncryptionService {

    String encrypt(String raw);

    String decrypt(String encrypted);

}
