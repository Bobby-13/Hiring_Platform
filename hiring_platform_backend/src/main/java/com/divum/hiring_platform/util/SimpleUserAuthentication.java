package com.divum.hiring_platform.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SimpleUserAuthentication implements Authentication {

    private boolean isAuthenticated;
    private final Object principal;
    private final Object credentials;
    private final Collection<? extends GrantedAuthority> authorities;

    public SimpleUserAuthentication(boolean isAuthenticated, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        this.isAuthenticated = isAuthenticated;
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.toString();
    }
}
