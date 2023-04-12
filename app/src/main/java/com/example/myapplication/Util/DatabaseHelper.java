package com.example.myapplication.Util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.myapplication.Classes.Branch;
import com.example.myapplication.Classes.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;


public class DatabaseHelper {

    public static void getBranches(DBCallback callback, ArrayList<Branch> branches){
        ArrayList<Service> services = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> s = snapshot.getChildren().iterator();
                while(s.hasNext()){
                    DataSnapshot next = s.next();
                    String phoneNumber =  next.child("number").getValue(String.class);
                    String address = next.child("address").getValue(String.class);
                    String[] time = null;
                    LocalTime start = null;
                    LocalTime end = null;
                    if(next.child("end").exists()) {
                        time = next.child("end").getValue().toString().split(":");
                        end = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1].substring(0, 2)));
                        time = next.child("start").getValue().toString().split(":");
                        start = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1].substring(0, 2)));
                    }
                    String branchName = next.getKey();
                    next.child("service").getChildren().forEach(
                            x-> services.add(new Service(x.getKey(),Boolean.parseBoolean(x.getValue().toString()))));
//                    branches.add(next.getValue(Branch.class));
                    branches.add(new Branch(branchName,address,phoneNumber,new ArrayList<Service>(services),start,end));
                    services.clear();
                }
                callback.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getServices(DBCallback callback, ArrayList<Service> services){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Services");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(x->
                        services.add(new Service(x.getKey(),
                                x.child("hourlyRate").getValue(Float.class),
                                x.child("formString").getValue(String.class))));
                callback.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
