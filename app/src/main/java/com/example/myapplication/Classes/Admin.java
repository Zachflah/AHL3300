package com.example.myapplication.Classes;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;
//    private ArrayList<Service> services;

    public Admin(String username,String password,String role) {
        super(username, password, role);
    }
}
