package com.gatech.edu.soloTechno.m4_login.model;

/**
 * Created by timothybaba on 3/12/17.
 */
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class WaterReportData {

    public String waterReportNumber;
    public String name;
    public String locationName;
    public String latitude;
    public String longitude;
    public String waterType;
    public String waterCondition;

    //Constructor for DataSnapshot. Enabling user's info to be pulled back from firebase
    public WaterReportData() {

    }

    public WaterReportData (String waterReportNumber, String name, String locationName, String latitude,
                            String longitude, String waterType, String waterCondition)  {
        this.waterReportNumber = waterReportNumber;
        this.name = name;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.waterType = waterType;
        this.waterCondition = waterCondition;
    }
}