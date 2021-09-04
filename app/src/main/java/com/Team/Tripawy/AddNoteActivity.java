package com.Team.Tripawy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Team.Tripawy.models.Trip;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        linearLayout = findViewById(R.id.linear);
        increase = findViewById(R.id.btn_increase);
        decrease = findViewById(R.id.btn_decrease);
        note=findViewById(R.id.note);
        addNote=findViewById(R.id.add);
        Trip trip=new Trip();
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(counter > maxNotesNumber -2)) {
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
                notes.add(note.getText().toString());
                trip.setNotes(notes);
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
}