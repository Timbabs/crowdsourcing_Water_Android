package com.gatech.edu.soloTechno.m4_login.model;

/**
 * Created by timothybaba on 3/5/17.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String firstName;
    public String lastName;
    public String accountType;
    public String email;

    public User() {

    }

    public User (String firstNameame, String lastName, String accountType, String email)  {
        this.firstName = firstNameame;
        this.lastName = lastName;
        this.accountType = accountType;
        this.email = email;
    }
}

