package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.os.Bundle;

import com.example.myapplication.Classes.Employee;
import com.example.myapplication.Classes.User;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public class EmployeeTimePickerActivity extends AppCompatActivity {

    Button btnPickTime;
    Button btnPickEndTime;
    Button btnReturn;
    TextView previewSelectedTime;
    TextView previewSelectedEndTime;
    User user;

    private final TimePickerDialog.OnTimeSetListener timePickerDialogListener = (TimePickerDialog.OnTimeSetListener) (
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(@Nullable TimePicker view, int hourOfDay, int min){
                    String formattedTime = hourOfDay == 0 ?(min < 10 ? hourOfDay + 12 + ":0" + min +
                            " am" : "" + (hourOfDay + 12) + ':' + min + " am") :
                            (hourOfDay > 12 ? (min < 10 ? hourOfDay - 12 + ":0" + min +
                                    " pm" : "" + (hourOfDay - 12) + ':' + min + " pm") : (hourOfDay == 12 ?
                                    (min < 10 ? hourOfDay + ":0" + min + " pm" : "" +
                                            hourOfDay + ':' + min + " pm") : (min < 10 ? "" + hourOfDay + ':' + min +
                                    " am" : "" + hourOfDay + ':' + min + " am")));
                    EmployeeTimePickerActivity.this.getPreviewSelectedTime().setText((CharSequence)formattedTime);
                }
            });

    private final TimePickerDialog.OnTimeSetListener timePickerDialogListener2 = (TimePickerDialog.OnTimeSetListener) (
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(@Nullable TimePicker view, int hourOfDay, int min){
                    String formattedTime = hourOfDay == 0 ?(min < 10 ? hourOfDay + 12 + ":0" + min +
                            " am" : "" + (hourOfDay + 12) + ':' + min + " am") :
                            (hourOfDay > 12 ? (min < 10 ? hourOfDay - 12 + ":0" + min +
                                    " pm" : "" + (hourOfDay - 12) + ':' + min + " pm") : (hourOfDay == 12 ?
                                    (min < 10 ? hourOfDay + ":0" + min + " pm" : "" +
                                            hourOfDay + ':' + min + " pm") : (min < 10 ? "" + hourOfDay + ':' + min +
                                    " am" : "" + hourOfDay + ':' + min + " am")));
                    EmployeeTimePickerActivity.this.getPreviewSelectedEndTime().setText((CharSequence) formattedTime);
                }
            });
    public final TextView getPreviewSelectedTime(){
        TextView time = previewSelectedTime;
        if (time == null){
            Intrinsics.throwUninitializedPropertyAccessException("PreviewSelectedTime");
        }
        return time;
    }

    public final void setPreviewSelectedTime(@NotNull TextView time){
        previewSelectedTime = time;
    }

    public final TextView getPreviewSelectedEndTime(){
        TextView time = previewSelectedEndTime;
        if (time == null){
            Intrinsics.throwUninitializedPropertyAccessException("PreviewSelectedTime");
        }
        return time;
    }

    public final void setPreviewSelectedEndTime(@NotNull TextView time){ previewSelectedEndTime = time; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        Intent i = getIntent();

        user = (Employee) i.getSerializableExtra("usertype");

        btnPickTime = findViewById(R.id.pick_time_button);
        previewSelectedTime = findViewById(R.id.preview_picked_time_textView);
        btnPickEndTime = findViewById(R.id.pick_endtime_button);
        previewSelectedEndTime = findViewById(R.id.preview_picked_Endtime);

        btnPickTime.setOnClickListener((View.OnClickListener) new View.OnClickListener(){
            public final void onClick(View view){
                TimePickerDialog timePicker = new TimePickerDialog(EmployeeTimePickerActivity.this,
                        EmployeeTimePickerActivity.this.timePickerDialogListener,12,10,false);
                timePicker.show();

            }
        });

        btnPickEndTime.setOnClickListener((View.OnClickListener) new View.OnClickListener(){
            public final void onClick(View view){
                TimePickerDialog timePicker = new TimePickerDialog(EmployeeTimePickerActivity.this,
                        EmployeeTimePickerActivity.this.timePickerDialogListener2,12,10,false);
                timePicker.show();

            }
        });
    }
    public void submitInfo(View v){
        Intent intent = new Intent(EmployeeTimePickerActivity.this, EmployeeActivity.class);
        intent.putExtra("Start", previewSelectedTime.getText().toString());
        intent.putExtra("End", previewSelectedEndTime.getText().toString());
        intent.putExtra("usertype",user);
        Intent i = getIntent();
        intent.putExtra("phone",i.getStringExtra("phone"));
        intent.putExtra("address",i.getStringExtra("address"));
        startActivity(intent);
    }
}