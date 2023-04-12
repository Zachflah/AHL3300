package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Classes.Form;
import com.example.myapplication.Classes.FormAdapter;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.User;
import com.example.myapplication.Util.InputVerification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FormFillingActivity extends AppCompatActivity {

    private TextView textViewServiceTitle, textViewHourlyRate;
    private ListView listViewForm;
    private Button buttonSubmit;
    private Service service;
    private User user;
    private String branchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_filling);

        textViewServiceTitle = findViewById(R.id.textViewServiceTitle);
        textViewHourlyRate = findViewById(R.id.textViewHourlyRate);
        listViewForm = findViewById(R.id.listViewForm);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        Bundle b = getIntent().getExtras();
        service = (Service)b.getSerializable("service");
        user = (User)b.getSerializable("user");
        branchName = (String)b.getString("branch name");
        textViewServiceTitle.setText(service.getServiceTitle());
        String hourlyRateString = "Hourly rate for service: " +Float.toString(service.getHourlyRate());
        textViewHourlyRate.setText(hourlyRateString);

        ArrayList<String> fields = new ArrayList<>();
        String[] keys = service.getFormString().split(",");
        for (String key:keys){
            fields.add(key.trim());
        }

        FormAdapter formAdapter = new FormAdapter(this, fields, service.getForm(), user);
        listViewForm.setAdapter(formAdapter);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Form userForm = formAdapter.getUserForm();
                if(!InputVerification.isValidForm(userForm)){
                    Toast.makeText(FormFillingActivity.this, "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                String formPath = "Branches/"+branchName+"/service forms/"+service.getServiceTitle()+"/"+user.getUsername();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference(formPath);
                userForm.setFormValue("Branch", branchName);
                userForm.setFormValue("Service", service.getServiceTitle());
                userForm.setFormValue("Status", "pending");
                userForm.setFormValue("Username",user.getUsername());
                db.setValue(userForm.getFormInformation());
                Toast.makeText(FormFillingActivity.this,"Form Sent for Approval",Toast.LENGTH_LONG).show();
                finish();


            }
        });



    }

}