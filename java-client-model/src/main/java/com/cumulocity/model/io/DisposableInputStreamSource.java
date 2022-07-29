package com.cumulocity.model.io;

import com.google.common.io.ByteSource;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public interface DisposableInputStreamSource {

    void dispose() throws IOException;

    InputStream getInputStream() throws IOException;

    @RequiredArgsConstructor
    class DisposableResource implements DisposableInputStreamSource {

        private final ByteSource resource;

        @Override
        public void dispose() throws IOException {
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return resource.openStream();
        }
    }
    @RequiredArgsConstructor
    class LazyInputStreamSource implements DisposableInputStreamSource {

        private final Supplier<InputStream> inputStreamSupplier;
        private InputStream inputStream;

        @Override
        public void dispose() throws IOException {
            if(inputStream != null) {
                inputStream.close();
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            dispose();
            inputStream = this.inputStreamSupplier.get();
            return inputStream;
        }
    }

}
