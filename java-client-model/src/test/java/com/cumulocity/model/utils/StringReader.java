package com.cumulocity.model.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.common.io.CharStreams;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StringReader {

    /**
     * TODO This implementation should be reviewed. It does not handle encoding explicitly
     * (UTF-8 should be used for all files), and should either read from file or classpath. 
     */
	public static String readFile(String filename) throws IOException {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			inputStream = StringReader.class.getClassLoader()
					.getResourceAsStream(filename);
		}
		Scanner scanner = new Scanner(inputStream);
		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}
		return text.toString();
	}
	
    public static String readResource(String resource) throws IOException {
        InputStreamReader stream = new InputStreamReader(StringReader.class.getResourceAsStream(resource), UTF_8.name());
        try {
            return CharStreams.toString(stream);
        } finally {
            stream.close();
        }
    }
}
