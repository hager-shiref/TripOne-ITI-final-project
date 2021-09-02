package com.Team.Tripawy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private EditText ed_mail,ed_pass,ed_confpass;
    private Button btn_sign;
    TextView back;
    FirebaseAuth mAuth=null;
    FirebaseUser currentUser;
    ProgressBar signProgress;
    private  String mail,password,confirmpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initComponent();
        mAuth=FirebaseAuth.getInstance();
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent=new Intent(Signup.this, Login.class);
                startActivity(backIntent);
            }
        });
    }
    private void signup() {
        mail = ed_mail.getText().toString();
        password = ed_pass.getText().toString();
        confirmpass = ed_confpass.getText().toString();
        signProgress.setVisibility(View.VISIBLE);
        if (mail.isEmpty() && password.isEmpty() && confirmpass.isEmpty()) {
            Toast.makeText(Signup.this, "Enter Data", Toast.LENGTH_SHORT).show();
        } else if (mail.isEmpty()) {
            Toast.makeText(Signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (!password .equals(confirmpass ) || password.isEmpty() || confirmpass.isEmpty()) {
            Toast.makeText(Signup.this, "password not match", Toast.LENGTH_SHORT).show();
            ed_pass.setText(null);
            ed_confpass.setText(null);

        } else {

            mAuth.createUserWithEmailAndPassword(ed_mail.getText().toString(), ed_pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signProgress.setVisibility(View.VISIBLE);
                                sendEmail();
                                signProgress.setVisibility(View.INVISIBLE);
                                Signup.this.finish();

                            } else {
                                Toast.makeText(Signup.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private  void sendEmail()
    {
        FirebaseUser user =mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Signup.this, "Successful send email", Toast.LENGTH_SHORT).show();
                    Intent logIntent=new Intent(Signup.this, Login.class);
                    startActivity(logIntent);

                }
                else
                {
                    Toast.makeText(Signup.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initComponent() {
        ed_confpass=findViewById(R.id.edt_pass_conf);
        ed_mail=findViewById(R.id.edt_mail);
        ed_pass=findViewById(R.id.edt_pass);
        back=findViewById(R.id.back);
        btn_sign=findViewById(R.id.btn_sign);
        signProgress=findViewById(R.id.signProgress);
    }
}