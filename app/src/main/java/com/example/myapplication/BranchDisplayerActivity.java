package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Classes.BranchAdapter;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.ServiceList;

import java.util.ArrayList;

import com.example.myapplication.Classes.Branch;
import com.example.myapplication.Classes.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BranchDisplayerActivity extends AppCompatActivity {

    private Branch branch;
    private User user;
    private TextView textViewName, textViewAddress, textViewPhone, textViewHours;
    private Button buttonRatings;
    private ListView serviceListView;
    private ArrayList<Service> services = new ArrayList<>();
    private BranchAdapter branchAdapter;
    private ServiceList serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_displayer);

        serviceListView = findViewById(R.id.listView);
        textViewName = findViewById(R.id.textViewName);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewHours = findViewById(R.id.textViewHours);
        buttonRatings = findViewById(R.id.buttonRatings);

        //TODO get branch from intent

        branch = (Branch)getIntent().getExtras().getSerializable("branch");
        user = (User)getIntent().getExtras().getSerializable("user");
        textViewName.setText(branch.getBranchName());
        textViewAddress.setText(branch.getAddress());
        textViewPhone.setText(branch.getPhoneNumber());
        String hours = "Hours of operation : "+branch.getOpenTime()+" - "+branch.getCloseTime();
        textViewHours.setText(hours);

        Log.d("branch name", branch.getBranchName());

        DatabaseReference dbBranches = FirebaseDatabase.getInstance().getReference("Branches/"+branch.getBranchName()+"/service");
        DatabaseReference dbServices = FirebaseDatabase.getInstance().getReference("Services");
        serviceAdapter = new ServiceList(this,services);
        serviceListView.setAdapter(serviceAdapter);

        dbBranches.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String visibility = snapshot.getValue(String.class);
                Log.d("available service title", snapshot.getKey());
                if (visibility.equals("true")){
                    DatabaseReference db = dbServices.child(snapshot.getKey());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot serviceSnapshot) {
                            Service serv = serviceSnapshot.getValue(Service.class);
                            services.add(serv);
                            serviceAdapter.setServices(services);
                            serviceAdapter.notifyDataSetChanged();
                            Log.d("service available at " + branch.getBranchName(), serv.getServiceTitle());
                            Log.d("form info form db", serv.getFormString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                } else {
                    Log.d("visibility = false for ", snapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Service serv = snapshot.getValue(Service.class);
//                Log.d("form info form db", serv.getFormString());
//                String toRemove = serv.getServiceTitle();
//                for (int i = 0; i<services.size();i++){
//                    if (services.get(i).getServiceTitle().equals(toRemove)){
//                        services.remove(i);
//                    }
//                }
//                services.add(serv);
//                serviceAdapter.setServices(services);
//                serviceAdapter.notifyDataSetChanged();
//                Log.d("database read after change ", snapshot.getValue(Service.class).getServiceTitle());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                String toRemove = snapshot.getValue(Service.class).getServiceTitle();
//                for (int i = 0; i<services.size();i++){
//                    if (services.get(i).getServiceTitle().equals(toRemove)){
//                        services.remove(i);
//                    }
//                }
//                for (Service s: services){
//                    Log.d("After remove event service list", s.getServiceTitle());
//                }
//                serviceAdapter.setServices(services);
//                serviceAdapter.notifyDataSetChanged();
//                Log.d("database delete event removing local ", snapshot.getValue(Service.class).getServiceTitle());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BranchDisplayerActivity.this, ReviewViewingActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                b.putSerializable("branch", branch);
                i.putExtras(b);
                startActivity(i);
            }
        });

        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(BranchDisplayerActivity.this, FormFillingActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("service",services.get(position));
                b.putSerializable("user", user);
                b.putString("branch name", branch.getBranchName());
                i.putExtras(b);

                BranchDisplayerActivity.this.startActivity(i);
            }
        });

    }

}