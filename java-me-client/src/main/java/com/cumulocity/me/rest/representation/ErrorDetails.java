package com.cumulocity.me.rest.representation;

public class ErrorDetails {
    //  Exception message content.
    private String exceptionMessage;

    //    Class name of an exception that caused this error.
    private String expectionClass;

    private String expectionStackTrace;

    public String getExpectionClass() {
        return expectionClass;
    }

    public void setExpectionClass(String expectionClass) {
        this.expectionClass = expectionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExpectionStackTrace() {
        return expectionStackTrace;
    }

    public void setExpectionStackTrace(String expectionStackTrace) {
        this.expectionStackTrace = expectionStackTrace;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        sb.append("exceptionMessage=").append('"').append(exceptionMessage).append('"').append(',');
        sb.append("expectionClass=").append('"').append(expectionClass).append('"').append(',');
        sb.append("expectionStackTrace=").append('"').append(expectionStackTrace).append('"');
        sb.append('}');
        return sb.toString();
    }
}
