package com.example.myapplication.Util;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.myapplication.Classes.Form;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputVerification {




    public static boolean checkUsername(String username){
        //Cannot contain spaces
        if(username.contains(" ")||username.length()==0){
            return false;
        }
        return true;
    }
    public static boolean checkPassword(String password){
        //Password must be greater than 5 chars
        if(password.length()<5){
            return false;
        }
        return true;
    }

    public static boolean checkEmail(String email){
        if(email.length()==0||!validate(email)){
            return false;
        }
        return true;
    }

    public static boolean checkAddress(String address){
        Pattern VALID_ADDRESS = Pattern.compile("([0-9])*(.*[a-zA-Z])");
        Matcher matcher = VALID_ADDRESS.matcher(address);
        return matcher.find();
    }
    public static boolean checkCustomerAddress(String address, ArrayList<String> valid) {
        if (valid.contains(address)) return true;
        return false;
    }
    public static boolean checkPhoneNumber(String number) {
        Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("(^[0-9]+(-)+[0-9]{3}+(-)+[0-9]{3}+(-)+[0-9]{4}$)|(^[0-9]{3}+(-)+[0-9]{3}+(-)+([0-9]{4}$))");
//        Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("(^[0-9]+(-)+[0-9]{3}+(-)+[0-9]{3}+(-)+[0-9]{4})|([0-9]{10})|([0-9]{11})");
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(number);
        return (matcher.find());
    }
    public static boolean checkCustomerServices(String service, ArrayList<String> valid){
        if(valid.contains(service))return true;
        return false;
    }

    //***************************************************************
    //Code from https://stackoverflow.com/questions/8204680/java-regex-email
    // By Jason Buberel
    // This is used to verify email inputs
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //********************************************************************

    public static boolean isValidForm(Form form){
        for (Object value: form.getFormInformation().values()) {
            if (value == null || value.equals("")) {
                return false;
            }
        }
        return true;

    }
    public static boolean isValidReviewTitle(String serviceName){
        return !serviceName.equals("Please Select a Service");
    }
    public static boolean isValidReviewMessage(String message){
        return !message.equals("");
    }

}
