package com.cumulocity.sdk.client.common;

import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class UriMatcher implements ArgumentMatcher<String> {

    private final String expectedUrlString;

    @Override
    public boolean matches(String providedUrlString) {
        UriComponents providedUri = UriComponentsBuilder.fromUriString(providedUrlString).build();
        UriComponents expectedUri = UriComponentsBuilder.fromUriString(expectedUrlString).build();
        return providedUri.equals(expectedUri);
    }
}
