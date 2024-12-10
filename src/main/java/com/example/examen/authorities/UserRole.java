package com.example.examen.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.example.examen.authorities.UserPermission.*;

public enum UserRole {
    GUEST(List.of(GET.getPermission())),
    USER(List.of(GET.getPermission(), POST.getPermission(), PUT.getPermission())),
    ADMIN(List.of(GET.getPermission(), POST.getPermission(), PUT.getPermission(), DELETE.getPermission()));

    private final List<String> permission;

    UserRole (List<String> permission) {
        this.permission = permission;
    }

    public List<String> getPermission () {
        return permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities () {

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        simpleGrantedAuthorityList.addAll(getPermission().stream().map(SimpleGrantedAuthority::new).toList());

        return simpleGrantedAuthorityList;

    }
}
