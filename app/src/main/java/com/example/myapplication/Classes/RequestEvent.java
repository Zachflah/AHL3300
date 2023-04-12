package com.example.myapplication.Classes;

import java.util.EventObject;

public class RequestEvent extends EventObject {
    private String status;

    public RequestEvent(Object source,String status){
        super(source);
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
}
