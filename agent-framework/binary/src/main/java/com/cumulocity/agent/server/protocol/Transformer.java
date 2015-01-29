package com.cumulocity.agent.server.protocol;

import org.glassfish.grizzly.TransformationResult;
import org.glassfish.grizzly.attributes.AttributeStorage;

public interface Transformer<I, O> {

    TransformationResult<I, O> transform(AttributeStorage connection, I message);

    boolean hasInputRemaining(AttributeStorage connection, I message);

    void release(AttributeStorage connection);

}
