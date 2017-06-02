package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CumulocityHttpClient extends ApacheHttpClient4 {

    private final Pattern hostPattern = Pattern.compile("((http|https):\\/\\/.+?)(\\/|\\?|$)");

    private PlatformParameters platformParameters;

    CumulocityHttpClient(ApacheHttpClient4Handler createDefaultClientHander, ClientConfig cc, IoCComponentProviderFactory provider) {
        super(createDefaultClientHander, cc, provider);
    }

    @Override
    public WebResource resource(String path) {
        return super.resource(resolvePath(path));
    }

    protected String resolvePath(String path) {
        return platformParameters.isForceInitialHost() ? insertInitialHost(path) : path;
    }

    public void setPlatformParameters(PlatformParameters platformParameters) {
        this.platformParameters = platformParameters;
    }

    private String insertInitialHost(String path) {
        Matcher matcher = hostPattern.matcher(path);
        if (matcher.find()) {
            String capturedHost = matcher.group(1);
            return path.replace(capturedHost, getInitialHost());
        }
        return path;
    }

    private String getInitialHost() {
        String initialHost = platformParameters.getHost();
        if (initialHost.endsWith("/")) {
            initialHost = initialHost.substring(0, initialHost.length() - 1);
        }
        return initialHost;
    }

}
