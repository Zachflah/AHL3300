package com.example.myapplication.Classes;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalTime;

public class Branch implements Serializable {
    private String branchName;
    private String address;
    private String phoneNumber;
    private ArrayList<Service> services;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String[] workingDays;

    public Branch(){}
    public Branch(String branchName){
        this.branchName = branchName;
    }
    public Branch(String branchName,LocalTime startTime, LocalTime endTime,String phoneNumber){
        this.branchName = branchName;
        this.openTime = startTime;
        this.closeTime = endTime;
        this.phoneNumber = phoneNumber;
    }
    public Branch(LocalTime openTime, LocalTime closeTime, String[] workingDays,String branchName){
        services = new ArrayList<>();
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.workingDays = workingDays;
        this.branchName = branchName;
    }
    public Branch(String branchName,String branchAddress, String phoneNumber, ArrayList<Service> branchServices,LocalTime openTime, LocalTime closeTime){
        this.branchName = branchName;
        this.address = branchAddress;
        this.phoneNumber = phoneNumber;
        this.services = branchServices;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    private void setOpen(LocalTime openTime){
        this.openTime = openTime;
    }

    private void setClose(LocalTime closeTime){
        this.closeTime = closeTime;
    }

    private void setWorkingDays(String[] workingDays) {
        this.workingDays = workingDays;
    }

    private void setService(String serviceTitle){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Services");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services.add(snapshot.child(serviceTitle).getValue(Service.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LocalTime getOpenTime(){
        return openTime;
    }

    public LocalTime getCloseTime(){
        return closeTime;
    }

    public String[] getWorkingDays(){
        return workingDays;
    }

    public ArrayList<Service> getServices(){
        return services;
    }

    public String getBranchName(){
        return branchName;
    }



}
