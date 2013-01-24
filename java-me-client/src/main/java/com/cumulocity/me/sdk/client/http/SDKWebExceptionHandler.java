package com.cumulocity.me.sdk.client.http;

import com.cumulocity.me.http.WebException;
import com.cumulocity.me.http.WebExceptionHandler;
import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.sdk.SDKException;

public class SDKWebExceptionHandler implements WebExceptionHandler {

    public WebResponse handle(WebException exception) {
        throw new SDKException(exception.getResponse().getStatus(), exception.getMessage());
    }

}
