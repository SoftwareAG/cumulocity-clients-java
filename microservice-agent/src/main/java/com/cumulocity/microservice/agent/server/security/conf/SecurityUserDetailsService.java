package com.cumulocity.microservice.agent.server.security.conf;

import com.cumulocity.microservice.agent.server.security.service.RoleService;
import com.cumulocity.sdk.client.PlatformParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityUserDetailsService implements UserDetailsService {

    private final PlatformParameters platformParameters;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return SecurityUserDetails.activeUser(roleService.getUserRoles())
                .username(platformParameters.getUser())
                .password(platformParameters.getPassword())
                .build();
    }
}
