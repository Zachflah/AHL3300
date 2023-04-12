package com.example.myapplication.Classes;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    String branch;

    public Employee(){}
    public Employee(String branch, String email, String password, String role,String username){
        super(username,password,email,role);
        this.branch = branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }
}
