package com.cumulocity.sms.client.model;

import org.svenson.JSONProperty;

public class TextMessage {

    public static final int LIMIT = 160;

    private String text;

    public TextMessage() {
    }

    public TextMessage(String text) {
        this.text = text;
    }

    @JSONProperty("message")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JSONProperty(ignore = true)
    public boolean isLimitExceeded() {
        return this.text.length() > LIMIT;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TextMessage other = (TextMessage) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TextMessage [text=" + text + "]";
    }

}
