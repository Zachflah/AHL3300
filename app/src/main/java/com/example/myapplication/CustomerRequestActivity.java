package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.Classes.Customer;
import com.example.myapplication.Classes.Request;
import com.example.myapplication.Classes.RequestAdapter;
import com.example.myapplication.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerRequestActivity extends AppCompatActivity {
    User customer;
    ArrayList<Request> requestList = new ArrayList<Request>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request);

        ListView listViewRequests = findViewById(R.id.listViewRequests);

        customer = (Customer) getIntent().getSerializableExtra("usertype");

        RequestAdapter requestAdapter = new RequestAdapter(CustomerRequestActivity.this, requestList);
        listViewRequests.setAdapter(requestAdapter);



        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(branch->{
                    branch.child("service forms").getChildren().forEach(serviceType->{
                        serviceType.getChildren().forEach(user->{
                            if(user.getKey().equals(customer.getUsername())){
                                requestList.add(
                                        new Request(user.child("Branch").getValue().toString(),
                                                user.child("Service").getValue().toString(),
                                        user.child("Status").getValue().toString()));
                            }
                        });
                    });
                });
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}