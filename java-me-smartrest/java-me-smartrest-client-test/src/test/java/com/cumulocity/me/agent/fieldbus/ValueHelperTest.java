package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.fieldbus.impl.RegisterDefinitionBuilder;
import com.cumulocity.me.agent.fieldbus.model.Conversion;
import com.cumulocity.me.agent.fieldbus.model.RegisterDefinition;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class ValueHelperTest {

    @Test
    public void shouldConvertArrayCorrectly() throws Exception {
        RegisterDefinition registerDefinition = new RegisterDefinitionBuilder().withStartBit(0).withLength(24).withConversion(new Conversion(2, 3, 4, 5)).build();
        String converted = ValueHelper.convert(new byte[]{(byte) 0xFF, 0x00, (byte) 0xAA}, registerDefinition);
        assertThat(converted).isEqualTo("111.41237");
    }

    @Test
    public void shouldCutCorrectly() throws Exception {
        byte[] cutBytes = ValueHelper.cut(new byte[]{(byte) 0xFF, 0x00, (byte) 0xAA}, 4, 14);
        assertThat(cutBytes.length).isEqualTo(2);
        assertThat(cutBytes[0]).isEqualTo((byte) 0x3C);
        assertThat(cutBytes[1]).isEqualTo((byte) 0x02);
    }

    @Test
    public void shouldConvertLongCorrectly() throws Exception {
        String converted = ValueHelper.convert(1234L, new Conversion(2, 3, 4, 5));
        assertThat(converted).isEqualTo("0.00826");
    }

    @Test
    public void shouldMergeCorrectly() throws Exception {
        RegisterDefinition definition = new RegisterDefinitionBuilder().withStartBit(12).withLength(17).build();
        byte[] merged = ValueHelper.merge("-1", new byte[8], definition);
        assertThat(merged[1]).isEqualTo((byte) 0b00001111);
        assertThat(merged[2]).isEqualTo((byte) 0b11111111);
        assertThat(merged[3]).isEqualTo((byte) 0b11111000);
        for (int i = 0; i < merged.length; i++) {
            byte current = merged[i];
            if (i == 0 || i > 3){
                assertThat(current).isEqualTo((byte) 0);
            }
        }
    }

    @Test
    public void shouldMergeFullSizeCorrectly() throws Exception {
        RegisterDefinition definition = new RegisterDefinitionBuilder().withStartBit(0).withLength(64).build();
        byte[] merged = ValueHelper.merge("-1", new byte[8], definition);
        for (byte b : merged) {
            assertThat(b).isEqualTo((byte) 0xFF);
        }
    }

    @Test
    public void shouldConvertBackwardsCorrectly(){
        long converted = ValueHelper.convertBackwards("12345.6789", new Conversion(2, 3, 4, 5));
        assertThat(converted).isEqualTo(1851851829L);
    }

    @Test
    public void shouldConvertBackwardsWithoutDecimalPointCorrectly(){
        long converted = ValueHelper.convertBackwards("12345", new Conversion(2, 3, 4, 5));
        assertThat(converted).isEqualTo(1851749994L);
    }

    @Test
    public void shouldConvertBackwardAndForthCorrectly(){
        Conversion conversion = new Conversion(2, 3, 4, 5);
        long converted = ValueHelper.convertBackwards("12345.6789", conversion);
        String convertedAgain = ValueHelper.convert(converted, conversion);
        assertThat(convertedAgain).isEqualTo("12345.67890");
    }
}