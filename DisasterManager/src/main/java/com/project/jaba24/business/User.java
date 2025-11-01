package com.project.jaba24.business;

public class User {
    private long code;
    private String username;
    private String email;
    private String password;
    private String location;

    public User() {}

    public User(String username, String email, String password, String location) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
