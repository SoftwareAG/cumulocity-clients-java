package com.cumulocity.agent.server.protocol;

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Transformer;
import org.glassfish.grizzly.filterchain.AbstractCodecFilter;

public class ProtocolFilter<T> extends AbstractCodecFilter<Buffer, T> {

    public ProtocolFilter(Transformer<Buffer, T> decoder, Transformer<T, Buffer> encoder) {
        super(decoder, encoder);
    }

}
