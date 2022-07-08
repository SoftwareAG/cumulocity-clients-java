package org.svenson;

public class TestJsonObject extends BaseJsonObject {


    enum Value{
        ANY
    }

    private Value value;

    private String text;

    @JSONProperty(ignoreIfNull = true)
    public Value getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Value.valueOf(value);
    }

    public void setValue(Value value) {
        this.value = value;
    }
    @JSONProperty(ignoreIfNull = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
