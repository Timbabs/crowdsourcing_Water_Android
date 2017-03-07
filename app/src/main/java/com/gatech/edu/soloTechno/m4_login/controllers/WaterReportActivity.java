package com.gatech.edu.soloTechno.m4_login.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gatech.edu.soloTechno.m4_login.model.DatePickerFragment;
import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.TimePickerFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Joshua on 3/3/2017.
 */

public class WaterReportActivity extends FragmentActivity {
    private EditText location;
    private EditText name;
    private EditText waterReportNumber;
    private Spinner waterTypeSpinner;
    private Spinner waterConditionSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_report);

        saveButton = (Button) findViewById(R.id.save_button);
        name = (EditText) findViewById(R.id.water_report_username);
        location = (EditText) findViewById(R.id.water_report_location);
        final DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference("water reports");

        // initially auto generated code for water report
        final Random rand = new Random();
        waterReportNumber = (EditText) findViewById(R.id.water_report_number);
        waterReportNumber.setEnabled(false);
        // ramdom number range from 1 to 1000
        waterReportNumber.setText(Integer.toString(rand.nextInt(1000) + 1));

        // water type & condition spinners
        waterTypeSpinner = (Spinner) findViewById(R.id.water_type_spinner);
        waterConditionSpinner = (Spinner) findViewById(R.id.water_condition_spinner);

        ArrayAdapter<CharSequence> waterTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.water_type_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> waterConditionAdapter = ArrayAdapter.createFromResource(this,
                R.array.water_condition_array, android.R.layout.simple_spinner_item);

        waterTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        waterTypeSpinner.setAdapter(waterTypeAdapter);
        waterConditionSpinner.setAdapter(waterConditionAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // pending:: submit water report to firebase DB
                Map<String,String> wReport = new HashMap<>();
                wReport.put("Water Report Number", waterReportNumber.getText().toString());
                wReport.put("Name", name.getText().toString());
                wReport.put("Location", location.getText().toString());
                wReport.put("Water Type", waterTypeSpinner.getSelectedItem().toString());
                wReport.put("Water Condition", waterConditionSpinner.getSelectedItem().toString());
                myFirebaseRef.push().setValue(wReport);

                // when successfully saved user water report, direct user to main screen
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    /*
    * Displays dates range for user input
    *
    * @Param v View for date picker
    * */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*
    * Displays time range for user input
    *
    * @Param v View for date picker
    * */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
