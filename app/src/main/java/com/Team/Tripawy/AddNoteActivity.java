package com.Team.Tripawy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AddNoteActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageButton increase;
    ImageButton decrease;
    int maxNotesNumber = 10;
    int counter = 0;
    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        linearLayout = findViewById(R.id.linear);
        increase = findViewById(R.id.btn_increase);
        decrease = findViewById(R.id.btn_decrease);

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