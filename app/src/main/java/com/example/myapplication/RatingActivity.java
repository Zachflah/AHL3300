package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Classes.Branch;
import com.example.myapplication.Classes.Review;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.User;
import com.example.myapplication.Util.InputVerification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText editTextReview;
    private Button buttonSubmitReview;
    private Spinner spinnerServices;
    private Branch branch;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = findViewById(R.id.ratingBar);
        editTextReview = findViewById(R.id.editTextReview);
        buttonSubmitReview = findViewById(R.id.buttonSubmitReview);
        spinnerServices = findViewById(R.id.spinnerServices);

        Bundle b = getIntent().getExtras();
        branch = (Branch)b.getSerializable("branch");
        user = (User)b.getSerializable("user");

        ArrayList<String> serviceNames = new ArrayList<>();
        serviceNames.add("Please Select a Service");

        for (Service service: branch.getServices()){
            serviceNames.add(service.getServiceTitle());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setAdapter(dataAdapter);

        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/"+branch.getBranchName()+ "/Reviews");
                String reviewID = db.push().getKey();
                String serviceName = spinnerServices.getSelectedItem().toString();
                String reviewString = editTextReview.getText().toString();
                
                if (!InputVerification.isValidReviewTitle(serviceName)){
                    Toast.makeText(RatingActivity.this,"Please Select a Service to Review",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!InputVerification.isValidReviewMessage(reviewString)){
                    Toast.makeText(RatingActivity.this,"Please enter a message",Toast.LENGTH_SHORT).show();
                }
                Review review = new Review(user.getUsername(), reviewString, spinnerServices.getSelectedItem().toString(), ratingBar.getRating());
                db.child(reviewID).setValue(review);
                finish();
            }
        });

    }

}