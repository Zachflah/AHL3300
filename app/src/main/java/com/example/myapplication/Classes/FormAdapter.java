package com.example.myapplication.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;


public class FormAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> fields;
    private Form userForm;
    private User user;
    RecyclerView.ViewHolder viewHolder;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    public FormAdapter(Activity context, ArrayList<String> fields, Form userForm, User user) {
        super(context, R.layout.layout_service_list, fields);
        this.context = context;
        this.fields = fields;
        this.userForm = userForm;
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.form_info_layout, null, true);

            TextView textViewFormKey = (TextView) listViewItem.findViewById(R.id.textViewFormKey);
            EditText editTextFormValue = (EditText) listViewItem.findViewById(R.id.editTextFormValue);

            editTextFormValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    userForm.setFormValue(fields.get(position), editTextFormValue.getText().toString());
                }
            });

            String field = fields.get(position);
            textViewFormKey.setText(field);
            boolean both = false;
            if (field.equals("Email")) {
                editTextFormValue.setText(user.getEmail());
            } else {
                final Calendar calendar = Calendar.getInstance();
                if (field.toLowerCase().contains("date")){
                    both = true;
                    datePicker(calendar,editTextFormValue);
                }
                if (field.toLowerCase().contains("time")){
                    timePicker(calendar, editTextFormValue, both);
                }

            }
            return listViewItem;
        } else {
            viewHolder = (RecyclerView.ViewHolder)convertView.getTag();
        }
        return convertView;

    }
    public Form getUserForm(){
        return userForm;
    }
    private void datePicker(Calendar calendar, EditText editTextFormValue){

        // initialising the layout
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);

        // initialising the datepickerdialog
        datePickerDialog = new DatePickerDialog(context);

        // click on edittext to set the value
        editTextFormValue.setInputType(0);
        editTextFormValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        editTextFormValue.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                }, year, month, day);

                // set maximum date to be selected as today


                // show the dialog
                datePickerDialog.show();
            }
        });
    }
    private void timePicker(Calendar calendar, EditText editTextFormValue, boolean both){

        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        if (both){
            editTextFormValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String date = editTextFormValue.getText().toString();
                    if (date.length()<=10 && date.length()>0){
                        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String min = Integer.toString(minute);
                                if (minute <10){
                                    min = "0" + minute;
                                }
                                String date_and_time = editTextFormValue.getText().toString() + " at " + hourOfDay + ":" + min;
                                editTextFormValue.setText(date_and_time);


                            }
                        }, mHour, mMinute, false);
                        timePickerDialog.show();
                        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                editTextFormValue.setText("");
                            }
                        });
                    } else {
                        return;
                    }

                }
            });
        } else{
            editTextFormValue.setInputType(0);
            editTextFormValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            String time = hourOfDay + ":" + minute;
                            editTextFormValue.setText(time);

                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });

        }



    }
}