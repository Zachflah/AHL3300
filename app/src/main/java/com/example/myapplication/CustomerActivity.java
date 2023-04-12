package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Classes.Branch;
import com.example.myapplication.Classes.BranchAdapter;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.User;
import com.example.myapplication.Util.DBCallback;
import com.example.myapplication.Util.DatabaseHelper;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerActivity extends AppCompatActivity {
    User user;
    private TextView textSearchOpen, textSearchClose;
    private AutoCompleteTextView editTextSearchService,editTextSearchAddress;
    private ArrayList<Branch> branches = new ArrayList<>();
    private ArrayList<Service> services = new ArrayList<>();
    private ListView branchListView;
    private BranchAdapter branchAdapter;
    String start,end;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        editTextSearchAddress = findViewById(R.id.editTextSearchAddress);
        editTextSearchService = findViewById(R.id.editTextSearchService);
        textSearchOpen = findViewById(R.id.start);
        textSearchClose = findViewById(R.id.end);
        branchListView = findViewById(R.id.listView);

        Intent i = getIntent();
        String address = i.getStringExtra("address");
        String service = i.getStringExtra("service");
        if(address!=null&&!address.equals("")){
            editTextSearchAddress.setText(i.getStringExtra("address"));
        }
        if(service!=null&&!service.equals("")){
            editTextSearchService.setText(i.getStringExtra("service"));
        }

        editTextSearchAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextSearchAddress.showDropDown();
            }
        });
        editTextSearchService.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextSearchService.showDropDown();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        user = (User)extras.getSerializable("usertype");
        start = getIntent().getStringExtra("Start");
        end = getIntent().getStringExtra("End");

        if(start!=null&&end!=null){
            textSearchClose.setText(end);
            textSearchOpen.setText(start);
        }
        populateBranches();
        setAutoComplete();

    }
    private void setAutoComplete(){
        DatabaseHelper.getServices(new DBCallback() {
            @Override
            public void onSuccess() {
                ArrayList<String> strings = new ArrayList<>();
                for(Service s:services){
                    strings.add(s.getServiceTitle());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerActivity.this,android.R.layout.simple_list_item_1,strings);
                editTextSearchService.setAdapter(adapter);
            }
        },services);

    }
    private void populateBranches(){
        DatabaseHelper.getBranches(new DBCallback() {
            @Override
            public void onSuccess() {
                branchAdapter = new BranchAdapter(CustomerActivity.this,branches, user);
                branchListView.setAdapter(branchAdapter);
                ArrayList<String> addresses = new ArrayList<>();
                for(Branch b : branches){
                    if(b.getAddress()!=null) {
                        addresses.add(b.getAddress());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerActivity.this,android.R.layout.simple_list_item_1,addresses);
                editTextSearchAddress.setAdapter(adapter);
            }
        },branches);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void searchBranches(View v){
        String address = editTextSearchAddress.getText().toString();
        String service = editTextSearchService.getText().toString();
        String open = textSearchOpen.getText().toString();
        String close = textSearchClose.getText().toString();

        ArrayList<Branch> filteredBranches = new ArrayList<>();
        for(Branch b : branches){
            filteredBranches.add(
                    new Branch(b.getBranchName(),
                            b.getAddress(),
                            b.getPhoneNumber(),
                            b.getServices(),
                            b.getOpenTime(),
                            b.getCloseTime()));
        }
        filteredBranches = searchWithAddress(filteredBranches,address);
        filteredBranches = searchWithService(filteredBranches,service);
        filteredBranches = searchWithHours(filteredBranches,open,close);
        BranchAdapter filtered = new BranchAdapter(CustomerActivity.this, filteredBranches, user);
        branchListView.setAdapter(filtered);
    }
    private ArrayList<Branch> searchWithAddress(ArrayList<Branch> list, String address){
        if(address.isEmpty()){
            return list;
        }
        ArrayList<Branch> filter = new ArrayList<>();
        for (Branch branch : branches) {
            if (branch.getAddress() != null) {
                if (branch.getAddress().toLowerCase().equals(address.toLowerCase())) {
                    filter.add(branch);
                }
            }
        }
        return filter;
    }
    private ArrayList<Branch> searchWithService(ArrayList<Branch> list, String service){
        if(service.isEmpty()){
            return list;
        }
        ArrayList<Branch> filter = new ArrayList<>();
        for (Branch branch : list) {
            for(Service s: branch.getServices()) {
                if (s.getVisibility()&&
                        s.getServiceTitle().equals(service)) {
                   filter.add(branch);
                }
            }
        }
        return filter;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Branch> searchWithHours(ArrayList<Branch> list, String open, String close){
        if(open.equals("")||close.equals("")){
            return list;
        }
        //Disgusting way of solving this issue. Worst code I've written ;-;
        ArrayList<Branch> filter = new ArrayList<>();
        String[] o = open.split(":");
        String[] c = close.split(":");
        int startHour = Integer.parseInt(o[0]);
        if(o[1].substring(3).equals("pm")){
            startHour+=12;
        }
        int startMinute = Integer.parseInt(o[1].substring(0,2));
        LocalTime start = LocalTime.of(startHour,startMinute);
        int closeHour = Integer.parseInt(c[0]);
        if(c[1].substring(3).equals("pm")){
            startHour+=12;
        }
        int closeMinute = Integer.parseInt(c[1].substring(0,2));
        LocalTime end = LocalTime.of(closeHour,closeMinute);
        for(Branch branch : list){
            if(branch.getCloseTime()!=null&&branch.getOpenTime()!=null) {
                if (branch.getOpenTime().isBefore(start) && branch.getCloseTime().isAfter(end)) {
                    filter.add(branch);
                }
            }
        }
        return filter;
    }
    public void clear(View v){
        editTextSearchService.setText("");
        editTextSearchAddress.setText("");
        textSearchClose.setText("");
        textSearchOpen.setText("");
        branchAdapter = new BranchAdapter(this,branches, user);
        branchListView.setAdapter(branchAdapter);
    }
    public void goToTimePicker(View v){
        Intent i = new Intent(this,TimePickerActivity.class);
        i.putExtra("usertype",user);
        i.putExtra("service",editTextSearchService.getText().toString());
        i.putExtra("address",editTextSearchAddress.getText().toString());
        startActivity(i);
    }
    public void goToRequestActivity(View v){
        Intent i = new Intent(this,CustomerRequestActivity.class);
        i.putExtra("usertype",user);
        startActivity(i);
    }
}