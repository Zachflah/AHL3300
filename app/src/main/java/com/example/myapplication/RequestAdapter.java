package com.example.myapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;


public class RequestAdapter extends ArrayAdapter<Request> {
    private Activity context;
    private ArrayList<Request> requests;
    RecyclerView.ViewHolder viewHolder;

    public RequestAdapter(Activity context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.context = context;
        this.requests = requests;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.request_layout, null, true);

            TextView textViewBranch = listViewItem.findViewById(R.id.textViewBranch);
            TextView textViewServiceTitle = listViewItem.findViewById(R.id.textViewServiceTitle);
            TextView textViewStatus = listViewItem.findViewById(R.id.textViewStatus);

            textViewBranch.setText(requests.get(position).getBranchName());
            textViewServiceTitle.setText(requests.get(position).getServiceName());
            textViewStatus.setText(requests.get(position).getStatus());


            return listViewItem;
        } else {
            viewHolder = (RecyclerView.ViewHolder)convertView.getTag();
        }
        return convertView;

    }
}
