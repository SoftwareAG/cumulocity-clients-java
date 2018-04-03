package com.cumulocity.agent.packaging.platform.client.impl;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Getter;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class ProgressInputStream extends FilterInputStream {

    /**
     * progress from 0 to 100
     */
    public interface UpdateProgressListener {
        void onStart();
        void onProgress(int prevValue, int newValue);
        void onEnd();
    }

    public static final UpdateProgressListener NO_OP_PROGRESS = new UpdateProgressListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onProgress(int prevValue, int newValue) {
        }

        @Override
        public void onEnd() {
        }
    };

    public static final UpdateProgressListener SYSOUT_PROGRESS = new UpdateProgressListener() {
        @Override
        public void onStart() {
            System.out.println("|" + Strings.repeat("-", 100) + "|");
            System.out.flush();
            System.out.print("|");
            System.out.flush();
        }

        public void onProgress(int prevValue, int newValue) {
            System.out.print(Strings.repeat("*", newValue - prevValue));
            System.out.flush();
        }

        @Override
        public void onEnd() {
            System.out.println("|");
            System.out.flush();
        }
    };

    private final UpdateProgressListener progressListener;
    private final long maxNumBytes;
    private AtomicLong totalNumBytesRead = new AtomicLong(0);
    private AtomicInteger percents = new AtomicInteger(0);
    private volatile long marked;

    @Builder
    public static InputStream create(final InputStream inputStream, UpdateProgressListener progressListener) throws IOException {
        if (progressListener == null) {
            progressListener = NO_OP_PROGRESS;
        }
        return new ProgressInputStream(inputStream, inputStream.available(), progressListener);
    }

    public ProgressInputStream(InputStream in, long maxNumBytes, UpdateProgressListener updateListener) {
        super(in);
        this.progressListener = updateListener;
        this.maxNumBytes = maxNumBytes;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        updateProgress(1);
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return (int) updateProgress(super.read(b, off, len));
    }

    @Override
    public long skip(long n) throws IOException {
        return updateProgress(super.skip(n));
    }

    @Override
    public void mark(int readLimit) {
        this.marked = totalNumBytesRead.get();
        super.mark(readLimit);
    }

    @Override
    public void reset() throws IOException {
        totalNumBytesRead.set(marked);
        super.reset();
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    private long updateProgress(long numBytesRead) {
        try {
            if (numBytesRead > 0) {
                int prevPercents = percents.get();
                long newBytes = totalNumBytesRead.addAndGet(numBytesRead);
                int newPercents = (int) ((newBytes * 100) / maxNumBytes);

                if (prevPercents != newPercents) {
                    percents.set(newPercents);

                    if (prevPercents == 0) {
                        progressListener.onStart();
                    }

                    progressListener.onProgress(prevPercents, newPercents);

                    if (newPercents == 100) {
                        progressListener.onEnd();
                    }
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return numBytesRead;
    }
}
