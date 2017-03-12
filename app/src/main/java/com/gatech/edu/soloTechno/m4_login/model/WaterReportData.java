package com.gatech.edu.soloTechno.m4_login.model;

/**
 * Created by timothybaba on 3/12/17.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class WaterReportData {

    public String waterReportNumber;
    public String name;
    public String locationName;
    public String location;
    public String waterType;
    public String waterCondition;

    public WaterReportData() {

    }

    public WaterReportData (String waterReportNumber, String name, String locationName, String location,
                            String waterType, String waterCondition)  {
        this.waterReportNumber = waterReportNumber;
        this.name = name;
        this.locationName = locationName;
        this.location = location;
        this.waterType = waterType;
        this.waterCondition = waterCondition;
    }
}
