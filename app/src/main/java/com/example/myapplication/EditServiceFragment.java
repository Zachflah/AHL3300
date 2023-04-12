package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Classes.Form;
import com.example.myapplication.Classes.Service;
import com.example.myapplication.Classes.ServiceList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditServiceFragment extends Fragment {

    private Button btn_add_service, btn_remove_service;
    private EditText editTextTitle, editTextForm, editTextHourlyRate;
    private ListView listView;
    private ArrayList<Service> services;
    private ServiceList serviceAdapter;


    public EditServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_service, container, false);
        editTextTitle = v.findViewById(R.id.editTextTitle);
        editTextForm = v.findViewById(R.id.editTextForm);
        btn_add_service = v.findViewById(R.id.btn_add_service);
        btn_remove_service = v.findViewById(R.id.btn_remove_service);
        listView = v.findViewById(R.id.listView);
        editTextHourlyRate = v.findViewById(R.id.editTextHourlyRate);


        //----------------------- Set up list of services --------------------//
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Services");
        services = new ArrayList<>();
        serviceAdapter = new ServiceList(getActivity(),services);
        listView.setAdapter(serviceAdapter);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Service serv = snapshot.getValue(Service.class);
                Log.d("form info form db", serv.getFormString());
                services.add(serv);
                serviceAdapter.setServices(services);
                serviceAdapter.notifyDataSetChanged();
                Log.d("database read after init ", snapshot.getValue(Service.class).getServiceTitle());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Service serv = snapshot.getValue(Service.class);
                Log.d("form info form db", serv.getFormString());
                String toRemove = serv.getServiceTitle();
                for (int i = 0; i<services.size();i++){
                    if (services.get(i).getServiceTitle().equals(toRemove)){
                        services.remove(i);
                    }
                }
                services.add(serv);
                serviceAdapter.setServices(services);
                serviceAdapter.notifyDataSetChanged();
                Log.d("database read after change ", snapshot.getValue(Service.class).getServiceTitle());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String toRemove = snapshot.getValue(Service.class).getServiceTitle();
                for (int i = 0; i<services.size();i++){
                    if (services.get(i).getServiceTitle().equals(toRemove)){
                        services.remove(i);
                    }
                }
                for (Service s: services){
                    Log.d("After remove event service list", s.getServiceTitle());
                }
                serviceAdapter.setServices(services);
                serviceAdapter.notifyDataSetChanged();
                Log.d("database delete event removing local ", snapshot.getValue(Service.class).getServiceTitle());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /**
         * Set up pop up for editing form requirements and enabling or disabling visibility
         * and update that in the database and on screen
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"CLicked on #" + position,Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.update_services, null);
                dialogBuilder.setView(dialogView);

                final EditText editTextTitle2 = (EditText) dialogView.findViewById(R.id.editTextTitle2);
                final EditText editTextForm2  = (EditText) dialogView.findViewById(R.id.editTextForm2);
                final TextView formInfo = (TextView) dialogView.findViewById(R.id.current_form);
                final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateService);
                final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteService);
                final EditText editTextHourlyRate2  = (EditText) dialogView.findViewById(R.id.editTextHourlyRate2);

                Service serv = services.get(position);
                String serviceTitle = serv.getServiceTitle();
                String formatted = "Hourly Rate: " + serviceTitle + "\nCurrent Required Customer Information: "+ serv.getForm().toString();
                formInfo.setText(formatted);
                dialogBuilder.setTitle(serviceTitle);
                final AlertDialog b = dialogBuilder.create();
                b.show();

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name = editTextTitle2.getText().toString().trim();
                        String formInput = editTextForm2.getText().toString();
                        String hourly = editTextHourlyRate2.getText().toString();
                        String newTitle;
                        String newFormString;
                        String newRate;
                        boolean newName = false;
                        Log.d("Checking serviceTitle Value", "#" +name + "#");
                        if (name.equals("")) {
                            newTitle = serv.getServiceTitle();
                        } else {
                            newName = true;
                            newTitle = name;
                        }
                        if (formInput.equals("")) {
                            newFormString = serv.getFormString();
                        } else {
                            newFormString = "Firstname, Lastname, Email, Date of birth, Address, " + formInput;
                        }
                        if (hourly.equals("")) {
                            newRate = Float.toString(serv.getHourlyRate());
                        } else {
                            newRate = hourly;
                        }
                        if (newName){
                            removeService(serviceTitle);
                        }
                        addService(newTitle, newFormString, newRate);
                        Log.d("Checking serviceTitle Value", "#" +newTitle + "#");

                        b.dismiss();

                        Toast.makeText(getContext(), "Service Updated", Toast.LENGTH_LONG).show();

                    }
                });
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("pressed delete from long click", "idk");
                        removeService(serviceTitle);
                    }
                });
                return false;
            }
        });


        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextTitle.getText().length() == 0) {
                    editTextTitle.setError("Must have a title");
                }
            }
        });

        editTextHourlyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextHourlyRate.getText().length() == 0) {
                    editTextHourlyRate.setError("Must have an hourly rate");
                }
            }
        });

        btn_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("adding service title: ", editTextTitle.getText().toString());
                String name = editTextTitle.getText().toString().trim();
                String formInput = editTextForm.getText().toString();
                String hourly = editTextHourlyRate.getText().toString();
                String newFormString = "Firstname, Lastname, Email, Date of birth, Address";
                String newRate;
                if (!TextUtils.isEmpty(formInput)) {
                    newFormString = newFormString + ", " + formInput;
                    Log.d("new service form info", newFormString);
                }
                if (!TextUtils.isEmpty(hourly)) {
                    newRate = hourly;
                } else {
                    newRate = "15"; //minimum wage
                }
                addService(name, newFormString, newRate);
            }
        });
        btn_remove_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeService(editTextTitle.getText().toString());

            }
        });
        return v;
    }

    /**
     * The function below does not work. These functions were using onClick attribute from the buttons but it
     * only works for activities it seems so need to set a listener within the onCreateView() and do actions based on that
     * I pretty sure you can still use functions outside of the onCreateView, but just can't it directly from the onClick
     * So if you want to leave these functions below and convert them to be called by the listener inside onCreateView()
     * it should work. However, the code below isn't completed either so that will have to be fixed.
     *
     * @param v
     */
    public void removeService(String title) {
        Log.d("database delete event ", title);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference df = db.getReference("Services");
        if (title.length() == 0) {
            Toast.makeText(getContext(), "Enter a service title", Toast.LENGTH_SHORT).show();
        } else {
            df.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(title)) {
                        df.child(title).getRef().removeValue();
                        removeFromBranches(title);
                        Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Service does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void removeFromBranches(String title){
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("Branches");
        branchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(branch->{
                        branch.getChildren().forEach(service->{
                            if(service.hasChild(title)){
                                service.child(title).getRef().removeValue();
                            }
                        });
                    }
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addService(String serviceTitle, String formString, String rateString) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Log.d("add service called with params", serviceTitle + formString + rateString);
        if (serviceTitle.length() == 0) {
            Toast.makeText(getContext(), "Please enter a service title", Toast.LENGTH_SHORT).show();
        } else if(!serviceTitle.matches("^[a-zA-Z ]*$")){
            Toast.makeText(getContext(), "Please enter a title containing only letters", Toast.LENGTH_SHORT).show();
        } else if(!formString.matches("^[a-zA-Z, ]*$")){
        Toast.makeText(getContext(), "Please enter required form info with letters only", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("add service progress", "1");
            DatabaseReference df = db.getReference("Services");
            Form form = new Form();
            if(formString.length()!=0){
                String[] additionalForms = formString.split(",");
                Log.d("add service progress", "2");
                for (String param : additionalForms) {
                    form.setFormField(param);
                }
            }
            /**
             * Need to get the Form object also pushed into the database. Currently
             * it only pushes the hourlyrate, serviceTitle, and visibility class variables of Service
             * and not the form.
             */
            float rate;
            if (rateString.length() != 0){
                rate = Float.parseFloat(rateString);
            } else {
                rate = 0;
            }
            Service service = new Service(serviceTitle, form, rate);
            service.setFormString(formString);
            df.child(service.getServiceTitle()).setValue(service);
            Log.d("add service progress", "3");
            Toast.makeText(getContext(),"Successfully added service",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateService(String serviceTitle, String formString, String rateString){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Services/"+serviceTitle);
        if(!serviceTitle.matches("^[a-zA-Z ]*$")){
            Toast.makeText(getContext(), "Please enter a title containing only letters", Toast.LENGTH_SHORT).show();
        } else if(!formString.matches("^[a-zA-Z, ]*$")){
            Toast.makeText(getContext(), "Please enter required form info with letters and commas only", Toast.LENGTH_SHORT).show();
        }
        else {
            /**
             * Need to get the Form object also pushed into the database. Currently
             * it only pushes the hourlyrate, serviceTitle, and visibility class variables of Service
             * and not the form.
             */
            float rate;
            if (rateString.length() != 0) {
                rate = Float.parseFloat(rateString);
            } else {
                rate = 0;
            }
            db.child("formString").setValue(formString);
            db.child("hourlyRate").setValue(rate);
            Log.d("add service progress", "3");
            Toast.makeText(getContext(), "Successfully added service", Toast.LENGTH_SHORT).show();
        }
    }
}