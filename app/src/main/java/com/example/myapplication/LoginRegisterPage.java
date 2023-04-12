package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginRegisterPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private Context mContext;
    private EditText userName, email,password;
    private Button login, register;
    private Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_page);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);


        //Sets up dropdown bar
        role = findViewById(R.id.roles);
        String[] items = new String[]{"User", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);
    }


    public void registerClick(){
        //Checks if user input is valid
        if(checkEmail()&&checkUsername()&&checkPassword()){
            //Need to check if already in database
        }
        else{
            //find and highlight the invalid inputs for the user to change
        }
    }

    public void loginClick(View v){
        userName = findViewById(R.id.username);
        password=  findViewById(R.id.password);
        role = findViewById(R.id.roles);
        //Check the database

        String usernameString = userName.getText().toString();
        String passwordString = password.getText().toString();
        String roleString = role.getSelectedItem().toString();
//        User user = new User; Havent set up user class tyet
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Checks if login is by admin
                if(usernameString.equals("admin")){
                    if(snapshot.child("users").child("admin").child("password").getValue().toString().equals(passwordString)){
                        //Lets the user know that login was successful
                        Toast.makeText(getApplicationContext(),"Login Sucessful!",Toast.LENGTH_LONG).show();

                        //Sends the user to the main page
                        Intent intent = new Intent(LoginRegisterPage.this,AdminActivity.class);
                        startActivity(intent);
                    }
                }
                else if(checkLogin(usernameString,passwordString,roleString,snapshot)){
                    //Lets the user know that login was successful
                    Toast.makeText(getApplicationContext(),"Login Sucessful!",Toast.LENGTH_LONG).show();

                    //Sends the user to the main page
                    Intent intent = new Intent(LoginRegisterPage.this,MainActivity.class);
                    startActivity(intent);

                }else{
                    //Failed login message
                    Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Checks if user
    public boolean checkLogin(String username,String password, String role, DataSnapshot snapshot){
        DataSnapshot snippet = snapshot.child("users");
        if(snippet.hasChild(username)){
            try {
                for(DataSnapshot snap: snippet.getChildren()) {
                    if (snap.child("password").getValue().toString().equals(password) &&snap.child("role").getValue().toString().equals(role)) {
                        Log.d("report", "success");
                        return true;
                    }
                }
            }catch(NullPointerException e){
            }
        }
        return false;
    }

    //-----------------------------------------------Utils-----------------------------------------------//
    private boolean checkUsername(){
        userName = findViewById(R.id.username);
        String user = userName.getText().toString();
        //Cannot contain spaces
        if(user.contains(" ")){
            return false;
        }
        return true;
    }
    private boolean checkPassword(){
        password=  findViewById(R.id.password);
        String word = password.getText().toString();
        //Password must be greater than 5 chars
        if(word.length()<=5){
            return false;
        }
        return true;
    }

    private boolean checkEmail(){
        email = findViewById(R.id.email);
        String mail = email.getText().toString();
        if(!validate(mail)){
            return false;
        }
        return true;
    }

    //***************************************************************
    //Code from https://stackoverflow.com/questions/8204680/java-regex-email
    // By Jason Buberel
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    //********************************************************************


}