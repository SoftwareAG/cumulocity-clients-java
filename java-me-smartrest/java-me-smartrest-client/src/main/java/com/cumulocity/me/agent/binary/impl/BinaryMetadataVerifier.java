package com.cumulocity.me.agent.binary.impl;

import com.cumulocity.me.agent.binary.exception.VerificationException;
import com.cumulocity.me.agent.binary.model.BinaryMetadata;

public class BinaryMetadataVerifier {
    public static void verfyCreate(BinaryMetadata metadata) throws VerificationException{
        notNull(metadata.getName(), "Name was null");
        notNull(metadata.getType(), "Type was null");
        isNull(metadata.getId(), "Id was not null");
    }

    public static void verifyUpdate(BinaryMetadata metadata) throws VerificationException {
        notNull(metadata.getId(), "Id was null");
        notNull(metadata.getContentType(), "Content type was null");
    }

    public static void verifyGet(BinaryMetadata metadata) throws VerificationException {
        notNull(metadata.getId(), "Id was null");
    }

    private static void notNull(Object object, String errorMessage) throws VerificationException {
        if (object == null) {
            throw new VerificationException(errorMessage);
        }
    }

    private static void isNull(Object object, String errorMessage) throws VerificationException {
        if (object != null) {
            throw new VerificationException(errorMessage);
        }
    }
}
