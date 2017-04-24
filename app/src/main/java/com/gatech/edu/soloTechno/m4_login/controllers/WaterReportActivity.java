package com.gatech.edu.soloTechno.m4_login.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.DatePickerFragment;
import com.gatech.edu.soloTechno.m4_login.model.TimePickerFragment;
import com.gatech.edu.soloTechno.m4_login.model.User;
import com.gatech.edu.soloTechno.m4_login.model.WaterSourceReportData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Joshua on 3/3/2017.
 */

public class WaterReportActivity extends FragmentActivity {
    // private field to get the location's name
    private CharSequence locationName;
    private EditText name;
    private EditText waterReportNumber;
    private Spinner waterTypeSpinner;
    private Spinner waterConditionSpinner;
    private Button saveButton;
    private static ArrayList<String> waterLogger;
    // private field to get the longitude and latitude of location
    private LatLng locationLatLng;
    private FirebaseAuth mAuth;
    private static int mutator;
    private String s = "";
    private DatabaseReference mFirebaseDatabase =FirebaseDatabase.getInstance().getReference("users");
    MainActivity main = new MainActivity();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Water report initialization
        waterLogger = new ArrayList<String>();
        loadArray(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_report);

        saveButton = (Button) findViewById(R.id.save_button);
        name = (EditText) findViewById(R.id.water_report_username);

        final DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference("water source reports");
        mAuth = FirebaseAuth.getInstance();

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

        // Instantiate PlaceAutocompleteFragment for use.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Sets the listener to respond when the user selects a location from the search list
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                locationLatLng = place.getLatLng();
                locationName = place.getName();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("water source reports");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        if(dataSnapshot.getValue() != null){
                            for (Map.Entry<String, Object> entry : ((Map<String,Object>) dataSnapshot.getValue()).entrySet()) {
                                // Get user map
                                Map singleUser = (Map) entry.getValue();
                                s  = s + ((String) singleUser.get("locationName"))
                                        + " submitted by "
                                        + ((String) singleUser.get("name"))
                                        + " of "
                                        + ((String) singleUser.get("waterType"))
                                        + " type with condition "
                                        + ((String) singleUser.get("waterCondition"))
                                        + ". Report number: "
                                        + ((String) singleUser.get("waterReportNumber"))
                                        + System.lineSeparator()
                                        + System.lineSeparator()
                                        + System.lineSeparator();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });




        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // pending:: submit water report to firebase DB
                Map<String,String> wReport = new HashMap<>();
                wReport.put("Water Report Number", waterReportNumber.getText().toString());
                wReport.put("Name", name.getText().toString());
                wReport.put("Location Name", locationName.toString());
                wReport.put("latitude", String.valueOf(locationLatLng.latitude));
                wReport.put("longitude", String.valueOf(locationLatLng.longitude));
                wReport.put("Water Type", waterTypeSpinner.toString());
                wReport.put("Water Condition", waterConditionSpinner.getSelectedItem().toString());
               // myFirebaseRef.push().setValue(wReport);

                String userId = mAuth.getCurrentUser().getUid() + mutator++;

                WaterSourceReportData waterSourceReportData = new WaterSourceReportData( waterReportNumber.getText().toString(),
                        name.getText().toString(), locationName.toString(), String.valueOf(locationLatLng.latitude),
                        String.valueOf(locationLatLng.longitude), waterTypeSpinner.getSelectedItem().toString(),
                        waterConditionSpinner.getSelectedItem().toString());

                myFirebaseRef.child(userId).setValue(waterSourceReportData);

                // shows a brief outline of the report that the user just submitted
                waterLogger.add("Water Report Number: " + wReport.get("Water Report Number") + " Name: "
                        + wReport.get("Name") + " Location Name : " + wReport.get("Location Name")
                        + " latitude: " + wReport.get("latitude") + " longitude: " + wReport.get("longitude")
                        + " Water Type: " + wReport.get("Water Type") + " Water Condition: " + wReport.get("Water Condition")
                );

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            String st = "";
                            new AlertDialog.Builder(WaterReportActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Currently saved reports are: " + s)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Whatever...
                                            // when successfully saved user water report, direct user to main screen
                                            saveArray();
                                            String accountType = getIntent().getStringExtra("ACCOUNT_TYPE");
                                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                            mainActivity.putExtra("ACCOUNT_TYPE", accountType);
                                            startActivity(mainActivity);
                                        }
                                    }).show();
                        }
                    }
                });

                // when successfully saved user water report, direct user to main screen
                /*Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);*/
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
    * Displays f range for user input
    *
    * @Param v View for date picker
    * */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Hack to save the array in app
     * this ensures that my view reports always work
     * @return if the array was saved or not
     */
    public boolean saveArray()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* sKey is an array */
        mEdit1.putInt("Status_size", waterLogger.size());

        for(int i=0;i<waterLogger.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, waterLogger.get(i));
        }

        return mEdit1.commit();
    }

    /**
     * Internal hack that helps load the last saved array
     * @param mContext the context of this app
     * final state ensures that waterLogger is perfect
     */
    public static void loadArray(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        waterLogger.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            waterLogger.add(mSharedPreference1.getString("Status_" + i, null));
        }

    }
}

