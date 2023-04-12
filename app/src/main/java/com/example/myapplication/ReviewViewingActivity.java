package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Classes.Branch;
import com.example.myapplication.Classes.Review;
import com.example.myapplication.Classes.ReviewAdapter;
import com.example.myapplication.Classes.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewViewingActivity extends AppCompatActivity {

    private TextView textViewBranchReview;
    private ListView listViewReviews;
    private Button buttonLeaveReview;
    private ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_viewing);

        textViewBranchReview = findViewById(R.id.textViewBranchReview);
        listViewReviews = findViewById(R.id.listViewReviews);
        buttonLeaveReview = findViewById(R.id.buttonLeaveReview);

        Bundle b = getIntent().getExtras();
        Branch branch = (Branch)b.getSerializable("branch");
        User user = (User)b.getSerializable("user");
        String branchName = branch.getBranchName();
        reviews = new ArrayList<>();

        String title = "Reviews for Branch: " + branchName;
        textViewBranchReview.setText(title);
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews);
        listViewReviews.setAdapter(reviewAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/" + branchName + "/Reviews");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                reviews.add(snapshot.getValue(Review.class));
                reviewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonLeaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReviewViewingActivity.this, RatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("branch", branch);
                bundle.putSerializable("user", user);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }
}