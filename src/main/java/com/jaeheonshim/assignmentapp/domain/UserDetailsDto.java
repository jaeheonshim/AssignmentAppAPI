package com.jaeheonshim.assignmentapp.domain;

public class UserDetailsDto {
    private String name;
    private String emailAddress;
    private String password;
    private Boolean accountEnabled;

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isAccountEnabled() {
        return accountEnabled;
    }
}
