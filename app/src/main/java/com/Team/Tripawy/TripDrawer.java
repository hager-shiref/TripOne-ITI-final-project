package com.Team.Tripawy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.Team.Tripawy.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class TripDrawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String userEmail ;
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        userEmail=getIntent().getStringExtra(Login.EMAIL);

        TextView textView = binding.navView.getHeaderView(0).findViewById(R.id.userEmail);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(TripDrawer.this);
        if(signInAccount!=null)
        {
            textView.setText(signInAccount.getEmail());
        }

        binding.appBarMain.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripDrawer.this, AddTrip.class);
                startActivity(intent);
                Log.i("email","email : "+ userEmail);

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (!handled) {
                    if (item.getItemId() == R.id.sync) {
                        //try to syns data with firebase

                    } else if (item.getItemId() == R.id.logout) {
                        //try to delete local data and log user out
                        FirebaseAuth.getInstance().signOut();
                        Intent logoutIntent=new Intent(TripDrawer.this,Login.class);
                        startActivity(logoutIntent);
                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return handled;

            }
        });
        View headView =navigationView.getHeaderView(0);
        email=(TextView) headView.findViewById(R.id.userEmail);
        email.setText(userEmail);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}