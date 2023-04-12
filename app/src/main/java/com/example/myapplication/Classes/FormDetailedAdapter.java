package com.example.myapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Map;


public class FormDetailedAdapter extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<String> fields;
    RecyclerView.ViewHolder viewHolder;

    public FormDetailedAdapter(Activity context, ArrayList<String> form) {
        super(context, R.layout.form_detailed_list_item, form);
        this.context = context;
        this.fields = form;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.form_detailed_list_item, null, true);

            TextView textViewFormKey = (TextView) listViewItem.findViewById(R.id.form_information);

            String field = fields.get(position);
            textViewFormKey.setText(field);
            return listViewItem;
        } else {
            viewHolder = (RecyclerView.ViewHolder)convertView.getTag();
        }
        return convertView;

    }
    public void setServices(ArrayList<String> fields){
        this.fields = fields;
    }
}