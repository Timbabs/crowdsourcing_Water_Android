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
    public String password;

    //Constructor for DataSnapshot. Enabling user's info to be pulled from firebase
    public User() {

    }

    //Constructor for saving passed in user's info
    public User (String firstName, String lastName, String accountType, String email, String password)  {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
        this.email = email;
        this.password = password;
    }

}

