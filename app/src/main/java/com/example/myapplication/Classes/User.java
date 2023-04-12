package com.example.myapplication.Classes;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String role;

    User(){}
    User(String username, String role){
        this.username = username;
        this.role = role;
    }
    User(String username,String password,String email, String role){
        this.username =username;
        this.password =password;
        this.email = email;
        this.role = role;
    }
    User(String username,String password, String role){
        this.username =username;
        this.password =password;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

}
