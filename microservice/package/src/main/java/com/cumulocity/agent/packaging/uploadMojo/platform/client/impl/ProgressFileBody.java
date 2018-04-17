package com.cumulocity.agent.packaging.uploadMojo.platform.client.impl;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.util.Args;

import java.io.*;

public class ProgressFileBody extends AbstractContentBody {
    private final File file;
    private final String filename;

    public ProgressFileBody(File file, ContentType contentType, String filename) {
        super(contentType);
        Args.notNull(file, "File");
        this.file = file;
        this.filename = filename == null ? file.getName() : filename;
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
                .maxNumBytes(file.length())
                .progressListener(new ProgressInputStream.SysOutProgress())
                .build();
    }

    public String getTransferEncoding() {
        return "binary";
    }

    public long getContentLength() {
////        if the content length is unknown the entity is not repeatable
//        return -1;
        return this.file.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
