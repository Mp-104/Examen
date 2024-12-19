package com.example.examen.model;

import com.example.examen.authorities.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL
            , fetch = FetchType.EAGER // This solved Large object auto-commit!!
    )
    private List<Personnel> personnelList;


    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialNonExpired;
    private boolean isEnabled;

    public CustomUser(String username, String password, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public CustomUser () {}


    public boolean getIsAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setIsAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean getIsAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setIsAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean getIsCredentialNonExpired() {
        return isCredentialNonExpired;
    }

    public void setIsCredentialNonExpired(boolean credentialNonExpired) {
        isCredentialNonExpired = credentialNonExpired;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<Personnel> getPersonnelList() {
        return personnelList;
    }

    public void setPersonnelList(List<Personnel> personnelList) {
        this.personnelList = personnelList;
    }

    public List<SimpleGrantedAuthority> getAuthorities () {
        return userRole.getAuthorities();
    }

    public List<String> getPermissions () {
        return userRole.getPermission();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
