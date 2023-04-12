package com.example.myapplication.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFormAdapter extends ArrayAdapter<Form> {
    private Activity context;
    private List<Form> formList;
    RecyclerView.ViewHolder viewHolder;

    RequestListener l;

    public EmployeeFormAdapter(Activity context, List<Form> objects){
        super(context, 0, objects);
        this.context = context;
        formList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            View list = inflater.inflate(R.layout.form_list_item, null, true);


            TextView title = list.findViewById(R.id.form_title);
            TextView firstName = list.findViewById(R.id.first_name);
            TextView lastName = list.findViewById(R.id.last_name);

            if(formList.get(position).getFormInformation().get("Service")!=null) {
                title.setText(formList.get(position).getFormInformation().get("Service").toString());
                firstName.setText("First name: " + formList.get(position).getFormInformation().get("Firstname").toString());
                lastName.setText("Last name: " + formList.get(position).getFormInformation().get("Lastname").toString());

                String branchName = formList.get(position).getFormInformation().get("Branch").toString();
                String serviceName = formList.get(position).getFormInformation().get("Service").toString();
                String username = formList.get(position).getFormInformation().get("Username").toString();

                list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFormDialog(formList.get(position),branchName,serviceName,username);
                    }
                });
            }


            return list;
        }else {
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private void showFormDialog(Form form, String branch, String service, String username){
        Dialog dialog = new Dialog(context);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.form_dialog);


        TextView serviceName = dialog.findViewById(R.id.service_name);
        Button approve = dialog.findViewById(R.id.approve);
        Button deny = dialog.findViewById(R.id.deny);
        ListView formInfo = dialog.findViewById(R.id.form_information);


        serviceName.setText(form.getFormInformation().get("Service").toString());

        ArrayList<String> formArray = new ArrayList<>();
        form.getFormInformation().forEach((key,value)->{
            if(!key.toString().equals("Service")) {
                formArray.add(key.toString() + ": " + value.toString());
            }
        });

        FormDetailedAdapter adapter = new FormDetailedAdapter(context,formArray);
        formInfo.setAdapter(adapter);


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/"+branch+"/service forms/"+service+"/"+username);
                db.child("Status").setValue("Accepted");
                dialog.dismiss();
                context.finish();
                context.overridePendingTransition(0, 0);
                context.startActivity(context.getIntent());
                context.overridePendingTransition(0, 0);
            }
        });
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches/"+branch+"/service forms/"+service+"/"+username);
                db.child("Status").setValue("Rejected");
                dialog.dismiss();
                context.finish();
                context.overridePendingTransition(0, 0);
                context.startActivity(context.getIntent());
                context.overridePendingTransition(0, 0);
            }
        });

        dialog.show();
        /**
         * Populate form info from DB by matching the branch and
         */
    }

    public void addListener(RequestListener l){
        this.l = l;
    }
}
