package com.cumulocity.agent.packaging.microservice.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PushResponseItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.Sets;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MicroserviceDockerClientImplTest {

    private final int TEST_IMAGE_SIZE = 100 * FileUtils.ONE_MB;

    @Mock
    DockerClient dockerClientMock;

    @InjectMocks
    MicroserviceDockerClientImpl dockerClient;

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    BuildImageCmd buildImageCmd;

    private String dockerDir;
    private String networkeMode;
    private Map<String, String> buildArgs;
    private Set<String> tags;

    @BeforeEach
    public void init() throws IOException {
        temporaryFolder.create();
    }

    @AfterEach
    public void cleanUp() {
        temporaryFolder.delete();
    }

    @Test
    void buildDockerImageWithoutError() {

        //When I mock the Docker java client

        prepareDockerClientMockForBuild();
        pepareBuildCommandMock(false);

        //and I build the image with our client
        dockerClient.buildDockerImage(dockerDir, tags, buildArgs, networkeMode);

        //then docker was instructed to build the file
        //dockerClientMock.buildImageCmd(eq(new File(dockerDir)));
        verify(dockerClientMock, times(1)).buildImageCmd(eq(new File(dockerDir)));
    }

    @Test
    void buildDockerImageWithError() {

        //When I mock the Docker java client

        prepareDockerClientMockForBuild();
        pepareBuildCommandMock(true);

        //and I build the image with our client, then the docker error is propagated

        assertThrows(RuntimeException.class, () -> {
            dockerClient.buildDockerImage(dockerDir, tags, buildArgs, networkeMode);
        });

        verify(dockerClientMock, times(1)).buildImageCmd(eq(new File(dockerDir)));
    }

    private void pepareBuildCommandMock(boolean generateError) {
        //mock a three step build process, possibly with an error in the end.
        when (buildImageCmd.exec(any(BuildImageResultCallback.class))).thenAnswer(mockInvocation -> {

            BuildImageResultCallback imageResultCallback = (BuildImageResultCallback) mockInvocation.getArgument(0);

            imageResultCallback.onNext(mockBuildProgressItem("Progress 1"));
            imageResultCallback.onNext(mockBuildProgressItem("Progress 2"));
            imageResultCallback.onNext(mockBuildProgressItem("Progress 3"));
            if (!generateError) {
                imageResultCallback.onComplete();
            } else {
                imageResultCallback.onError(new RuntimeException("Mock Docker Execution Error"));
            }
            return null;


        });

        when(dockerClientMock.buildImageCmd(any(File.class))).thenReturn(buildImageCmd);
    }

    private void prepareDockerClientMockForBuild() {
        dockerDir = "/foo/bar";
        tags = Sets.newHashSet("tag1","tag2","tag3");
        buildArgs = Maps.newHashMap();
        buildArgs.put("IMAGEARCH","amd64");
        buildArgs.put("hey","yolo");
        networkeMode = "none";

        when(buildImageCmd.withBuildArg(any(), any())).thenReturn(buildImageCmd);
        when(buildImageCmd.withNetworkMode(any())).thenReturn(buildImageCmd);
        when(buildImageCmd.withTags(any())).thenReturn(buildImageCmd);
    }

    private BuildResponseItem mockBuildProgressItem(String text) {
        BuildResponseItem buildResponseItem = mock(BuildResponseItem.class);
        when(buildResponseItem.getStream()).thenReturn(text);
        return buildResponseItem;
    }

    @SneakyThrows
    @Test
    void saveDockerImage() {

        //When I mock a docker image in memory
        byte[] mockImage = new byte[TEST_IMAGE_SIZE];
        Random random = new Random();
        random.nextBytes(mockImage);
        String sourceImageDigest = Hex.encodeHexString(DigestUtils.getSha256Digest().digest(mockImage));
        log.info("Created mock image in memory with size {}, digest {}", mockImage.length, sourceImageDigest);

        //and I tell the mocked docker client to present this as an output stream for the saved image
        SaveImageCmd saveImageCmd = mock(SaveImageCmd.class);
        when(saveImageCmd.exec()).thenReturn(new ByteArrayInputStream(mockImage));
        when(dockerClientMock.saveImageCmd("test-image")).thenReturn(saveImageCmd);

        //and I tell our implementation client to save this image to a temporary target file.
        File targetFile = temporaryFolder.newFile();
        dockerClient.saveDockerImage("test-image", targetFile);

        //then the target file contains the exact bytes and the hashes are the same.
        File savedImage = new File(targetFile.getAbsolutePath());
        String targetImageDigest = Hex.encodeHexString(DigestUtils.digest(DigestUtils.getSha256Digest(),savedImage));

        assertEquals("Image corrupted by save method!", sourceImageDigest, targetImageDigest);


    }

    @Test
    void testDeleteAll() {

        //When I have a image called testimage mocked
        Image image = getMockedImage("testimage");
        RemoveImageCmd removeImageCmd = mockAndGetRemoveImageCmd();

        ListImagesCmd listImagesCmd = mock(ListImagesCmd.class);
        when(listImagesCmd.getFilters()).thenReturn(Maps.newHashMap());
        when(listImagesCmd.exec()).thenReturn(Lists.newArrayList(image));
        when(dockerClientMock.listImagesCmd()).thenReturn(listImagesCmd);

        //and I delete it
        dockerClient.deleteAll("testimage",true);
        //then our client calls the corresponding remove command.
        verify(removeImageCmd,times(1)).exec();


    }

    private RemoveImageCmd mockAndGetRemoveImageCmd() {
        RemoveImageCmd removeImageCmd = mock(RemoveImageCmd.class);
        when(removeImageCmd.getImageId()).thenReturn("testimage");
        when(removeImageCmd.withForce(any())).thenReturn(removeImageCmd);
        when(dockerClientMock.removeImageCmd("testimage")).thenReturn(removeImageCmd);
        return removeImageCmd;
    }

    private Image getMockedImage(String imageName) {
        Image image = mock(Image.class);
        when(image.getId()).thenReturn(imageName);
        when(image.getRepoTags()).thenReturn(new String[]{"A","B","C"});
        return image;
    }

    @Test
    void tagImage() {

        TagImageCmd tagImageCmd = mock(TagImageCmd.class);
        when(dockerClientMock.tagImageCmd(any(), any(), any())).thenAnswer(invocationOnMock -> {
            return tagImageCmd;
        });

        //make sure our docker client passes the tagging command to docker and executes it
        dockerClient.tagImage("hello","bla/hello","world");
        verify(tagImageCmd,times(1)).exec();

    }

    @Test
    void pushImageWithoutError() {

        //when I mock a push process
        PushImageCmd pushImageCmd = mock(PushImageCmd.class);
        when(pushImageCmd.exec(any())).thenAnswer(invocationOnMock -> {
            ResultCallback resultCallback = (ResultCallback) invocationOnMock.getArgument(0);
            resultCallback.onNext(getMockedPushResponseItem("Pushing"));
            resultCallback.onNext(getMockedPushResponseItem("Still pushing"));
            resultCallback.onComplete();
            return null;
        });

        when(dockerClientMock.pushImageCmd("testimage2")).thenReturn(pushImageCmd);

        //and I then ask our client to push this image
        dockerClient.pushImage("testimage2");
        //then our push image command is executed on the docker side
        verify(pushImageCmd,times(1)).exec(any());

    }

    @Test
    void pushImageWithError() {
        //when I mock a push process
        PushImageCmd pushImageCmd = mock(PushImageCmd.class);
        when(pushImageCmd.exec(any())).thenAnswer(invocationOnMock -> {
            ResultCallback resultCallback = (ResultCallback) invocationOnMock.getArgument(0);
            resultCallback.onError(new RuntimeException("Push failed"));
            return null;
        });

        when(dockerClientMock.pushImageCmd("testimage2")).thenReturn(pushImageCmd);

        //and I then ask our client to push this image
        assertThrows(RuntimeException.class, () -> {
            dockerClient.pushImage("testimage2");
        });
        //then our push image command is executed on the docker side
        verify(pushImageCmd,times(1)).exec(any());

    }

    private PushResponseItem getMockedPushResponseItem(String stream) {
        PushResponseItem pushResponseItem = mock(PushResponseItem.class);
        when(pushResponseItem.getStream()).thenReturn(stream);
        return pushResponseItem;
    }


}