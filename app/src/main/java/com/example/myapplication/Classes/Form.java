package com.example.myapplication.Classes;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Form implements Serializable {
    Map<String,String> formInformation;
    public Form(){
        formInformation = new HashMap<>();
    }
    public Form(String formInfo){
        this();
        for (String field: formInfo.split(",")){
            formInformation.put(field.trim(), null);
        }
    }

    // Setters //
    public void setFormField(String key){
        formInformation.put(key,null);
    }
    public void setFormInformation(Map<String, String> formInformation) {
        this.formInformation = formInformation;
    }
    public void setFormValue(String key, String value){
        formInformation.put(key,value);
    }
    // Getter //
    public Map getFormInformation(){ return formInformation; }

    public String removeFormField(String key){return formInformation.remove(key);}

    @Override
    public String toString(){

        String res = "";
        for (String data: formInformation.keySet()){
            res += data + ", ";
        }
        res = res.substring(0, res.length() - 2);
        return res;
    }


}
