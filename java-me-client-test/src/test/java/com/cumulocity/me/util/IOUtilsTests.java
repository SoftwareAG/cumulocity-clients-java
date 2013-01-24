package com.cumulocity.me.util;

import static com.cumulocity.me.model.CumulocityCharset.CHARSET;
import static java.util.Collections.nCopies;
import static org.apache.commons.lang.StringUtils.join;
import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilsTests {

    @Test
    public void shouldReadData() throws Exception {
        String input = join(nCopies(151, "tdrhgsadafsafgsgfsadsdfhsdgasgashgsdgfasdgshgsdg"), "");
        byte[] inputData = input.getBytes(CHARSET);
        int inputLength = inputData.length;
        InputStream inputStream = new ByteArrayInputStream(inputData);
        
        byte[] output = IOUtils.readData(inputLength, inputStream);
        
        assertThat(new String(output, CHARSET)).isEqualTo(input);
    }
    
    @Test
    public void shouldWriteData() throws Exception {
        String input = join(nCopies(151, "tdrhgsadafsafgsgfsadsdfhsdgasgashgsdgfasdgshgsdg"), "");
        byte[] inputData = input.getBytes(CHARSET);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        IOUtils.writeData(inputData, outputStream);
        
        assertThat(outputStream.size()).isEqualTo(inputData.length);
        assertThat(outputStream.toString(CHARSET)).isEqualTo(input);
    }
}