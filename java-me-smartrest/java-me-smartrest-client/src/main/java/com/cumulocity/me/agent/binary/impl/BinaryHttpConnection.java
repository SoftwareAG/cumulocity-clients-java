package com.cumulocity.me.agent.binary.impl;

import com.cumulocity.me.agent.binary.model.BinaryMetadata;
import com.cumulocity.me.agent.smartrest.model.template.Json;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.util.ConnectorWrapper;
import com.cumulocity.me.util.IOUtils;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class BinaryHttpConnection {
    private static final String BOUNDARY = "----J2MEStaticBoundary1234567890";
    private static final Logger LOG = LoggerFactory.getLogger(BinaryHttpConnection.class);

    private HttpConnection connection;
    private String authHeader;
    private String baseUrl;

    public BinaryHttpConnection(String baseUrl, String authHeader) {
        this.authHeader = authHeader;
        this.baseUrl = baseUrl;
    }

    private void open(String binaryId) throws IOException {
        connection = new ConnectorWrapper().open(buildUrl(binaryId));
        connection.setRequestProperty("Authorization", authHeader);
    }

    private String buildUrl(String binaryId){
        StringBuffer url = new StringBuffer(baseUrl);
        url.append("/inventory/binaries");
        if (binaryId != null) {
            url.append("/");
            url.append(binaryId);
        }
        return url.toString();
    }

    public byte[] get(BinaryMetadata metadata){
        try {
            open(metadata.getId());
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpConnection.HTTP_OK){
                throw new SDKException(responseCode, "Unexpected response for binary GET");
            }
            InputStream input = connection.openInputStream();
            Vector bytes = new Vector();
            int singleByte = input.read();
            while (singleByte != -1){
                bytes.addElement(new Byte((byte) singleByte));
                singleByte = input.read();
            }
            return copyIntoByteArray(bytes);
        } catch (IOException e) {
            throw new SDKException("IO Error BinaryHttpConnection", e);
        } finally {
            IOUtils.closeQuietly(connection);
        }
    }

    public BinaryMetadata put(BinaryMetadata metadata, byte[] data){
        try {
            open(metadata.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", metadata.getContentType());
            OutputStream output = connection.openOutputStream();
            output.write(data);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpConnection.HTTP_OK){
                InputStream input = connection.openInputStream();
                String body = IOUtils.readData(input);
                BinaryMetadataParser parser = new BinaryMetadataParser(body);
                return parser.parse();
            } else {
                throw new SDKException(responseCode, "Unexpected response for binary PUT");
            }
        } catch (IOException e) {
            throw new SDKException("IO Error BinaryHttpConnection", e);
        } finally {
            IOUtils.closeQuietly(connection);
        }
    }

    public BinaryMetadata post(BinaryMetadata metadata, byte[] data){
        try {
            open(null);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream output = connection.openOutputStream();
            writePostBody(output, metadata.getName(), metadata.getType(), data);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpConnection.HTTP_CREATED){
                InputStream input = connection.openInputStream();
                String body = IOUtils.readData(input);
                BinaryMetadataParser parser = new BinaryMetadataParser(body);
                return parser.parse();
            } else {
                throw new SDKException(responseCode, "Unexpected response for binary PUT");
            }
        } catch (IOException e) {
            throw new SDKException("IO Error BinaryHttpConnection", e);
        } finally {
            IOUtils.closeQuietly(connection);
        }
    }

    private void writePostBody(OutputStream output, String fileName, String type, byte[] data) throws IOException {
        output.write((BOUNDARY + "\n").getBytes());
        output.write("Content-Disposition: form-data; name=\"object\"\n\n".getBytes());
        output.write(Json.json("\"")
                .addString("name", fileName)
                .addString("type", type)
                .get().getBytes());
        output.write((BOUNDARY + "\n").getBytes());
        output.write("Content-Disposition: form-data; name=\"file\"\n".getBytes());
        output.write(data);
        output.write((BOUNDARY + "\n").getBytes());
    }

    private byte[] copyIntoByteArray(Vector vector){
        byte[] bytes = new byte[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            Byte currentByte = (Byte) vector.elementAt(i);
            byte primitive = currentByte.byteValue();
            bytes[i] = primitive;
        }
        return bytes;
    }
}
