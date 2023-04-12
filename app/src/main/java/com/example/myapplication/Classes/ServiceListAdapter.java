package com.example.myapplication.Classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

//Based on tutorial from https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
public class ServiceListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> serviceList;
    RecyclerView.ViewHolder viewHolder;

    CheckBoxListener l;
    public ServiceListAdapter(Activity context, List<String> objects){
        super(context, 0, objects);
        this.context = context;
        serviceList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            View list = inflater.inflate(R.layout.service_list_item, null, true);
            CheckBox btn = list.findViewById(R.id.service);
            btn.setText(serviceList.get(position));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boxClick(btn.getText().toString(), btn.isChecked());
                }
            });
            return list;
        }else {
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public void addListener(CheckBoxListener l){
        this.l = l;
    }
    private void boxClick(String box, boolean pressed){
        CheckBoxEvent e = new CheckBoxEvent(this,box,pressed);
        l.changeEvent(e);
    }
}
