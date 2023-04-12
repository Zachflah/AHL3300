package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.Classes.Customer;
import com.example.myapplication.Classes.EmployeeFormAdapter;
import com.example.myapplication.Classes.Form;
import com.example.myapplication.Classes.RequestEvent;
import com.example.myapplication.Classes.RequestListener;
import com.example.myapplication.Classes.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {
    private ListView list;
    private ArrayList<Form> requests = new ArrayList<>();
    private String branchName;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        branchName = getIntent().getStringExtra("branchName");

        list = findViewById(R.id.service_request_list);
        user = (Customer) getIntent().getSerializableExtra("usertype");

        getRequests(branchName);
    }

    /**
     * Gets requests form from database and creates populates the list along with getting a list of Form objects which is used by the dialog
     *  to display form in detail
     * @param branchName
     */
    private void getRequests(String branchName){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/"+branchName);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("service forms").exists()) {

                    snapshot.child("service forms").getChildren().forEach(form -> {
                        HashMap<String, String> formInfo = new HashMap<>();
                        //create form objects containing the form inforation which can be used later on for the dialog
                        form.getChildren().forEach(users->{
                            if(users.child("Status").getValue(String.class).equalsIgnoreCase("pending")) {
                                users.getChildren().forEach(info -> {
                                    formInfo.put(info.getKey(), info.getValue().toString());
                                });
                            }
                        });
                        if(!formInfo.isEmpty()) {
                            Form f = new Form();
                            f.setFormInformation(formInfo);
                            requests.add(f);
                        }
                    });
                    EmployeeFormAdapter apt = new EmployeeFormAdapter(RequestsActivity.this,requests);
                    list.setAdapter(apt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}