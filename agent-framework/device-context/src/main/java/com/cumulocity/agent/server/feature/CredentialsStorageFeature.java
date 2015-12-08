package com.cumulocity.agent.server.feature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cumulocity.agent.server.context.BootstrapCredentialsStorageService;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;

@Configuration
public class CredentialsStorageFeature {

    @Bean
    @Autowired
    public BootstrapCredentialsStorageService bootstrapCredentialsStorageService(
            @Value("${C8Y.devicebootstrap.tenant}") String bootstrapTenant,
            @Value("${C8Y.devicebootstrap.user}") String bootstrapUser,
            @Value("${C8Y.devicebootstrap.password}") String bootstrapPassword,
            @Value("${C8Y.baseURL}") String c8yBaseUrl,
            @Value("${C8Y.credentials.file.path}") String filePath ) {
        CumulocityCredentials credentials = new CumulocityCredentials(bootstrapTenant, bootstrapUser,
                bootstrapPassword, null);
        Platform bootstrapPlatform = new PlatformImpl(c8yBaseUrl, credentials);
        return new BootstrapCredentialsStorageService(bootstrapPlatform.getDeviceCredentialsApi(), filePath);
    }


}
