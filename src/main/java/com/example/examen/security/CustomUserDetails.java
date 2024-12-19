package com.example.examen.security;

import com.example.examen.model.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialNonExpired;
    private final boolean isEnabled;

    public CustomUserDetails(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean isAccountNonExpired,
                             boolean isAccountNonLocked,
                             boolean isCredentialNonExpired,
                             boolean isEnabled
    ) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialNonExpired = isCredentialNonExpired;
        this.isEnabled = isEnabled;
    }

    public CustomUserDetails (CustomUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.isAccountNonExpired = user.getIsAccountNonExpired();
        this.isAccountNonLocked = user.getIsAccountNonLocked();
        this.isCredentialNonExpired = user.getIsCredentialNonExpired();
        this.isEnabled = user.getIsEnabled();
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
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return isAccountNonExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return isAccountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return isCredentialNonExpired;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }
}
