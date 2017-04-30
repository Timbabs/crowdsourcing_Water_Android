package com.gatech.edu.soloTechno.m4_login.controllers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gatech.edu.soloTechno.m4_login.R;
import com.gatech.edu.soloTechno.m4_login.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private String databaseID;
    private ProgressDialog mProgressDialog;

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


        databaseID = mAuth.getCurrentUser().getUid();
                mFirebaseDatabase.child(databaseID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(!TextUtils.isEmpty(password) || !TextUtils.isEmpty(confirmPassword)) {
                    validPassword = isValidPassword(password, confirmPassword);
                    if (!validPassword)
                        return;
                }

                if (!validEmail || !validFirstName || !validLastName)
                    return;

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

               final AlertDialog.Builder alert = new AlertDialog.Builder(EditProfileActivity.this);

                alert.setTitle("User Validation");
                alert.setMessage("Enter your current password");

                // Set an EditText view to get user input
                final EditText currentPassword = new EditText(EditProfileActivity.this);
                currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                currentPassword.setHint("Current Password");
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(currentPassword);
                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                final AlertDialog dialog = alert.create();

                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String myPassword;
;                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(currentPassword.getText().toString().isEmpty()) {
                            myPassword = "1";
                        } else {
                            myPassword = currentPassword.getText().toString();
                        }

                        AuthCredential authCredential;
                        if(mAuth.getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                            System.out.println(LoginActivity.signInAccount.getIdToken());
                            System.out.println(LoginActivity.signInAccount.getId());
                            authCredential = GoogleAuthProvider.getCredential(LoginActivity.signInAccount.getIdToken(), null);
                            //authCredential = GoogleAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), myPassword);

                        } else {
                            authCredential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), myPassword);

                        }




                        mAuth.getCurrentUser().reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    showProgressDialog();
                                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user != null) {
                                                String userID = mAuth.getCurrentUser().getUid();
                                                mFirebaseDatabase.child(userID).child("firstName").setValue(firstName);
                                                mFirebaseDatabase.child(userID).child("lastName").setValue(lastName);
                                                mFirebaseDatabase.child(userID).child("accountType").setValue(accountType);
                                                mFirebaseDatabase.child(userID).child("email").setValue(email);
                                                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                                                    mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                                                                        .setDisplayName(firstName)
                                                                        .build();

                                                                mAuth.getCurrentUser().updateProfile(addProfileName)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    mAuth.removeAuthStateListener(mAuthListener);
                                                                                    hideProgressDialog();
                                                                                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    intent.putExtra("ACCOUNT_TYPE", accountType);
                                                                                    intent.putExtra("INFO_UPDATE", "true");
                                                                                    startActivity(intent);
                                                                                    Log.d(TAG, "");
                                                                                }
                                                                            }

                                                                        });
                                                            } else {
                                                                Log.d(TAG, "Error password not updated");
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    mAuth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                                                                                    .setDisplayName(firstName)
                                                                                    .build();

                                                                            mAuth.getCurrentUser().updateProfile(addProfileName)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                mAuth.removeAuthStateListener(mAuthListener);
                                                                                                hideProgressDialog();
                                                                                                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                intent.putExtra("ACCOUNT_TYPE", accountType);
                                                                                                intent.putExtra("INFO_UPDATE", "true");
                                                                                                startActivity(intent);
                                                                                                Log.d(TAG, "");
                                                                                            }
                                                                                        }

                                                                                    });
                                                                        } else {
                                                                            Log.d(TAG, "Error password not updated");
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                Log.d(TAG, "Error password not updated");

                                                            }
                                                        }
                                                    });

                                                }




                                            }

                                        }

                                    };
                                    mAuth.addAuthStateListener(mAuthListener);
                                    dialog.dismiss();

                                } else {
                                    currentPassword.setError("Invalid password");
                                    currentPassword.requestFocus();


                                }

                            }
                        });

                    }
                });


            }

        });



        final Button delete_Account = (Button) findViewById(R.id.delete_account);
        delete_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteProgressDialog();
                FirebaseUser user = mAuth.getCurrentUser();

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    hideDeleteProgressDialog();
                                    Log.d(TAG, "User account deleted.");
//                                    TextView status_Msg = (TextView) findViewById(R.id.account_status);
//                                    status_Msg.setText("User Account Deleted");
//                                    status_Msg.setTextColor(Color.RED);
                                    mFirebaseDatabase.child(databaseID).removeValue();
                                    Intent logoutActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                    logoutActivity.putExtra("Deleted", true);
                                    logoutActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(logoutActivity);
                                } else {

                                }
                            }
                        });
            }
        });

    }
    private void showDeleteProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Deleting account...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideDeleteProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Updating profile...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
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
                firstName_text.setError("Please enter your first name");
            } else {
                lastName_text.setError("Please enter your last name");
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
