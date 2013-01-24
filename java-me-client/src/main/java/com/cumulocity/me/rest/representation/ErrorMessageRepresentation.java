package com.cumulocity.me.rest.representation;

//import org.svenson.JSONProperty;

public class ErrorMessageRepresentation implements CumulocityResourceRepresentation {

    // Application level error code 
    private String error;
    // Short text description of the error 
    private String message;
    
    private String info; //   1   URL to error description on the Internet.
    
    // details     JSON-Object with ErrorDetails. Only available if in DEBUG mode.
    private ErrorDetails details;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    //@JSONProperty(ignoreIfNull = true)
    public ErrorDetails getDetails() {
        return details;
    }

    public void setDetails(ErrorDetails details) {
        this.details = details;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        sb.append("error=").append('"').append(error).append('"').append(',');
        sb.append("message=").append('"').append(message).append('"').append(',');
        sb.append("info=").append('"').append(info).append('"').append(',');
        sb.append("details=").append('"').append(details).append('"');
        sb.append('}');
        return sb.toString();
    }
}
