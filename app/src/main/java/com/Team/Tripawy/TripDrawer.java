package com.Team.Tripawy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.databinding.ActivityMainBinding;
import com.Team.Tripawy.models.Trip;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class TripDrawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String userEmail ;
    TextView email;
    private List<Trip> tripList;

    @Override
    protected void onStart() {
        super.onStart();
        LiveData<List<Trip>> listLiveData = RDB.getTrips(getApplicationContext()).getAll();
        listLiveData.observe(this, trips -> tripList = trips);
    }

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
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String id= user.getUid();
                        sync(id);

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
    public void sync (String uId){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=db.getReference("Trips");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(uId).child("Trips").setValue(tripList);
                Toast.makeText(TripDrawer.this,"Data Saved",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TripDrawer.this,"failed to add data"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}