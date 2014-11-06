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
    
    public static final void closeQuietly(OutputStream out) {
        if (out == null) {
            return;
        } 
        try {
            out.close();
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
    
    public static final String readData(InputStream input) {
        final int maxNumberOfAttempts = 3;
        final int nextAttemptTimeout = 250;
        int consecutiveFailedAttemptsCount = 0;
        boolean lookForHeartbeat = true;
        StringBuffer line = new StringBuffer();
        try {
            
            do {
                int c;
                while ((c = input.read()) != -1) {
                    if (lookForHeartbeat) {
                        lookForHeartbeat = isHeartbeat(c);
                    }
                    if (!lookForHeartbeat) {
                        consecutiveFailedAttemptsCount = 0;
                        line.append((char)c);
                    }
                }
                if (consecutiveFailedAttemptsCount < maxNumberOfAttempts) {
                    consecutiveFailedAttemptsCount++;
                    Thread.sleep(nextAttemptTimeout);
                } else {
                    break;
                }
            } while(true);
            
            return line.toString();
        } catch(IOException e) {
            throw new SDKException("I/O error!", e);
        } catch (InterruptedException e) {
            throw new SDKException("Interrupted!", e);
        } finally {
            IOUtils.closeQuietly(input);
            input = null;
        }
    }
    
    public static final boolean isHeartbeat(int c) {
        return c == 32;
    }
}
