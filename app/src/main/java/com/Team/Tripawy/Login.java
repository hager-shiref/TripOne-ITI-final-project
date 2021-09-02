package com.Team.Tripawy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    private EditText ed_email, ed_pass;
    private Button btn_log;
    TextView sign;
    ImageButton btnGoogle;
    private ProgressBar loginProgress;
    FirebaseAuth mAuth =null;
    String email_edt, pass_edt;
    FirebaseUser currentUser;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    public static final String EMAIL="email";
    public static final String NAME="name";
    FirebaseUser user ;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        mAuth = FirebaseAuth.getInstance();

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIntent = new Intent(Login.this, Signup.class);
                startActivity(signIntent);

            }
        });
        createRequest();
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Intent mainIntent = new Intent(Login.this, TripDrawer.class);
                            startActivity(mainIntent);
                            Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Sorry auth Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void login() {
        loginProgress.setVisibility(View.VISIBLE);
        email_edt = ed_email.getText().toString();
        pass_edt = ed_pass.getText().toString();
        if (email_edt.isEmpty()) {
            Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (pass_edt.isEmpty()) {
            Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email_edt, pass_edt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loginProgress.setVisibility(View.INVISIBLE);
                        verfiy();
                        Login.this.finish();
                    } else {
                        Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void verfiy() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser.isEmailVerified()) {
            Intent mainIntent = new Intent(Login.this, TripDrawer.class);
            startActivity(mainIntent);
            Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Login.this, "please Verfiy your acount", Toast.LENGTH_SHORT).show();
        }
    }

     protected void onStart() {
          super.onStart();
          if(FirebaseAuth.getInstance().getCurrentUser()!=null)
          {
              user = mAuth.getCurrentUser();
              email=user.getEmail();
              Intent intent=new Intent(Login.this, TripDrawer.class);
              intent.putExtra(EMAIL,email);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
          }
      }
    private void initComponent() {
        btn_log = findViewById(R.id.btn_log);
        sign = findViewById(R.id.sign1);
        ed_email = findViewById(R.id.edt_email1);
        ed_pass = findViewById(R.id.edt_password1);
        loginProgress = findViewById(R.id.loginProgress);
        btnGoogle=findViewById(R.id.google_btn);
    }
}