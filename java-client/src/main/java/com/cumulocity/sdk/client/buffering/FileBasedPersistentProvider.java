package com.cumulocity.sdk.client.buffering;

import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.FileUtils.openOutputStream;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.write;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;


public class FileBasedPersistentProvider extends PersistentProvider {
    
    protected static final String NEW_REQUESTS_PATH = "/new-requests/";

    protected static final String NEW_REQUESTS_TEMP_PATH = "/new-requests-temp/";

    private static final int MAX_QUEUE_SIZE = 10;
    
    protected final AtomicLong counter = new AtomicLong(1);
    
    private Queue<ProcessingRequest> requests = new LinkedList<ProcessingRequest>();
    
    private volatile CountDownLatch latch = new CountDownLatch(1);
    
    private File newRequestsTemp;

    private File newRequests;
    
    public FileBasedPersistentProvider(String filePath) {
        this(DEFAULT_BUFFER_LIMIT, filePath);
    }
    
    public FileBasedPersistentProvider(long bufferLimit, String filePath) {
        super(bufferLimit);
        this.newRequestsTemp = new File(filePath + NEW_REQUESTS_TEMP_PATH);
        this.newRequests = new File(filePath + NEW_REQUESTS_PATH);
        
        setup();
    }
    
    private void setup() {
        newRequestsTemp.mkdir();
        if (newRequests.exists()) {
            initRequestIdCounter();
        } else {
            newRequests.mkdir();
        }
    }

    private void initRequestIdCounter() {
        File[] incomingFiles = getIncomingFilesSorted();
        if (incomingFiles.length > 0) {
            Long lastRequestID = Long.valueOf(incomingFiles[incomingFiles.length-1].getName());
            counter.set(lastRequestID + 10);
        }
    }
    
    @Override
    public long generateId() {
        return counter.getAndIncrement();
    }

    @Override
    public void offer(ProcessingRequest request) {
        if (newRequests.listFiles().length >= bufferLimit) {
            throw new IllegalStateException("Queue is full");
        }
        
        String filename = getFilename(request.getId());
        writeToFile(request.getEntity().toCsvString(), new File(newRequestsTemp, filename));
        moveFile(filename, newRequestsTemp, newRequests);
        latch.countDown();
    }

    private String getFilename(long requestId) {
        return requestId + "";
    }

    @Override
    public ProcessingRequest poll() {
        if (requests.isEmpty()) {
            readFilesToQueue();
        }
        
        if (requests.isEmpty()) {
            waitForRequest();
            readFilesToQueue();
            latch = new CountDownLatch(1);
        }
        return requests.poll();
    }

    private void waitForRequest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }

    private void readFilesToQueue() {
        File[] files = getIncomingFilesSorted();
        for (File file : files) {
            requests.add(readFromFile(file));
            file.delete();
            if (requests.size() == MAX_QUEUE_SIZE) {
                break;
            }
        }
    }
    
    private ProcessingRequest readFromFile(File file) {
        FileInputStream stream = null;
        try {
            stream = openInputStream(file);
            String content = IOUtils.toString(stream, "UTF-8");
            return ProcessingRequest.parse(file.getName(), content);
        } catch (IOException e) {
            throw new RuntimeException("I/O error!", e);
        } finally {
            closeQuietly(stream);
        }
    }
    
    private File[] getIncomingFilesSorted() {
        File[] files = newRequests.listFiles();
        Arrays.sort(files, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o1.getName()).compareTo(Long.valueOf(o2.getName()));
            }
        });
        return files;
    }
    
    private void writeToFile(String content, File file) {
        FileOutputStream stream = null;
        try {
            stream = openOutputStream(file);
            write(content, stream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("I/O error!", e);
        } finally {
            closeQuietly(stream);
        }
    }
    
    private boolean moveFile(String filename, File fromQueue, File toQueue) {
        File from = new File(fromQueue, filename);
        File to = new File(toQueue, filename);
        return from.renameTo(to);
    }
}
