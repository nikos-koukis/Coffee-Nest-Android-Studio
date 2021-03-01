package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.coffeenest.Dialogs.DeleteUserAccountDialog;
import com.example.coffeenest.Dialogs.UserLogOutDialog;
import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {

    //Init Variables
    ImageButton myAccountBackButton;
    EditText emailInputText;
    EditText usernameInputText;
    TextInputLayout layoutEmail;
    TextInputLayout layoutUsername;
    Button buttonUpdate;
    Button buttonLogOut;
    Button buttonDeleteAccount;
    Button buttonChangePass;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        //Hook Elements From xml File - activity_my_account.xml
        myAccountBackButton = findViewById(R.id.my_account_back_button);
        emailInputText = findViewById(R.id.email_input_text);
        layoutEmail = findViewById(R.id.layout_email);
        usernameInputText = findViewById(R.id.username_input_text);
        layoutUsername = findViewById(R.id.layout_username);
        buttonUpdate = findViewById(R.id.btn_update);
        buttonLogOut = findViewById(R.id.btn_logout);
        buttonDeleteAccount = findViewById(R.id.btn_delete_acc);
        buttonChangePass = findViewById(R.id.btn_change_pass);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();


        //My Account Back Button Listener
        //When My Account Back Button pressed, then User Profile Activity will started
        myAccountBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, UserProfileActivity.class));
            }
        });


        //Replace the Text of TextView emailInputText with the email of user after Log In
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String email = userProfile.email;
                    String username = userProfile.username;
                    emailInputText.setText(email);
                    //Disable Email Input Text
                    emailInputText.setEnabled(false);
                    usernameInputText.setText(username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Button Update Listener
        //When Button Update pressed, then update username of user
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username_text = usernameInputText.getText().toString().trim();

                //Input Validation
                if (username_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                } else {
                    progressDialog = new ProgressDialog(MyAccountActivity.this);
                    progressDialog.setMessage("Παρακαλώ περιμένετε..."); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                            String userID = user.getUid();
                            reference.child(userID).child("username").setValue(username_text);
                            startActivity(getIntent()); //refresh activity
                        }
                    }).start();
                    Toast.makeText(MyAccountActivity.this, "Το όνομα χρήστη άλλαξε επιτυχώς", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //If an input text has error, when user type inside the edit_text, then the error disable

        usernameInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutUsername.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Button Change Pass Listener
        //When Button Change Pass pressed, then Change Password Activity will started
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity
                startActivity(new Intent(MyAccountActivity.this, ChangePasswordActivity.class));
            }
        });

        //Button Log Out Listener
        //When Button Log Out pressed, then user will logged out
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogOutDialog();

            }
        });

        //Button Delete Account Listener
        //When Button Delete Account pressed, then user will deleted permanently from firebase
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteUserAccountDialog();
            }
        });


    }

    private void deleteUserAccountDialog() {
        DeleteUserAccountDialog deleteUserAccountDialog = new DeleteUserAccountDialog();
        deleteUserAccountDialog.show(getSupportFragmentManager(), "Delete User Account");
    }

    private void userLogOutDialog() {
        UserLogOutDialog userLogOutDialog = new UserLogOutDialog();
        userLogOutDialog.show(getSupportFragmentManager(), "User Log Out");
    }

    //Check For Internet Connection
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    //When user click back android button
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyAccountActivity.this, UserProfileActivity.class));
    }
}