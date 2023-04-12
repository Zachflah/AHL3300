package com.example.myapplication.Classes;

import android.widget.CheckBox;

import java.util.EventObject;
import java.util.Observable;

//https://stackoverflow.com/questions/1658702/how-do-i-make-a-class-extend-observable-when-it-has-extended-another-class-too
public class CheckBoxEvent extends EventObject {
    String box;
    boolean state;
    public CheckBoxEvent(Object source, String box, boolean isPressed){
        super(source);
        this.box = box;
        state = isPressed;
    }

    public String getBox() {
        return box;
    }

    public boolean isPressed(){
        return state;
    }
}
