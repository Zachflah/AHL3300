package com.example.myapplication.Classes;

public class BasicAccount {
    private String username;
    private String role;
    public BasicAccount() {}
    public BasicAccount(String username, String role){
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
