package com.cumulocity.microservice.security.service;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.PlatformParameters;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.beans.ConstructorProperties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Repository
public class SecurityUserDetailsService implements UserDetailsService {

    private final PlatformParameters platformParameters;
    private final RoleService roleService;
    private final Cache<CumulocityCredentials, UserDetails> userDetails = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors() * 2)
            .expireAfterWrite(10, SECONDS)
            .build();

    @Autowired
    @ConstructorProperties({"userPlatform", "roleService"})
    public SecurityUserDetailsService(PlatformParameters platformParameters, RoleService roleService) {
        this.platformParameters = platformParameters;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final CumulocityCredentials credentials = platformParameters.getCumulocityCredentials();
        try {
            return userDetails.get(credentials, new Callable<UserDetails>() {
                @Override
                public UserDetails call() {
                    return SecurityUserDetails.activeUser(roleService.getUserRoles())
                            .credentials(credentials)
                            .build();
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
