package com.cumulocity.model.utils;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StringReader {

    public static String readResource(String resource) throws IOException {
        InputStreamReader stream = new InputStreamReader(StringReader.class.getResourceAsStream(resource), UTF_8.name());
        try {
            return CharStreams.toString(stream);
        } finally {
            stream.close();
        }
    }
}
