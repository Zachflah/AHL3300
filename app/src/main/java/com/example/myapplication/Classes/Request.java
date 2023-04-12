package com.example.myapplication.Classes;

public class Request {
    private String branchName;
    private String serviceName;
    private String status;

    public Request(String branchName, String serviceName, String status) {
        this.branchName = branchName;
        this.serviceName = serviceName;
        this.status = status;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getStatus() {
        return status;
    }
}
