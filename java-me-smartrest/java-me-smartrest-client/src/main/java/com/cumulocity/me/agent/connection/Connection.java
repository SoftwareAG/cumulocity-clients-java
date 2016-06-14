package com.cumulocity.me.agent.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection {

    protected InputStream inputStream;

    protected OutputStream outputStream;

    protected void closeConnection() throws IOException {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    public void write(byte[] message) throws IOException {
        outputStream.write(message);
    }

    public byte[] read(int bytes) throws IOException {
        byte[] response = new byte[bytes];
        inputStream.read(response);
        return response;
    }

    public int read() throws IOException {
        return inputStream.read();
    }
}
