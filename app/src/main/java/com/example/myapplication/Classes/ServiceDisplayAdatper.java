package com.example.myapplication.Classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.myapplication.R;

import java.util.List;


public class ServiceDisplayAdatper extends ArrayAdapter {
    private Activity context;
    private List<String> serviceList;

    public ServiceDisplayAdatper(Activity context, List<String> objects){
        super(context, 0, objects);
        this.context = context;
        serviceList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View list = inflater.inflate(R.layout.service_list_item,null,true);


        return list;
    }
}
