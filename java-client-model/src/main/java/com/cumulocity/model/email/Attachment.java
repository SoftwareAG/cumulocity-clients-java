package com.cumulocity.model.email;

import com.cumulocity.model.io.DisposableInputStreamSource;
import lombok.Builder;
import lombok.Value;

import java.io.Closeable;
import java.io.IOException;

@Value
@Builder(builderMethodName = "aAttachment")
public class Attachment implements Closeable {
    private String filename;

    private DisposableInputStreamSource inputStreamSource;

    @Override
    public void close() throws IOException {
        inputStreamSource.dispose();
    }

}
