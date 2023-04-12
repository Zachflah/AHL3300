package com.example.myapplication.Classes;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Customer(){}
    public Customer(String username, String password, String roles, String email){
        super(username,password, email,roles);
    }

}
