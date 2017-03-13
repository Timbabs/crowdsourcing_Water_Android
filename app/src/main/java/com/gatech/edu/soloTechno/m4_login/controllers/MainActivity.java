package com.gatech.edu.soloTechno.m4_login.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.WaterReportData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Declare Firebase Authentication.
     */
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    public static GoogleMap mMap;

    private NavigationView navigationView;
    private DrawerLayout drawer;


    private String locationName;
    private String name;
    private String waterReportNumber;
    private String waterType;
    private String waterCondition;
    private String latitude;
    private String longitude;

    private DatabaseReference mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");

        /**
         * Displays a welcome message to the AppBar once a user is successfully logged in.
         */
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    getSupportActionBar().setTitle("Welcome, " + user.getDisplayName() + "!");
                    if (RegisterActivity.firstName!= null && user.getDisplayName() != RegisterActivity.firstName) {
                        getSupportActionBar().setTitle("Welcome, " + RegisterActivity.firstName + "!");
                    }
                    initMap();
                }
            }
        };
    }

    private void waterReportData() {
         mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WaterReportData data = dataSnapshot.getValue(WaterReportData.class);
                locationName = data.locationName;
                name = data.name;
                waterReportNumber = data.waterReportNumber;
                waterType = data.waterType;
                waterCondition = data.waterCondition;
                latitude = data.latitude;
                longitude = data.longitude;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.");
            }
        });
    }

    /*
    * Initializes Google map
    *
    * */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Re-order z-index of a sidebar menu
        navigationView.bringToFront();
        drawer.requestLayout();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("water reports");
        //loop over. Get datasnapshot at root node

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectLatitudeLongitude((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private Map singleUser = null;

    //Then loop through users, accessing their map and collecting the phone field.
    private void collectLatitudeLongitude(Map<String,Object> reports) {
        //iterate through each user
        for (Map.Entry<String, Object> entry : reports.entrySet()){
            //Get user map
            singleUser = (Map) entry.getValue();
            String latitude = (String) singleUser.get("latitude");
            String longitude = (String) singleUser.get("longitude");

            MainActivity.mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                    .title(
                            ((String) singleUser.get("locationName"))
                                    + " submitted by "
                                    + ((String) singleUser.get("name"))

                    )
                    .snippet(
                            ((String) singleUser.get("waterType"))
                                    + " type with condition "
                                    + ((String) singleUser.get("waterCondition"))
                                    + ". Report number: "
                                    + ((String) singleUser.get("waterReportNumber"))
                    )

            );
            MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));

        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            /**
             * Uses firebase built-in signout method to a sign out users of their current session
             */
            FirebaseAuth.getInstance().signOut();

            Intent logoutActivity = new Intent(getApplicationContext(), LoginActivity.class);
            logoutActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutActivity);
            finish();
        } else if (id == R.id.nav_edit_profile) {
            Intent editProfileActivity = new Intent(getApplicationContext(), EditProfileActivity.class);
            editProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(editProfileActivity);
            finish();

        } else if (id == R.id.nav_water_report) {
            Intent waterReportActivity = new Intent(getApplicationContext(), WaterReportActivity.class);
            startActivity(waterReportActivity);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(context);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(marker.getTitle());

        TextView snippet = new TextView(context);
        snippet.setTextColor(Color.GRAY);
        snippet.setText(marker.getSnippet());

        info.addView(title);
        info.addView(snippet);

        return info;
    }


}
