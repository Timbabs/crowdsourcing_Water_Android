package com.gatech.edu.soloTechno.m4_login.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gatech.edu.soloTechno.m4_login.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shong313 on 4/3/17.
 */

public class HistoricalReportActivity extends FragmentActivity {
    private Button submitButton;
    private Spinner yearSpinner;
    private Spinner ppmSpinner;
    private Spinner locationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicalreport_setup);

        submitButton = (Button) findViewById(R.id.submit_button);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ppmSpinner = (Spinner) findViewById(R.id.virusPPM_spinner);
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);

        ArrayAdapter<CharSequence> ppmAdapter = ArrayAdapter.createFromResource(this,
                R.array.ppm_array, android.R.layout.simple_spinner_item);
        ppmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ppmSpinner.setAdapter(ppmAdapter);

        DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference("water purity reports");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> locations = new ArrayList<>();
                final List<String> years = new ArrayList<>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String locationName = areaSnapshot.child("locationName").getValue(String.class);
                    String yearData = areaSnapshot.child("locationName").getValue(String.class);
                    locations.add(locationName);
                }

                ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, locations);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(locationAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
    }
}
