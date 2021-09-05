package com.Team.Tripawy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.models.Trip;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Update_Trip extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    ImageView dateView,timeView;
    TextView timeText,dateText;
    EditText editName,editStart,editEnd , tripName;
    Spinner mySpinner,mySpinner2;
    Button update_btn,cancle_btn;
    int trip_id;
    Trip trip;
    AddNoteActivity addNoteActivity=new AddNoteActivity();
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeText.setText( hourOfDay +  " : " + minute );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);
        initComponent();
        List<String>data=new ArrayList<>();
        data=getIntent().getStringArrayListExtra("data");
        editName.setText(data.get(0));
        editStart.setText(data.get(1));
        editEnd.setText(data.get(2));
        dateText.setText(data.get(3));
        timeText.setText(data.get(4));
         trip_id= Integer.parseInt(data.get(5));
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
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_Trip.this.finish();
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tripName.getText().toString().isEmpty()||dateText.getText().toString().isEmpty()
                        ||timeText.getText().toString().isEmpty())
                {
                    Toast.makeText(Update_Trip.this, "Enter All Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    trip=new Trip();
                    trip.setId(trip_id);
                    Executors.newSingleThreadExecutor().execute(() ->{
                        RDB.getTrips(getApplicationContext()).delete(trip);
                    });
                    Executors.newSingleThreadExecutor().execute(() -> {
                        RDB.getTrips(Update_Trip.this).insert(
                                new Trip(tripName.getText().toString(),
                                        dateText.getText().toString(),
                                        timeText.getText().toString(),
                                        "UpComing",
                                        "One Way",
                                        editStart.getText().toString(),
                                        editEnd.getText().toString(),
                                        addNoteActivity.notes)

                        );
                    });
                    Toast.makeText(Update_Trip.this, "update", Toast.LENGTH_SHORT).show();
                    Update_Trip.this.finish();
                }

            }
        });

    }
    private  void initComponent(){
        toolbar = findViewById(R.id.addToolBar);
        // setSupportActionBar(toolbar);
        tripName=findViewById(R.id.editName);
        timeView=findViewById(R.id.alarm);
        dateView=findViewById(R.id.calender);
        timeText=findViewById(R.id.timeId);
        dateText=findViewById(R.id.dateId);
        timeText.setText("");
        dateText.setText("");
       update_btn=findViewById(R.id.update_btn);
       cancle_btn=findViewById(R.id.cancle_btn);
        editName=findViewById(R.id.editName);
        editStart=findViewById(R.id.editStart);
        editEnd=findViewById(R.id.editEnd);
        mySpinner=(Spinner) findViewById(R.id.mySpinner);
        mySpinner2=(Spinner) findViewById(R.id.mySpinner2);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String date= DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        dateText.setText(date);
    }
}
