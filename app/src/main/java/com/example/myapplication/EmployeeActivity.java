package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Classes.CheckBoxEvent;
import com.example.myapplication.Classes.CheckBoxListener;
import com.example.myapplication.Classes.Employee;
import com.example.myapplication.Classes.ServiceListAdapter;
import com.example.myapplication.Util.InputVerification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {
    Employee user;
    ListView serviceList;
    TextView branch;

    EditText address;
    EditText phone;
    Button submitService;
    Button submitBranchInfo;
    String start;
    String end;

    ArrayList<String> checkServices = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        serviceList = findViewById(R.id.service_list);
        address = findViewById(R.id.editTextAddress);
        phone = findViewById(R.id.editTextPhone);
        branch = findViewById(R.id.textView);
        submitBranchInfo = findViewById(R.id.branch_info);
        submitService = findViewById(R.id.service_options);

        Bundle extras = getIntent().getExtras();
        user = (Employee) extras.getSerializable("usertype");

        Intent i = getIntent();
        start = i.getStringExtra("Start");
        end = i.getStringExtra("End");

        // --------- Show the branch the employee is associated with ---------//
        branch.setText("Branch: "+user.getBranch());

        // --------- Submit button listener-------- //

        submitBranchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean addressValid = InputVerification.checkAddress(address.getText().toString());
                boolean numberValid = InputVerification.checkPhoneNumber(phone.getText().toString());
                if(!addressValid) address.setError("Invalid address");
                if(!numberValid) phone.setError("Invalid number. Please follow ###-###-#### format");
//                Check working hour
                if(start==null) Toast.makeText(EmployeeActivity.this,"Please set working hours",Toast.LENGTH_LONG).show();
                if(numberValid&&addressValid) {
                    DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/address");
                    DatabaseReference numberRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/number");
                    DatabaseReference startRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/start");
                    DatabaseReference endRef = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/end");

                    startRef.setValue(start);
                    endRef.setValue(end);
                    addressRef.setValue(address.getText().toString());
                    numberRef.setValue(phone.getText().toString());
                    Toast.makeText(EmployeeActivity.this,"Successfuly uploaded",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(EmployeeActivity.this,EmployeeMainActivity.class);
                    i.putExtra("usertype",user);
                    startActivity(i);
                }
                /**
                 * Implement check for working hours in here and InputVerification class
                 * along with checking other user inputs here
                 */
            }
        });
        submitService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String s: checkServices){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Branches/"+user.getBranch()+"/service/"+s);
                    ref.setValue("true");
                }
                if(checkServices.isEmpty()) {
                    Toast.makeText(EmployeeActivity.this, "No services added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EmployeeActivity.this, "Services added",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent i = getIntent();
        String addressString = i.getStringExtra("address");
        String phoneString = i.getStringExtra("phone");
        if(addressString!=null){
            address.setText(addressString);
        }
        if(phoneString!=null){
            phone.setText(phoneString);
        }

        //---- Setting up list of available services ----//
        ArrayList<String> list = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Services");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    list.add(item.getKey());
                }
                ServiceListAdapter adapter = new ServiceListAdapter(EmployeeActivity.this,list);
                adapter.addListener(new CheckBoxListener() {
                    @Override
                    public void changeEvent(CheckBoxEvent event) {
                        if(event.isPressed()) {
                            checkServices.add(event.getBox());
                        }else{
                            checkServices.remove(event.getBox());
                        }
                        for(String s:checkServices)
                        Log.d("output",s);
                    }
                });
                serviceList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void goToSetHour(View v){
        Intent intent = new Intent(this,EmployeeTimePickerActivity.class);
        intent.putExtra("usertype",user);
        intent.putExtra("phone",phone.getText().toString());
        intent.putExtra("address",address.getText().toString());
        startActivity(intent);
    }
}

