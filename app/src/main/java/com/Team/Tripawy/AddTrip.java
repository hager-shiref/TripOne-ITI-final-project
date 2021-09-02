package com.Team.Tripawy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Team.Tripawy.models.Trip;
import com.Team.Tripawy.ui.upcoming.UpcomingFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTrip extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Toolbar toolbar;
    ImageView dateView,timeView;
    TextView timeText,dateText;
    EditText editName,editStart,editEnd;
    Spinner mySpinner,mySpinner2;
    Button add_btn;
    double latitudeForDikirnis=31.0857,longitudeForDikirnis=31.5888,latitudeForIti=31.0407,longitudeForIti=31.3544;
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeText.setText( hourOfDay +  " : " + minute );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initComponent();
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker= new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"timePicker");
            }
        });
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");

            }

        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip =new Trip();
                if(editName.getText().toString().isEmpty()||editStart.getText().toString().isEmpty()||editEnd.getText().toString().isEmpty()
                ||timeText.getText().toString().isEmpty()||dateText.getText().toString().isEmpty())
                {
                    Toast.makeText(AddTrip.this, "Enter All Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    trip.setName(editName.getText().toString());
                    trip.setFrom(editStart.getText().toString());
                    trip.setTo(editEnd.getText().toString());
                    trip.setTime(timeText.getText().toString());
                    trip.setDate(dateText.getText().toString());
                    trip.setTripState("UpComing");
                    trip.getTripType(mySpinner2.getSelectedItem().toString());
                    trip.getTripRepeat(mySpinner.getSelectedItem().toString());
                }



            }
        });

    }
    private  void initComponent(){
        toolbar = findViewById(R.id.addToolBar);
       // setSupportActionBar(toolbar);
        timeView=findViewById(R.id.alarm);
        dateView=findViewById(R.id.calender);
        timeText=findViewById(R.id.timeId);
        dateText=findViewById(R.id.dateId);
        timeText.setText("");
        dateText.setText("");
        add_btn=findViewById(R.id.addButton);
        editName=findViewById(R.id.editName);
        editStart=findViewById(R.id.editStart);
        editEnd=findViewById(R.id.editEnd);
        mySpinner=findViewById(R.id.mySpinner);
        mySpinner2=findViewById(R.id.mySpinner2);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        long now = c.getTimeInMillis();

        String date= DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        dateText.setText(date);
    }
}
