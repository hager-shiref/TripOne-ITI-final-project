package com.Team.Tripawy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.models.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageButton increase;
    ImageButton decrease;
    int maxNotesNumber = 10;
    int counter = 0;
    private View view1;
    TextView note;
    public static ArrayList<String> notes = new ArrayList<>();

    Button addNote;
    Trip trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
       initComponent();

       int id= getIntent().getIntExtra("id",2);
       List<String>data=new ArrayList<>();
       data=getIntent().getStringArrayListExtra("data");
       trip =new Trip();
       trip.setId(id);
       trip.setName(data.get(0));
       trip.setDate(data.get(1));
       trip.setTime(data.get(2));
       trip.setTripType(data.get(3));
       trip.setTo(data.get(4));
       trip.setFrom(data.get(5));
       trip.setTripState(data.get(6));


        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(counter > maxNotesNumber - 2)) {
                    view1 = LayoutInflater.from(AddNoteActivity.this).inflate(R.layout.add_note, linearLayout, false);
                    view1.findViewById(R.id.btn_increase).setOnClickListener(this);
                    linearLayout.addView(view1);
                    counter++;
                }


            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> notes1 = readAllNotes();
                trip.setNotes(notes1);

                Executors.newSingleThreadExecutor().execute(() ->{
                    RDB.getTrips(AddNoteActivity.this).update(trip);
                });
                Toast.makeText(AddNoteActivity.this, "Saved", Toast.LENGTH_SHORT).show();
               AddNoteActivity.this.finish();
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter != 0) {
                    view1.findViewById(R.id.btn_decrease).setOnClickListener(this);
                    linearLayout.removeViewAt(counter);
                    counter--;
                }
            }
        });

    }

    private void initComponent() {
        linearLayout = findViewById(R.id.linear);
        increase = findViewById(R.id.btn_increase);
        decrease = findViewById(R.id.btn_decrease);
        note=findViewById(R.id.note);
        addNote=findViewById(R.id.add);

    }
    private ArrayList<String> readAllNotes() {
        ArrayList<String> notes = new ArrayList<>();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    View mView = linearLayout.getChildAt(j);
                    if (mView instanceof EditText) {
                        EditText editText = (EditText) mView;
                        String etNote = editText.getText().toString().trim();
                        if (!etNote.equals("")) {
                            notes.add(etNote);
                        }
                    }

                }
            }
        }
        return notes;
    }
}