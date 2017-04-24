package com.gatech.edu.soloTechno.m4_login.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.WaterPurityListAdapter;
import com.gatech.edu.soloTechno.m4_login.model.WaterPurityReportData;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class WaterPurityListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WaterPurityReportData> purityList = new ArrayList<>();

    private DatabaseReference mFirebaseDatabase;
    private Map purityReportMap;
    private Map singleUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_purity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
       mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("water purity reports");

        // Fetches the needed purity information from database
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Gets information from database using Map
                        int i = 0;
                        if(dataSnapshot.getValue() != null) {
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) dataSnapshot.getValue()).entrySet()){
                                //Get user map
                                singleUser = (Map) entry.getValue();
                                String latitude = (String) singleUser.get("latitude");
                                String longitude = (String) singleUser.get("longitude");
                                String waterReportNumber = (String) singleUser.get("waterReportNumber");
                                String contaminantPPM = (String) singleUser.get("contaminantPPM");
                                String name = (String) singleUser.get("name");
                                String locationName = (String) singleUser.get("locationName");
                                String waterCondition = (String) singleUser.get("waterCondition");
                                String virusPPM = (String) singleUser.get("virusPPM");
                                String year = (String) singleUser.get("year");
                                String month = (String) singleUser.get("month");

                                //Add each data into the list to display on Recycler View
                                purityList.add(new WaterPurityReportData(waterReportNumber, name, locationName, latitude, longitude
                                        , waterCondition, virusPPM, contaminantPPM, year, month));
                                System.out.println(purityList.get(i++));
                            }
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });

        mAdapter = new WaterPurityListAdapter(getBaseContext(), purityList);
        mRecyclerView.setAdapter(mAdapter);

    }
}
