package com.Team.Tripawy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    EditText edt_notes;
    EditText edt;
    public static ArrayList<String> notes = new ArrayList<>();

    Button addNote;
    Trip trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
       initComponent();
       trip =new Trip();


        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1 = LayoutInflater.from(AddNoteActivity.this).inflate(R.layout.add_note, linearLayout, false);
                edt =view1.findViewById(R.id.edt_notes);
                if (!(counter > maxNotesNumber -2)) {


                    notes.add(counter,edt.getText().toString());
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
               // notes.add(note.getText().toString());

                trip.setNotes(notes);

              /*  String text="";
                for(int i=0;i<notes.size();i++)
                {
                    text=text+"\n"+notes.get(i);
                }
                Toast.makeText(AddNoteActivity.this, text, Toast.LENGTH_SHORT).show();*/
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter != 0) {
                    view1.findViewById(R.id.btn_decrease).setOnClickListener(this);
                    linearLayout.removeViewAt(counter);
                    notes.remove(notes.size()-1);
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
        edt_notes=findViewById(R.id.edt_notes);
    }
}