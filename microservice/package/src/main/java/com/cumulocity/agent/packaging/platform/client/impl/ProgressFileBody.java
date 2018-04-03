package com.cumulocity.agent.packaging.platform.client.impl;


import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.util.Args;

import java.io.*;

public class ProgressFileBody extends AbstractContentBody {
    private final File file;
    private final String filename;

    public ProgressFileBody(File file) {
        this(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
    }

    public ProgressFileBody(File file, ContentType contentType, String filename) {
        super(contentType);
        Args.notNull(file, "File");
        this.file = file;
        this.filename = filename == null ? file.getName() : filename;
    }

    public ProgressFileBody(File file, ContentType contentType) {
        this(file, contentType, file != null ? file.getName() : null);
    }

    public InputStream getInputStream() throws IOException {
        return createInputStream(file);
    }

    public void writeTo(OutputStream out) throws IOException {
        Args.notNull(out, "Output stream");
        InputStream in = createInputStream(file);

        try {
            byte[] tmp = new byte[1024 * 32];

            int l;
            while((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }

            out.flush();
        } finally {
            in.close();
        }
    }

    protected InputStream createInputStream(File file) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        return ProgressInputStream.builder()
                .inputStream(fileInputStream)
                .progressListener(ProgressInputStream.SYSOUT_PROGRESS)
                .build();
    }

    public String getTransferEncoding() {
        return "binary";
    }

    public long getContentLength() {
        return this.file.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
