package com.cumulocity.me.agent.binary.impl;

import com.cumulocity.me.agent.binary.model.BinaryMetadata;
import com.cumulocity.me.util.StringUtils;

public class BinaryMetadataParser {
    private final String toParse;
    private final String[] lines;

    private static String normalizeInput(String input){
        String normalized = input.trim();
        if (normalized.startsWith("{")){
            normalized = normalized.substring(1, normalized.length() - 1);
        }
        return normalized;
    }

    public BinaryMetadataParser(String toParse) {
        this.toParse = normalizeInput(toParse);
        lines = StringUtils.splitQuoted(this.toParse, ",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }
    }

    public BinaryMetadata parse(){
        BinaryMetadata result = new BinaryMetadata();
        result.setId(getId());
        result.setName(getName());
        result.setContentType(getContentType());
        result.setType(getType());
        result.setLength(getLength());
        return result;
    }

    public String getId(){
        return getPropertyValue("id");
    }

    public String getName(){
        return getPropertyValue("name");
    }

    public String getContentType(){
        return getPropertyValue("contentType");
    }

    public String getType(){
        return getPropertyValue("type");
    }

    public Integer getLength(){
        String value = getPropertyValue("length");
        if (value == null) {
            return null;
        }
        return new Integer(Integer.parseInt(value));
    }

    private String getPropertyValue(String key){
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("\"" + key + "\"")){
                String value = line.substring(line.indexOf(':') + 1);
                value = value.trim();
                if (value.startsWith("\"")){
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        }
        return null;
    }
}
