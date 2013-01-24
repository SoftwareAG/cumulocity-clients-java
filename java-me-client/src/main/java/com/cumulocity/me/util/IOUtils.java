package com.cumulocity.me.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;

import com.cumulocity.me.logger.Log;
import com.cumulocity.me.sdk.SDKException;

public abstract class IOUtils {

    private static final Log LOG = Log.getInstance();

    public static final int BUFF_SIZE = 2048;

    protected IOUtils() {
    }

    public static final void closeQuietly(InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (IOException e) {
            IOUtils.LOG.debug(e.getMessage());
        }
    }

    public static final void closeQuietly(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (IOException e) {
            LOG.debug(e.getMessage());
        }
    }

    public static final byte[] readData(int inputLength, InputStream inputStream) {
        if (inputLength < 0 || inputStream == null) {
            return null;
        }

        final int maxNumberOfAttempts = 10;
        final int nextAttemptTimeout = 500;
        int bytesCount = 0;
        int consecutiveFailedAttemptsCount = 0;

        try {
            byte[] data = new byte[inputLength];
            
            int len = BUFF_SIZE;
            int read = 0;

            do {
                do {
                    if (len > (inputLength - bytesCount)) {
                        len = (inputLength - bytesCount);
                    }

                    read = inputStream.read(data, bytesCount, len);
                    
                    if (read > 0) {
                        consecutiveFailedAttemptsCount = 0;
                        bytesCount += read;
                    }
                } while (read == len && bytesCount < inputLength);

                if (bytesCount < inputLength && consecutiveFailedAttemptsCount < maxNumberOfAttempts) {
                    consecutiveFailedAttemptsCount++;
                    Thread.sleep(nextAttemptTimeout);
                } else {
                    break;
                }
            } while (true);

            return data;
        } catch (IOException e) {
            throw new SDKException("I/O error!", e);
        } catch (InterruptedException e) {
            throw new SDKException("Interrupted!", e);
        } finally {
            if (bytesCount < inputLength) {
                throw new SDKException("Received only " + bytesCount + " of " + inputLength + " expected bytes!");
            }
        }
    }

    public static final void writeData(byte[] input, OutputStream output) {
        if (input == null) {
            return;
        }
        try {
            output.write(input);
        } catch (IOException e) {
            throw new SDKException("I/O error!", e);
        }
    }
}
