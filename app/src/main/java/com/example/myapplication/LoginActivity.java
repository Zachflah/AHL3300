package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Classes.Admin;
import com.example.myapplication.Classes.Employee;
import com.example.myapplication.Classes.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity{

    private EditText userName, password;
    private Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //Sets up dropdown bar
        role = findViewById(R.id.roles);
        String[] items = new String[]{"Customer", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);

        //Checks user input
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().contains(" ")) {
                    userName.setError("No spaces allowed");
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
                if (password.getText().length() < 5) {
                    password.setError("Password must be <5 characters");
                } else if (password.getText().toString().contains(" ")) {
                    password.setError("No spaces allowed");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });
    }





    //----------------------------------- Login --------------------------------------//
    public void loginClick(View v){
        userName = findViewById(R.id.username);
        password=  findViewById(R.id.password);
        role = findViewById(R.id.roles);
        //Check the database

        String usernameString = userName.getText().toString();
        String passwordString = password.getText().toString();
        String roleString = role.getSelectedItem().toString();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Checks if login is by admin
                Bundle bundle = new Bundle();
                if(usernameString.equals("admin")){
                    if(checkAdminLogin(passwordString,snapshot)){
                        //Lets the user know that login was successful
                        Toast.makeText(getApplicationContext(),"Logged in as admin",Toast.LENGTH_LONG).show();

                        //Sends the user to the main page
                        //Also creates a admin object which is used to define the user
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        bundle.putSerializable("usertype",new Admin("admin","admin","Administrator"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        //Failed login message
                        Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                    }
                }
                //If not an admin login
                else if(checkLogin(usernameString,passwordString,roleString,snapshot)){
                    //Lets the user know that login was successful
                    Toast.makeText(getApplicationContext(),"Login Sucessful!",Toast.LENGTH_LONG).show();

                    //Sends the user to the main page
                    //Also creates and loads the user object accordingly based on the database information
                    Intent intent;
                    if(roleString.equals("Customer")) {
                        intent = new Intent(LoginActivity.this, CustomerActivity.class);
                        Customer customer = snapshot.child("users/"+usernameString).getValue(Customer.class);
                        bundle.putSerializable("usertype",customer);
//                        bundle.putSerializable("usertype",new Customer(usernameString,passwordString,roleString,snapshot.child("users").child(usernameString).child("email").getValue().toString()));
                    }else{ //Else Employee
                        intent = new Intent(LoginActivity.this, EmployeeMainActivity.class);
                        Employee employee = snapshot.child("users/"+usernameString).getValue(Employee.class);
                        bundle.putSerializable("usertype",employee);
                    }
                    intent.putExtras(bundle);
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

    //Checks if the login matches admin password
    // The username is checked prior to entering this method so username parameter is not taken
    public static boolean checkAdminLogin(String password, DataSnapshot snapshot){
        return snapshot.child("users").child("admin").child("password").getValue().toString().equals(password);
    }

    //Checks if user info matches database
    public static boolean checkLogin(String username,String password, String role, DataSnapshot snapshot){
        DataSnapshot snippet = snapshot.child("users");
        if(snippet.hasChild(username)){ //Checks if its in the DB first before attempting to parse as it would throw an exception
            snippet = snippet.child(username);
            if(snippet.child("password").getValue().toString().equals(password) && snippet.child("role").getValue().toString().equals(role)) {
                return true;
            }
        }
        return false;
    }

    //--------------------- register ---------------------//
    public void registerClick(View v){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        intent.putExtra("logged",0);
        startActivity(intent);
    }
}