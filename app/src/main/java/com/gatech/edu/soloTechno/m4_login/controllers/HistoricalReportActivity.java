package com.gatech.edu.soloTechno.m4_login.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gatech.edu.soloTechno.m4_login.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by shong313 on 4/3/17.
 */

public class HistoricalReportActivity extends FragmentActivity {
    private Button submitButton;
    private Spinner yearSpinner;
    private Spinner ppmSpinner;
    private Spinner locationSpinner;
    static String data1;
    static String data2;
    static String data3;
    //static ArrayList locations = new ArrayList();
    static ArrayList years = new ArrayList();
    static ArrayList virus = new ArrayList();
    static ArrayList contaminants = new ArrayList();
    private ArrayList<String> options = new ArrayList<>(Arrays.asList("select", "Year", "Virus", "Contaminants"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicalreport_setup);

        submitButton = (Button) findViewById(R.id.submit_button);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ppmSpinner = (Spinner) findViewById(R.id.virusPPM_spinner);
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);

        /*ArrayAdapter<CharSequence> ppmAdapter = ArrayAdapter.createFromResource(this,
                R.array.ppm_array, android.R.layout.simple_spinner_item);
        ppmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ppmSpinner.setAdapter(ppmAdapter);*/

        final ArrayAdapter<String> ppmAdapter = new ArrayAdapter<String>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, options);
        ppmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ppmSpinner.setAdapter(ppmAdapter);

        DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference("water purity reports");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    // String locationName = areaSnapshot.child("locationName").getValue(String.class);
                    String yearData = areaSnapshot.child("year").getValue(String.class);
                    //Try this
                   // String yearData = (String)areaSnapshot.child("year").getValue();
                    String virusData = areaSnapshot.child("virusPPM").getValue(String.class);
                    String contaminantsData = areaSnapshot.child("contaminantPPM").getValue(String.class);
                    // locations.add(locationName);
                    years.add(yearData);
                    virus.add(virusData);
                    contaminants.add(contaminantsData);
                }

                /*ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, locations);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(locationAdapter);

                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, years);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);*/

                ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, options);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(locationAdapter);

                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(HistoricalReportActivity.this, android.R.layout.simple_spinner_item, options);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data2 = ppmSpinner.getSelectedItem().toString().trim() == "select"? null: ppmSpinner.getSelectedItem().toString().trim();
                data1 = locationSpinner.getSelectedItem().toString().trim() == "select"? null: locationSpinner.getSelectedItem().toString().trim();
                data3 = yearSpinner.getSelectedItem().toString().trim() == "select"? null: yearSpinner.getSelectedItem().toString().trim();

                boolean cancel = false;
                if(data1 == null) {
                    TextView text= (TextView) findViewById(R.id.water_report_location);
                    text.setError("Select an item");
                    text.requestFocus();
                    cancel = true;
                }
                if(data2 == null) {
                    TextView text= (TextView) findViewById(R.id.water_type_spinner_text);
                    text.setError("Select an item");
                    text.requestFocus();
                    cancel = true;
                }
                if(years.isEmpty() || virus.isEmpty() || contaminants.isEmpty()) {
                    Toast toast =  Toast.makeText(HistoricalReportActivity.this, "You have no data to plot. Please submit more reports!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                    TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
                    view.setTextColor(Color.RED);
                    toast.show();
                    cancel = true;

                }

                if(!cancel) {
                    Intent graphDisplay = new Intent(HistoricalReportActivity.this, DisplayGraphActivity.class);
                    startActivity(graphDisplay);
                }




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
