package com.example.myapplication.Classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BranchDisplayerActivity;
import com.example.myapplication.R;

import java.util.List;

public class BranchAdapter extends ArrayAdapter<Branch> {
    private Activity context;
    private List<Branch> branchList;
    private User user;
    RecyclerView.ViewHolder viewHolder;

    public BranchAdapter(Activity context, List<Branch> branches, User user){
        super(context,0,branches);
        this.context = context;
        branchList = branches;
        this.user = user;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inf = context.getLayoutInflater();
            View list = inf.inflate(R.layout.branch_list_item, null,true);
            TextView name = list.findViewById(R.id.branch_name);
            TextView address = list.findViewById(R.id.branch_address);
            TextView number = list.findViewById(R.id.branch_number);
            name.setText(branchList.get(position).getBranchName());
            if(branchList.get(position).getAddress()!=null) {
                address.setText("Address: " + branchList.get(position).getAddress());
            }else{
                address.setText("Address: Not initialized");
            }
            if(branchList.get(position).getPhoneNumber()!=null){
                number.setText("Phone number: " + branchList.get(position).getPhoneNumber());
            }else{
                number.setText("Phone number: Not initialized");
            }
            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * Replace this customer activity with lukes service request page
                     */
                    Intent i = new Intent(context, BranchDisplayerActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("branch",branchList.get(position));
                    b.putSerializable("user", user);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            return list;
        }else{
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
