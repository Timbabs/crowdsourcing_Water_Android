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
public class WaterPurityReportData {

    public String waterReportNumber;
    public String name;
    public String locationName;
    public String latitude;
    public String longitude;
    public String waterCondition;
    public String virusPPM;
    public String contaminantPPM;
    public String year;
    public String month;

    //Constructor for DataSnapshot. Enabling user's info to be pulled back from firebase
    public WaterPurityReportData() {

    }

    public WaterPurityReportData (String waterReportNumber, String name, String locationName, String latitude,
                            String longitude, String waterCondition, String virusPPM, String contaminantPPM, String year, String month)  {
        this.waterReportNumber = waterReportNumber;
        this.name = name;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.waterCondition = waterCondition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
        this.year = year;
        this.month = month;
    }

    /**
     * Fetches the report in string for display
     * @return string of text
     */
    public String getReport(){
        String list = "Water Report Number : " + waterReportNumber
                + "\nName : " + name
                + "\nLocation Name : " + locationName
                + "\nLatitude : " + latitude
                + "\nLongitude : " + longitude
                + "\nWater Condition : " + waterCondition
                + "\nVirus PPM : " + virusPPM
                + "\nContaminant PPM : " + contaminantPPM
                + "\nYear : " + year
                + "\nMonth : " + month;
        return list;
    }
}