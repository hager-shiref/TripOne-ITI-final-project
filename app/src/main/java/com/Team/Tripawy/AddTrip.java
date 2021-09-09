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
import com.Team.Tripawy.helper.HelperMethods;
import com.Team.Tripawy.models.Trip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;

public class AddTrip extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Toolbar toolbar;
    ImageView dateView,timeView;
    TextView timeText,dateText;
    EditText editName,editStart,editEnd , tripName;
    Spinner mySpinner,mySpinner2;
    Button add_btn;
    AddNoteActivity addNoteActivity=new AddNoteActivity();



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
        timeText.setText(simpleDateFormat.format(c.getTime()));
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
                if(tripName.getText().toString().isEmpty()||dateText.getText().toString().isEmpty()
                ||timeText.getText().toString().isEmpty())
                {
                    Toast.makeText(AddTrip.this, "Enter All Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        RDB.getTrips(AddTrip.this).insert(
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
                    Toast.makeText(AddTrip.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    HelperMethods.startScheduling(AddTrip.this, dateText.getText().toString(),timeText.getText()
                            .toString(),tripName.getText().toString(),editStart.getText().toString(),editEnd.getText().toString());
                    AddTrip.this.finish();
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
        add_btn=findViewById(R.id.addButton);
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
       // String date= DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy");

        dateText.setText(simpleDateFormat.format(c.getTime()));
       // dateText.setText(date);
    }
}
