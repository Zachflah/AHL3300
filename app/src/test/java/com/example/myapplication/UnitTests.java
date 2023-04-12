package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;


import com.example.myapplication.Classes.Form;
import com.example.myapplication.Util.InputVerification;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    //tests for deliverable 4 further down
    @Test
    public void invalidEmailType(){
        boolean actual = InputVerification.checkEmail("notvalid.@.@mail.com");
        assertFalse(actual);
    }
    @Test
    public void validEmailType(){
        boolean actual = InputVerification.checkEmail("valid@somemail.com");
        assertTrue(actual);
    }

    @Test
    public void invalidPasswordTest() {
        //Doesn't accept password shorter than 5 characters
        boolean actual = InputVerification.checkPassword("123");
        assertFalse(actual);
    }
    @Test
    public void validPasswordTest() {
        boolean actual = InputVerification.checkPassword("12345");
        assertTrue(actual);
    }
    @Test
    public void invalidUsername(){
        //Doesn't accept spaces in username
        boolean actual = InputVerification.checkUsername("in valid");
        assertFalse(actual);
    }
    @Test
    public void validFormInput(){
        boolean actual = InputVerification.checkUsername("valid");
        assertTrue(actual);
    }
    @Test
    public void validUsername(){
        boolean expected = true;
        boolean actual = InputVerification.checkUsername("valid");
        assertEquals(expected,actual);
    }
    //These are the tests for deliverable 4
    @Test
    public void validUserFormInput(){
        Form userForm = new Form();
        HashMap<String,String> formFields = new HashMap<>();
        String fields = "Firstname, Lastname, Email, Address, Date of Birth";
        for (String field: fields.split(",")){
            formFields.put(field, "any non-null or non-empty string is valid since every form is reviewed by a employee");
        }
        userForm.setFormInformation(formFields);
        boolean actual = InputVerification.isValidForm(userForm);
        assertTrue(actual);
    }
    @Test
    public void invalidUserFormInput(){
        Form userForm = new Form();
        HashMap<String,String> formFields = new HashMap<>();
        String fields = "Firstname, Lastname, Email, Address, Date of Birth";
        for (String field: fields.split(",")){
            formFields.put(field, "any non-null or non-empty string is valid since every form is reviewed by a employee");
        }
        formFields.put("Last Field", "");
        userForm.setFormInformation(formFields);
        boolean actual = InputVerification.isValidForm(userForm);
        assertFalse(actual);
    }
    @Test
    public void validReviewTitle(){
        boolean actual = InputVerification.isValidReviewTitle("Car Rental");
         //the input is a spinner that takes its values from the db so the only invalid input is the default message
        assertTrue(actual);
    }
    @Test
    public void invalidReviewTitle(){
        boolean actual = InputVerification.isValidReviewTitle("Please Select a Service");
        assertFalse(actual);
    }
    @Test
    public void validReviewMessage(){
        boolean actual = InputVerification.isValidReviewMessage("this is a valid review message");
        assertTrue(actual);
    }
    @Test
    public void invalidReviewMessage(){
        boolean actual = InputVerification.isValidReviewMessage(""); //this is an invalid review message
        assertFalse(actual);
    }
    public void validCustomerAddress(){
        ArrayList<String> validAddresses = new ArrayList<>();
        //Simulating the information from the DB
        validAddresses.add("10 Calgary St");
        validAddresses.add("1234 Montreal St");
        validAddresses.add("10 Somerset");
        boolean actual  = InputVerification.checkCustomerAddress("10 Somerset",validAddresses);
        assertTrue(actual);
    }

    public void invalidCustomerAddress(){
        ArrayList<String> validAddresses = new ArrayList<>();
        validAddresses.add("10 Calgary St");
        validAddresses.add("1234 Montreal St");
        validAddresses.add("10 Somerset");
        boolean actual  = InputVerification.checkCustomerAddress("Not in DB",validAddresses);
        assertFalse(actual);
    }
    public void validCustomerService(){
        ArrayList<String> validServices = new ArrayList<>();
        //Simulating the information retrieved from DB
        validServices.add("Truck Rental");
        validServices.add("Moving Assistance");
        validServices.add("Tool Rental");
        boolean actual = InputVerification.checkCustomerServices("Tool Rental", validServices);
        assertTrue(actual);
    }
    public void invalidCustomerService(){
        ArrayList<String> validServices = new ArrayList<>();
        validServices.add("Truck Rental");
        validServices.add("Moving Assistance");
        validServices.add("Tool Rental");
        boolean actual = InputVerification.checkCustomerServices("Not Rental", validServices);
        assertFalse(actual);
    }
}