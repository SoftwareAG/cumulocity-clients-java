package com.cumulocity.microservice.security.service;

import com.cumulocity.sdk.client.PlatformParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.beans.ConstructorProperties;

@Repository
public class SecurityUserDetailsService implements UserDetailsService {

    private final PlatformParameters platformParameters;
    private final RoleService roleService;

    @Autowired
    @ConstructorProperties({"userPlatform", "roleService"})
    public SecurityUserDetailsService(PlatformParameters platformParameters, RoleService roleService) {
        this.platformParameters = platformParameters;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return SecurityUserDetails.activeUser(roleService.getUserRoles())
                .username(platformParameters.getUser())
                .password(platformParameters.getPassword())
                .oAuthAccessToken(platformParameters.getOAuthAccessToken())
                .tenant(platformParameters.getTenantId())
                .build();
    }
}
