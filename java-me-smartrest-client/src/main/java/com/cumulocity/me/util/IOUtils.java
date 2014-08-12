/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import javax.microedition.io.Connection;

import com.cumulocity.me.sdk.SDKException;

public abstract class IOUtils {


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
        }
    }

    public static final void closeQuietly(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (IOException e) {
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
    
    public static String[] readData(Reader reader) throws IOException {
        StringBuffer line = new StringBuffer();
        int c = reader.read();

        while (c != -1) {
            line.append((char)c);
            c = reader.read();
        }
        

        return StringUtils.split(line.toString(), "\n");
    }

    public static final void writeData(byte[] input, OutputStream output) {
        if (input == null) {
            return;
        }
        try {
            output.write(input);
            output.flush();
        } catch (IOException e) {
            throw new SDKException("I/O error!", e);
        }
    }
}
