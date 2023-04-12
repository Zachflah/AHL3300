package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.Classes.Admin;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.ServiceList;
import com.example.myapplication.Classes.Form;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    Toolbar toolbar;
    DatabaseReference databaseServices;
    ArrayList<Service> services;
    Admin admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        databaseServices = FirebaseDatabase.getInstance().getReference("Services");
        Bundle extras = getIntent().getExtras();
        admin = (Admin) extras.getSerializable("usertype");
        services = new ArrayList<>();


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Services");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /**
                 * Retrieve global service list from the database
                 */
//                for (DataSnapshot dbService : snapshot.getChildren()) {
//                    Service service = dbService.getValue(Service.class);
//                    services.add(service);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment,
                new EditServiceFragment()).commit();
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


        /**
         * Need to remove from remove from all branch service lists
         */


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_edit_accounts:
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment,
                        new EditAccountFragment()).commit();
                break;
            case R.id.nav_edit_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.navigation_fragment,
                        new EditServiceFragment()).commit();
                break;
            case R.id.nav_register:
                Intent intent = new Intent(AdminActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}