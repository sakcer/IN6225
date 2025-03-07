package com.in6225.project.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserLoginDetails implements UserDetails {
    private String token;
    private String employeeId;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor
    public UserLoginDetails(String employeeId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.employeeId = employeeId;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return employeeId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // User is enabled
    }
}
