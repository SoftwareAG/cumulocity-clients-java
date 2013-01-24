package com.cumulocity.me.http;

public interface WebExceptionHandler {

    WebResponse handle(WebException exception);
}
