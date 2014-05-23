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

import org.apache.commons.io.IOUtils;


public class FileBasedPersistentProvider extends PersistentProvider {
    
    private static final int MAX_QUEUE_SIZE = 10;
    
    private Queue<ProcessingRequest> requests = new LinkedList<ProcessingRequest>();
    
    private volatile CountDownLatch latch = new CountDownLatch(1);
    
    private File newRequestsTemp;

    private File newRequests;
    
    public FileBasedPersistentProvider(String filePath) {
        this.newRequestsTemp = new File(filePath + "/.cumulocity/new-requests-temp/");
        this.newRequests = new File(filePath + "/.cumulocity/new-requests/");
        
        this.newRequestsTemp.mkdir();
        this.newRequests.mkdir();
    }
    
    @Override
    public void offer(ProcessingRequest request) {
        if (newRequests.listFiles().length >= bufferLimit) {
            throw new IllegalStateException("Queue is full");
        }
        
        String filename = request.getId() + "";
        writeToFile(request.getEntity().toCsvString(), new File(newRequestsTemp, filename));
        moveFile(filename, newRequestsTemp, newRequests);
        latch.countDown();
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
        File[] files = getIncomingFile();
        for (File file : files) {
            requests.add(readFromFile(file));
            file.delete();
            if (requests.size() == MAX_QUEUE_SIZE) {
                break;
            }
        }
    }
    
    public ProcessingRequest readFromFile(File file) {
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
    
    private File[] getIncomingFile() {
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
    
    public boolean moveFile(String filename, File fromQueue, File toQueue) {
        File from = new File(fromQueue, filename);
        File to = new File(toQueue, filename);
        return from.renameTo(to);
    }
}
