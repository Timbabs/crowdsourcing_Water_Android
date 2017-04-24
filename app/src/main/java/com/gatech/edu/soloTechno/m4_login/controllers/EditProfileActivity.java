package com.gatech.edu.soloTechno.m4_login.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Random;

/**
 * Created by timothybaba on 3/6/17.
 */


public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private Spinner accountTypeSpinner;
    private EditText firstName_text;
    private EditText lastName_text;
    private EditText email_text;
    private EditText password_text;
    private EditText confirmPassword_text;
    private Button updateButton;
    private ProgressDialog mAuthProgressDialog;

    private String confirmPassword;
    private boolean validEmail;
    private boolean validPassword;
    private boolean validFirstName;
    private boolean validLastName;


    private String accountType;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    //private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static List<String> accounts = Arrays.asList("Manager", "Worker", "Admin", "User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        accountType = getIntent().getStringExtra("ACCOUNT_TYPE");

        mAuth = FirebaseAuth.getInstance();

       accountTypeSpinner = (Spinner) findViewById(R.id.spinner4);
        firstName_text = (EditText) findViewById(R.id.first_Name);
        lastName_text = (EditText) findViewById(R.id.last_Name);
        email_text = (EditText) findViewById(R.id.email);
        password_text = (EditText) findViewById(R.id.password);
        confirmPassword_text = (EditText) findViewById(R.id.confirm_Password);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        //set user's info
        //accountTypeSpinner.;
        //firstName_text.setText(mAuth.getCurrentUser().getDisplayName());
       // email_text.setText(mAuth.getCurrentUser().getEmail());

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);


        mFirebaseDatabase.child(mAuth.getCurrentUser().getDisplayName().split(" ")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(User.class) != null) {
                    User user = dataSnapshot.getValue(User.class);
                    firstName_text.setText(user.firstName);
                    lastName_text.setText(user.lastName);
                    email_text.setText(user.email);
                    password_text.setText(user.password);
                    confirmPassword_text.setText(user.password);
                    String prevAccountType = user.accountType;
                    switch (prevAccountType) {
                        case "Manager": accountTypeSpinner.setSelection(0);
                            break;
                        case "Worker": accountTypeSpinner.setSelection(1);
                            break;
                        case "Admin": accountTypeSpinner.setSelection(2);
                            break;
                        case "User": accountTypeSpinner.setSelection(3);
                            break;
                        default: accountTypeSpinner.setSelection(3);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.");
            }
        });

       // firstName_text.setText(mAuth.getCurrentUser().getDisplayName());
       // email_text.setText(mAuth.getCurrentUser().getEmail());
       // password_text.setText(LoginActivity.mAuth.getCurrentUser().getProviderData().);
       // lastName_text.setText(mFirebaseDatabase.child(mAuth.getCurrentUser().getUid()).child(lastName).g);



        updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                createAuthProgressDialog();
               // mAuthProgressDialog.show();

                //get user's info
                accountType = accountTypeSpinner.getSelectedItem().toString().trim();
                email = email_text.getText().toString().trim();
                firstName = firstName_text.getText().toString().trim();
                lastName = lastName_text.getText().toString().trim();
                password = password_text.getText().toString().trim();
                confirmPassword = confirmPassword_text.getText().toString().trim();

                validEmail = isValidEmail(email);
                validFirstName = isValidName(firstName);
                validLastName = isValidName(lastName);
                validPassword = isValidPassword(password, confirmPassword);
                if (!validEmail || !validFirstName || !validLastName || !validPassword)
                    return;

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Captures and stores updated user's info in firebase
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String userName = mAuth.getCurrentUser().getDisplayName().split(" ")[0];
                            mFirebaseDatabase.child(userName).child("firstName").setValue(firstName);
                            mFirebaseDatabase.child(userName).child("lastName").setValue(lastName);
                            mFirebaseDatabase.child(userName).child("accountType").setValue(accountType);
                            if(!getIntent().hasExtra("GoogleSigned")) {
                                mFirebaseDatabase.child(userName).child("password").setValue(password);
                            }
                            mFirebaseDatabase.child(userName).child("email").setValue(email);

                            mAuth.getCurrentUser().updateEmail(email);
                            mAuth.getCurrentUser().updatePassword(password);



                            UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName)
                                    .build();

                            mAuth.getCurrentUser().updateProfile(addProfileName)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Log.d(TAG, user.getDisplayName());
                                                Log.d(TAG, "");
                                            }
                                        }

                                    });

                           /* mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(EditProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(EditProfileActivity.this, "Log in failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();

                                            } else {

                                                Log.d(TAG, "Authentication successful");


                                                //    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                                //   startActivity(mainActivity);


                                            }
                                        }
                                    });*/

                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("ACCOUNT_TYPE", accountType);
                            startActivity(intent);
                            //finish();



                        } else {
                            // User is signed out
                        }
                    }

                };
                mAuth.addAuthStateListener(mAuthListener);
               // mAuthProgressDialog.dismiss();

            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("ACCOUNT_TYPE", accountType);
        startActivity(intent);
        //finish();
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Updating info...");
        mAuthProgressDialog.setCancelable(false);
    }

    /**
     * Uses an Android pattern to check if an entered email is in the correct format. If the email
     * is not valid, it displays an error in the email_text
     * @param email email entered by a user
     * @return whether an email is valid or not
     */
    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            email_text.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    /**
     * Ensures the name field has not been left blank. It it has, it displays an error in either
     * the firstName_text or the lastName_text depending on which has been left blank
     * @param name name entered by a user
     * @return whether a name field is blank or not
     */
    private boolean isValidName(String name) {
        if (name.equals("")) {
            if (name.equals(firstName_text.getText().toString().trim())) {
                firstName_text.setError("Please enter your name");
            } else {
                lastName_text.setError("Please enter your name");
            }

            return false;
        }
        return true;
    }

    /**
     * Confirms that an entered password is atleast 6 characters long, and ensures the password and
     * the password confirmation fields match. if not the case, it displays an error  int the
     * password_text
     * @param password password entered in the password field
     * @param confirmPassword password entered in the confirm password field
     * @return whether password is 6 characters long and both the password and confirmPassword fields
     * match
     */

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            password_text.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            password_text.setError("Passwords do not match");
            return false;
        }
        return true;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}
