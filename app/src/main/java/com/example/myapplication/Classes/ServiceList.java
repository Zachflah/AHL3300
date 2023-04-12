package com.example.myapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.myapplication.R;

import java.util.ArrayList;


public class ServiceList extends ArrayAdapter<Service> {
    private Activity context;
    ArrayList<Service> services;

    public ServiceList(Activity context, ArrayList<Service> services) {
        super(context, R.layout.layout_service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.layout_service_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewHourlyRate = (TextView) listViewItem.findViewById(R.id.textViewHourlyRate);
        TextView textViewFormInformation = (TextView) listViewItem.findViewById(R.id.textViewFormInformation);

        Service service = services.get(position);
        textViewName.setText(service.getServiceTitle());
        String HourlyRateString = "Hourly Rate: " + Float.toString(service.getHourlyRate());
        textViewHourlyRate.setText(HourlyRateString);
        textViewFormInformation.setText(service.getFormString());
        Log.d("Listview item added ", service.getServiceTitle());
        return listViewItem;
    }
    public void setServices(ArrayList<Service> services){
        this.services = services;
    }
}

