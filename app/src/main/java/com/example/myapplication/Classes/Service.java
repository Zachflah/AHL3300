package com.example.myapplication.Classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class Service implements Serializable{
    private String serviceTitle;
    private boolean visibility = false;
    private Form form;
    private String formString;

    //    Branch
    float hourlyRate;

    public Service(){};
    public Service(String serviceTitle,boolean visibility){
        this.serviceTitle = serviceTitle;
        this.visibility = visibility;
    }
    public Service(String serviceTitle,float hourlyRate,String formString){
        this.serviceTitle = serviceTitle;
        this.hourlyRate = hourlyRate;
        this.formString = formString;
    }
    public Service(String title, Form form, float hourlyRate){
        this.serviceTitle = title;
        this.form = form;
        this.hourlyRate = hourlyRate;
    }

    // ------ Getters -----//
    public String getServiceTitle(){
        return  serviceTitle;
    }
    public Form getForm(){return form;}
    public float getHourlyRate(){
        return hourlyRate;
    }
    public String getFormString() { return formString;    }
    /**
     * Used to define visibility for the branch
     * @return true if visible
     */
    public boolean getVisibility(){
        return visibility;
    }



    //----- Setters -----//
    public void setVisibility(boolean b){ visibility = b; }
    public void setHourlyRate(float r) { hourlyRate = r; }
    public void setForm(Form form){
        this.form = form;
    }
    public void setServiceTitle(String title){serviceTitle = title;}
    public void setFormString(String formString) {
        this.formString = formString;
        this.form = new Form(formString);

    }
}
