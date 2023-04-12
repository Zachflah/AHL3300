package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Util.InputVerification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, email, password;
    private Button register;
    private Spinner role, branch;
    private boolean flag;
    ArrayList<String> branches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);


        register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        role = findViewById(R.id.roles);
        branch = findViewById(R.id.branch_selection);


        //----------------------------------- Sets drop list for user roles and branches ------------------------------------//
        String[] items = new String[]{"Customer", "Employee"};
        branches = new ArrayList<>();
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, branches);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        role.setAdapter(adapter);
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(role.getSelectedItem().toString().equals("Employee")){
                    branch.setAdapter(branchAdapter);
                }else{
                    String[] emptyList = new String[]{};
                    branch.setAdapter(new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, emptyList));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //----------------------------------- Checks inputs ------------------------------------//
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!InputVerification.checkUsername(username.getText().toString())){
                    username.setError("No spaces allowed");
                    flag = true;
                }else{
                    flag = false;
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().length()<5){
                    password.setError("Password must be <5 characters");

                }
                else if(password.getText().toString().contains(" ")){
                    password.setError("No spaces allowed");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
                if(!InputVerification.checkEmail(email.getText().toString())) {
                    email.setError("Invalid email");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //---------------------------------------------------------------------------------------//
    }

    @Override
    public void onStart(){
        super.onStart();
        // ----------------- Gets the branch list from database -----------------//
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Branches");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot branch: snapshot.getChildren()){
                    branches.add(branch.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * onClick event for the register button. Checks the DB if a user with that username exists in the username before registering
     * @param v - view object for the button
     */
    public void registerClick(View v) {
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String emailString = email.getText().toString();

        //Checks if user input is valid
        if (InputVerification.checkEmail(emailString) && InputVerification.checkUsername(usernameString) && InputVerification.checkPassword(passwordString)) {
            //Need to check if already in database
            checkIfUserExistsAndAdd();
        }
    }

    /**
     * Checks if the user exists in the DB and if not, adds them into the DB. Called by registerClick
     */
    private void checkIfUserExistsAndAdd() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snippet = snapshot.child("users");
                if (!snippet.hasChild(username.getText().toString())) {
                    DatabaseReference newUserRole = db.getReference("users/" + username.getText().toString() + "/role");
                    DatabaseReference newUserEmail = db.getReference("users/" + username.getText().toString() + "/email");
                    DatabaseReference newUserName = db.getReference("users/" + username.getText().toString() + "/username");
                    DatabaseReference newUserPassword = db.getReference("users/" + username.getText().toString() + "/password");
                    DatabaseReference newBranch = db.getReference("users/" + username.getText().toString() + "/branch");

                    //Checks if the registration is by employee and if so, attaches branch association to DB
                    if(role.getSelectedItem().toString().equals("Employee")){
                        newBranch.setValue(branch.getSelectedItem().toString());
                    }
                    newUserName.setValue(username.getText().toString());
                    newUserRole.setValue(role.getSelectedItem().toString());
                    newUserEmail.setValue(email.getText().toString());
                    newUserPassword.setValue(password.getText().toString());

                    Toast.makeText(getBaseContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(),"Registration Failed. Username taken",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}