package com.cumulocity.me.agent.binary;

import com.cumulocity.me.agent.binary.exception.VerificationException;
import com.cumulocity.me.agent.binary.impl.BinaryHttpConnection;
import com.cumulocity.me.agent.binary.impl.BinaryMetadataVerifier;
import com.cumulocity.me.agent.binary.model.BinaryMetadata;

import java.io.InputStream;

public class BinaryService {

    private final String baseUrl;
    private final String authHeader;

    public BinaryService(String baseUrl, String authHeader) {
        this.baseUrl = baseUrl;
        this.authHeader = authHeader;
    }

    public InputStream get(BinaryMetadata metadata) throws VerificationException {
        BinaryMetadataVerifier.verifyGet(metadata);
        BinaryHttpConnection connection = new BinaryHttpConnection(baseUrl, authHeader);
        return connection.get(metadata);
    }

    public BinaryMetadata create(BinaryMetadata metadata, byte[] data) throws VerificationException {
        BinaryMetadataVerifier.verfyCreate(metadata);
        BinaryHttpConnection connection = new BinaryHttpConnection(baseUrl, authHeader);
        return connection.post(metadata, data);
    }

    public BinaryMetadata update(BinaryMetadata metadata, byte[] data) throws VerificationException {
        BinaryMetadataVerifier.verifyUpdate(metadata);
        BinaryHttpConnection connection = new BinaryHttpConnection(baseUrl, authHeader);
        return  connection.put(metadata, data);
    }
}
