package com.example.myapplication.Classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<BasicAccount> {
    private Activity context;
    private List<BasicAccount> accountList;
    RecyclerView.ViewHolder viewHolder;

    public AccountAdapter(Activity context, List<BasicAccount> accounts){
        super(context,0,accounts);
        this.context = context;
        accountList = accounts;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inf = context.getLayoutInflater();
            View list = inf.inflate(R.layout.account_list_item, null,true);
            TextView username = list.findViewById(R.id.username);
            TextView role = list.findViewById(R.id.role);

            username.setText(accountList.get(position).getUsername());
            role.setText(accountList.get(position).getRole());
            return list;
        }else{
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
