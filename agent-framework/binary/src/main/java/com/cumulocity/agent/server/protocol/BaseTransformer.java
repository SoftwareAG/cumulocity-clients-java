package com.cumulocity.agent.server.protocol;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.attributes.AttributeBuilder;
import org.glassfish.grizzly.attributes.AttributeStorage;
import org.glassfish.grizzly.attributes.DefaultAttributeBuilder;
import org.glassfish.grizzly.memory.MemoryManager;

public abstract class BaseTransformer<I, O> implements Transformer<I, O> {

    protected final AttributeBuilder attributeBuilder = DefaultAttributeBuilder.DEFAULT_ATTRIBUTE_BUILDER;

    protected MemoryManager obtainMemoryManager(AttributeStorage storage) {

        if (storage instanceof Connection) {
            Connection connection = (Connection) storage;
            return connection.getTransport().getMemoryManager();
        }

        return MemoryManager.DEFAULT_MEMORY_MANAGER;
    }

}
