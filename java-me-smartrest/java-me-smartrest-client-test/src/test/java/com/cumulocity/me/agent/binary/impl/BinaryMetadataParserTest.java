package com.cumulocity.me.agent.binary.impl;

import com.cumulocity.me.agent.binary.model.BinaryMetadata;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class BinaryMetadataParserTest {

    @Test
    public void shouldParseIdCorrectly(){
        BinaryMetadataParser parser = new BinaryMetadataParser("{\"id\":\"12345\"}");
        String id = parser.getId();
        assertThat(id).isEqualTo("12345");
    }

    @Test
    public void shouldParseNullIdCorrectly(){
        BinaryMetadataParser parser = new BinaryMetadataParser("{}");
        String id = parser.getId();
        assertThat(id).isNull();
    }

    @Test
    public void shouldParseLengthCorrectly(){
        BinaryMetadataParser parser = new BinaryMetadataParser("{\"length\":657}");
        Integer length = parser.getLength();
        assertThat(length).isEqualTo(new Integer(657));
    }

    @Test
    public void shouldParseNullLengthCorrectly(){
        BinaryMetadataParser parser = new BinaryMetadataParser("{}");
        Integer length = parser.getLength();
        assertThat(length).isNull();
    }

    @Test
    public void shouldParseWholeObjectCorrectly(){
        BinaryMetadataParser parser = new BinaryMetadataParser("{\"id\":\"63009\",\"lastUpdated\":\"2016-05-13T14:05:55.988+02:00\",\"name\":\"HelloWorld\",\"owner\":\"sysadmin\",\"self\":\"https://dev-d.cumulocity.com/inventory/binaries/63009\",\"type\":\"text/plain\",\"c8y_IsBinary\":\"\",\"length\":657,\"contentType\":\"application/octet-stream\"}");
        BinaryMetadata metadata = parser.parse();
        assertThat(metadata.getId()).isEqualTo("63009");
        assertThat(metadata.getName()).isEqualTo("HelloWorld");
        assertThat(metadata.getType()).isEqualTo("text/plain");
        assertThat(metadata.getContentType()).isEqualTo("application/octet-stream");
        assertThat(metadata.getPrimitiveLength()).isEqualTo(657);
    }
}