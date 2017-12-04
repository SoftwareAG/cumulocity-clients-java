package com.cumulocity.microservice.security.service;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

@Value
@Builder
public class SecurityUserDetails implements UserDetails {
    public static SecurityUserDetails activeUser(String tenant, String username, String password, String... userRoles) {
        return activeUser(tenant, username, password, newArrayList(userRoles));
    }

    public static SecurityUserDetails activeUser(String tenant, String username, String password, Iterable<String> userRoles) {
        return activeUser(userRoles)
                .tenant(tenant)
                .username(tenant + "/" + username)
                .password(password)
                .build();
    }

    public static SecurityUserDetails.SecurityUserDetailsBuilder activeUser(Iterable<String> userRoles) {
        final SecurityUserDetailsBuilder result = SecurityUserDetails.builder()
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true);
        for (final String userRole : userRoles) {
            result.authority(new SimpleGrantedAuthority(userRole));
        }
        return result;
    }

    @Singular private Collection<? extends GrantedAuthority> authorities;
    private String tenant;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
