package com.jaeheonshim.assignmentapp.domain;

public class NewUserDao {
    private String name;
    private String email;
    private String password;

    private String recaptcha;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRecaptcha() {
        return recaptcha;
    }
}
