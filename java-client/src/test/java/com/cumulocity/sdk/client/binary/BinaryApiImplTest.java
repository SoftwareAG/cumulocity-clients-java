
package com.cumulocity.sdk.client.binary;


import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.BinariesApi;
import com.cumulocity.sdk.client.inventory.BinariesApiImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class BinaryApiImplTest {

    private static final String INVENTORY_MANAGEDOBJECTS_URL = "http://localhost/inventory/managedObjects";

    private static final String INVENTORY_BINARY_URL = "http://localhost/inventory/binaries";

    BinariesApi binariesApi;

    InventoryRepresentation inventoryRepresentation = new InventoryRepresentation();

    @Mock
    private RestConnector restConnector;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ManagedObjectReferenceCollectionRepresentation representation = new ManagedObjectReferenceCollectionRepresentation();
        representation.setSelf(INVENTORY_MANAGEDOBJECTS_URL);
        inventoryRepresentation.setManagedObjects(representation);

        binariesApi = new BinariesApiImpl(restConnector, inventoryRepresentation);
    }


    @Test
    public void shouldUploadFileAsByteArray() throws SDKException {
        //Given
        byte[] binaryData = {1,2,4,6,6};
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        when(restConnector.postFile(INVENTORY_BINARY_URL, managedObjectRepresentation,binaryData,ManagedObjectRepresentation.class)).thenReturn(created);

        // When
        ManagedObjectRepresentation mor = binariesApi.uploadFile(managedObjectRepresentation,binaryData);

        // Then
        assertThat(mor, sameInstance(created));
    }

    @Test
    public void shouldUploadFileAsStream() throws SDKException {
        //Given
        InputStream inputStream = IOUtils.toInputStream("hello");
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        when(restConnector.postFileAsStream(INVENTORY_BINARY_URL, managedObjectRepresentation,inputStream, ManagedObjectRepresentation.class)).thenReturn(created);

        // When
        ManagedObjectRepresentation mor = binariesApi.uploadFile(managedObjectRepresentation,inputStream);

        // Then
        assertThat(mor, sameInstance(created));
    }

    @Test
    public void shouldReplaceFile() throws SDKException {
        //Given
        InputStream inputStream = IOUtils.toInputStream("hello");
        GId binaryId = GId.asGId(123);
        ManagedObjectRepresentation updated = new ManagedObjectRepresentation();
        when(restConnector.putStream(INVENTORY_BINARY_URL  + "/" +binaryId.getValue(), MediaType.APPLICATION_OCTET_STREAM,inputStream,ManagedObjectRepresentation.class)).thenReturn(updated);

        // When
        ManagedObjectRepresentation mor = binariesApi.replaceFile(binaryId,MediaType.APPLICATION_OCTET_STREAM,inputStream);

        // Then
        assertThat(mor, sameInstance(updated));
    }

    @Test
    public void shouldDownloadFile() throws SDKException {
        //Given
        InputStream inputStream = IOUtils.toInputStream("hello");
        GId binaryId = GId.asGId(123);
        when(restConnector.get(INVENTORY_BINARY_URL  + "/" +binaryId.getValue(), MediaType.APPLICATION_OCTET_STREAM_TYPE,InputStream.class)).thenReturn(inputStream);

        // When
        InputStream mor = binariesApi.downloadFile(binaryId);

        // Then
        assertThat(mor, sameInstance(inputStream));
    }

    @Test
    public void shouldDeleteFile() throws SDKException {
        //Given
        GId binaryId = GId.asGId(123);

        // When
        binariesApi.deleteFile(binaryId);

        // Then
        verify(restConnector, times(1)).delete(INVENTORY_BINARY_URL+ "/" +binaryId.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnDeleteFileIfBinaryIDIsNull() throws SDKException {
        //Given

        // When
        binariesApi.deleteFile(null);

        // Then
        fail();

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnDownloadFileIfBinaryIDIsNull() throws SDKException {
        //Given

        // When
        binariesApi.downloadFile(null);

        // Then
        fail();

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnReplaceFileIfBinaryIDIsNull() throws SDKException {
         //Given
         InputStream inputStream = IOUtils.toInputStream("hello");

        // When
        binariesApi.replaceFile(null,MediaType.APPLICATION_OCTET_STREAM,inputStream);

        // Then
        fail();

    }

    @Test(expected = SDKException.class)
    public void shouldThrowSDKExceptionIfAnyIssueWhileRestCallOnUploadFileAsByteArray() throws SDKException {
        //Given
        byte[] binaryData = {1,2,4,6,6};
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        when(restConnector.postFile(INVENTORY_BINARY_URL, managedObjectRepresentation,binaryData,ManagedObjectRepresentation.class)).thenThrow(SDKException.class);

        // When
        binariesApi.uploadFile(managedObjectRepresentation,binaryData);

        // Then
        fail();
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionIfAnyIssueWhileRestCallOnUploadFileAsStream() throws SDKException {
        //Given
        InputStream inputStream = IOUtils.toInputStream("hello");
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        when(restConnector.postFileAsStream(INVENTORY_BINARY_URL, managedObjectRepresentation,inputStream,ManagedObjectRepresentation.class)).thenThrow(SDKException.class);

        // When
        binariesApi.uploadFile(managedObjectRepresentation,inputStream);

        // Then
        fail();
    }

    @Test(expected = SDKException.class)
    public void shouldThrowSDKExceptionIfAnyIssueWhileRestCallOnDownloadFile() throws SDKException {
        //Given
        GId binaryId = GId.asGId(123);
        when(restConnector.get(INVENTORY_BINARY_URL  + "/" +binaryId.getValue(), MediaType.APPLICATION_OCTET_STREAM_TYPE,InputStream.class)).thenThrow(SDKException.class);

        // When
        InputStream mor = binariesApi.downloadFile(binaryId);

        // Then
        fail();
    }

    @Test(expected = SDKException.class)
    public void shouldThrowSDKExceptionIfAnyIssueWhileRestCallOnReplaceFile() throws SDKException {
        //Given
        InputStream inputStream = IOUtils.toInputStream("hello");
        GId binaryId = GId.asGId(123);
        ManagedObjectRepresentation updated = new ManagedObjectRepresentation();
        when(restConnector.putStream(INVENTORY_BINARY_URL  + "/" +binaryId.getValue(), MediaType.APPLICATION_OCTET_STREAM,inputStream,ManagedObjectRepresentation.class)).thenThrow(SDKException.class);

        // When
        binariesApi.replaceFile(binaryId,MediaType.APPLICATION_OCTET_STREAM,inputStream);

        // Then
        fail();
    }


}
