package com.cumulocity.sdk.client.buffering;

import static com.cumulocity.sdk.client.buffering.FileBasedPersistentProvider.NEW_REQUESTS_PATH;
import static com.cumulocity.sdk.client.buffering.FileBasedPersistentProvider.NEW_REQUESTS_TEMP_PATH;
import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import javax.ws.rs.HttpMethod;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;

public class FileBasedPersistentProviderTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    String pathToTempFolder;
    FileBasedPersistentProvider persistentProvider;
    
    @Before
    public void setup() {
        pathToTempFolder = testFolder.getRoot().getPath();
        persistentProvider = new FileBasedPersistentProvider(pathToTempFolder);
    }

    @Test
    public void shouldInitializeFiles() {
        assertThat(new File(pathToTempFolder + NEW_REQUESTS_TEMP_PATH)).exists();
        assertThat(new File(pathToTempFolder + NEW_REQUESTS_PATH)).exists();
    }
    
    @Test
    public void shouldInitializeRequestIdCounter() throws Exception {
        new File(pathToTempFolder + NEW_REQUESTS_PATH + "13").createNewFile();
        
        persistentProvider = new FileBasedPersistentProvider(pathToTempFolder);
        
        assertThat(persistentProvider.counter.get()).isGreaterThan(13);
    }
    
    @Test
    public void shouldPersistRequestToFile() throws Exception {
        BufferedRequest request = BufferedRequest.create(HttpMethod.POST, "test", AlarmMediaType.ALARM, new AlarmRepresentation());
        
        persistentProvider.offer(new ProcessingRequest(1 ,request));
        
        assertThat(new File(pathToTempFolder + NEW_REQUESTS_PATH + "1")).exists();
    }
    
    @Test
    public void shouldNotCreateAFileWhenQueueIsFull() throws Exception {
        BufferedRequest request = BufferedRequest.create(HttpMethod.POST, "test", AlarmMediaType.ALARM, new AlarmRepresentation());
        persistentProvider = new FileBasedPersistentProvider(1, pathToTempFolder);
        
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Queue is full");
        
        persistentProvider.offer(new ProcessingRequest(1 ,request));
        persistentProvider.offer(new ProcessingRequest(2 ,request));
    }
    
    @Test
    public void shouldReturnRequestFromQueue() throws Exception {
        BufferedRequest request = BufferedRequest.create(HttpMethod.POST, "test", AlarmMediaType.ALARM, new AlarmRepresentation());
        persistentProvider.offer(new ProcessingRequest(1 ,request));
        
        ProcessingRequest result = persistentProvider.poll();
        
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getEntity().getRepresentation()).isInstanceOf(AlarmRepresentation.class);
    }
}
