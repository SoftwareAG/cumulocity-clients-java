package com.cumulocity.rest.representation.mongo.queryplanner;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import java.io.*;
import java.util.Objects;


public class QueryPlanLoader {

    @SneakyThrows
    public static String getSampleQueryPlanner() {
        val classLoader = QueryPlanLoader.class.getClassLoader();
        val inputStream = Objects.requireNonNull(classLoader.getResourceAsStream("sample_query_plan.json"));
        @Cleanup val input = new BufferedInputStream(inputStream);

        val json = new StringBuilder();
        for (int i = input.read(); i != -1; i = input.read()) {
            json.append((char) i);
        }
        return json.toString();
    }
}
