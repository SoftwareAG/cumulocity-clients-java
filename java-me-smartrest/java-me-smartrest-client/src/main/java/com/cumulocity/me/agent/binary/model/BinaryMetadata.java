package com.cumulocity.me.agent.binary.model;

public class BinaryMetadata {
    private String id;
    private String type;
    private Integer length;
    private String contentType;
    private String name;

    public BinaryMetadata() {
    }

    public BinaryMetadata(String id) {
        this.id = id;
    }

    public BinaryMetadata(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrimitiveLength() {
        if (id == null) {
            return -1;
        }
        return length.intValue();
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = new Integer(length);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
