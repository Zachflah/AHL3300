package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Classes.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeMainActivity extends AppCompatActivity {
    Employee user;
    ListView serviceList;
    TextView branch;
    TextView address;
    TextView phone;
    TextView workingHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        serviceList = findViewById(R.id.service_list);
        address = findViewById(R.id.address_text);
        phone = findViewById(R.id.phone_number_text);
        workingHour = findViewById(R.id.working_hour_text);
        branch = findViewById(R.id.branch_name);

        Intent i = getIntent();
        user = (Employee) i.getSerializableExtra("usertype");
        branch.setText("Branch: "+user.getBranch());
        // set up text values based on upload
    }
    @Override
    protected void onStart(){
        super.onStart();
        /**
         * Change this to show providing services of the Branch
         */
        ArrayList<String> list = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot branch: snapshot.child("service").getChildren()){
                    if(branch.getValue().toString().equals("true")) {
                        list.add(branch.getKey());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(EmployeeMainActivity.this,android.R.layout.simple_spinner_dropdown_item,list);
                serviceList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        serviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String service = list.get(position);
                deleteService(service);
                list.remove(service);
                serviceList.setAdapter(new ArrayAdapter<String>(EmployeeMainActivity.this,android.R.layout.simple_spinner_dropdown_item,list));
                return false;
            }
        });
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sp = snapshot.child(user.getBranch());
                String addressString= null;
                String phoneString = null;
                String workingHourString = null;
                try {
                    addressString = snapshot.child(user.getBranch()).child("address").getValue().toString();
                }catch(NullPointerException e) {}
                try{
                    phoneString = snapshot.child(user.getBranch().toString()).child("number").getValue().toString();
                }catch(NullPointerException e){}
                try {
                    workingHourString = sp.child("start").getValue().toString()+" - "+sp.child("end").getValue().toString();
                }catch (NullPointerException e){}
                if(addressString!=null) {
                    address.setText("Address: "+addressString);
                }
                if(phoneString!=null) {
                    phone.setText("Phone Number: "+phoneString);
                }
                if(workingHourString!=null) {
                    workingHour.setText(workingHourString);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void deleteService(String service){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/service/"+service);
        db.setValue("false");
        Toast.makeText(this,"Removed service",Toast.LENGTH_SHORT);
    }
    public void goToEdit(View v){
        Intent intent = new Intent(this,EmployeeActivity.class);
        intent.putExtra("usertype",user);
        startActivity(intent);
    }
    public void goToRequests(View v){
        Intent intent = new Intent(this,RequestsActivity.class);
        intent.putExtra("branchName",user.getBranch());
        startActivity(intent);
    }
}