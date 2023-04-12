package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Classes.AccountAdapter;
import com.example.myapplication.Classes.BasicAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAccountFragment extends Fragment {


    Button btn_remove_acc;
    EditText editTextUsername;
    Spinner roles;
    ListView accountsList;
    ArrayList<BasicAccount> accounts = new ArrayList<>();

    public EditAccountFragment() {
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
        View v = inflater.inflate(R.layout.fragment_edit_account, container, false);
        roles = v.findViewById(R.id.roles);
        btn_remove_acc = v.findViewById(R.id.btn_remove_account);
        editTextUsername = v.findViewById(R.id.editText_username);
        accountsList = v.findViewById(R.id.account_list);

        //Account list setup
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(account->{
                    if(!account.getKey().equals("admin")) {
                        accounts.add(new BasicAccount(account.child("username").getValue().toString(), account.child("role").getValue().toString()));
                    }
                });
                AccountAdapter adapter = new AccountAdapter(getActivity(),accounts);
                accountsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String[] items = new String[]{"Customer", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        roles.setAdapter(adapter);

        btn_remove_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference df = db.getReference("users");
                if(editTextUsername.getText().length()==0){
                    Toast.makeText(getContext(),"Enter a username", Toast.LENGTH_SHORT).show();
                }else if(editTextUsername.getText().toString().equals("admin")){
                    Toast.makeText(getContext(), "You cannot delete an admin account", Toast.LENGTH_SHORT).show();
                }else {
                    df.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(editTextUsername.getText().toString())
                                    && snapshot.child(editTextUsername.getText().toString() + "/" + "role").
                                    getValue().toString().equals(roles.getSelectedItem().toString())) {

                                df.child(editTextUsername.getText().toString()).getRef().removeValue();
                                Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "User does not exist or wrong role", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        return v;
    }
}