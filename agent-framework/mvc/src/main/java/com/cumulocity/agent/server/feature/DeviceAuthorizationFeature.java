package com.cumulocity.agent.server.feature;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.context.AuthorizationHeaderDeviceCredentialsResolver;
import com.cumulocity.agent.server.context.ServletDeviceContextFilter;

@Configuration
@Import({ ServletDeviceContextFilter.class, AuthorizationHeaderDeviceCredentialsResolver.class })
public class DeviceAuthorizationFeature {

}
