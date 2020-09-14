package com.jaeheonshim.assignmentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class User implements UserDetails {
    @Id
    private String id;

    private String name;
    private String emailAddress;
    private String password;

    private List<AssignmentClass> assignmentClasses = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

    public User() {
    }

    public User(String name, String emailAddress, String password) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<AssignmentClass> getAssignmentClasses() {
        return assignmentClasses;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
