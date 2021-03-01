package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.example.coffeenest.Listener.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    //Init Variables
    ImageButton changePassBackButton;
    TextInputLayout layoutPastText;
    TextInputLayout layoutNewText;
    EditText pastPassInputText;
    EditText newPassInputText;
    Button buttonChangePass;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    ProgressDialog progressDialog;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        //Hook Elements From xml File - activity_change_password.xml
        changePassBackButton = findViewById(R.id.change_pass_back_button);
        layoutPastText = findViewById(R.id.layout_past_pass);
        layoutNewText = findViewById(R.id.layout_new_pass);
        pastPassInputText = findViewById(R.id.past_pass_input_text);
        newPassInputText = findViewById(R.id.new_pass_input_text);
        buttonChangePass = findViewById(R.id.btn_change_pass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");


        //Change Pass Back Button Listener
        //When Change Pass Back Button pressed, then back to My Account Activity
        changePassBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity
                startActivity(new Intent(ChangePasswordActivity.this, MyAccountActivity.class));
            }
        });


        //Change Pass Button Listener
        //When Change Pass pressed, then password will be updated
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String past_pass_text = pastPassInputText.getText().toString().trim();
                String new_pass_text = newPassInputText.getText().toString().trim();

                String PATTERN_PASSWORD = ("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
                Pattern pattern_password = Pattern.compile(PATTERN_PASSWORD);
                Matcher match_password = pattern_password.matcher(newPassInputText.getText());

                //Input Validation
                if (past_pass_text.isEmpty() && new_pass_text.isEmpty()) {

                    layoutPastText.setErrorEnabled(true);
                    layoutPastText.setError("Το πεδίο είναι υποχρεωτικό!");
                    pastPassInputText.requestFocus();

                    layoutNewText.setErrorEnabled(true);
                    layoutNewText.setError("Το πεδίο είναι υποχρεωτικό!");

                } else if (!past_pass_text.isEmpty() && new_pass_text.isEmpty()) {

                    layoutPastText.setErrorEnabled(false);

                    layoutNewText.setErrorEnabled(true);
                    layoutNewText.setError("Το πεδίο είναι υποχρεωτικό!");
                    newPassInputText.requestFocus();

                } else if (past_pass_text.isEmpty() && !new_pass_text.isEmpty()) {

                    layoutPastText.setErrorEnabled(true);
                    layoutPastText.setError("Το πεδίο είναι υποχρεωτικό!");
                    pastPassInputText.requestFocus();

                    layoutNewText.setErrorEnabled(false);

                } else if (!match_password.matches()) {

                    layoutPastText.setErrorEnabled(false);

                    layoutNewText.setErrorEnabled(true);
                    layoutNewText.setError("Ο κωδικός πρόσβασης πρέπει να είναι τουλάχιστον 8 χαρακτήρες, να περίεχει τουλάχιστον 1 ψηφίο και τουλάχιστον 1 κεφαλαίο γράμμα");
                    newPassInputText.requestFocus();

                } else if (!past_pass_text.isEmpty() && !new_pass_text.isEmpty()) {
                    layoutPastText.setErrorEnabled(false);

                    layoutNewText.setErrorEnabled(false);

                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), past_pass_text);
                    progressDialog = new ProgressDialog(ChangePasswordActivity.this);
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
                            user.reauthenticate(authCredential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updatePassword(new_pass_text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Update the realtime database firebase
                                                            String userID = user.getUid();
                                                            reference.child(userID).child("password").setValue(new_pass_text);
                                                            Toast.makeText(ChangePasswordActivity.this, "Ο κωδικός σας άλλαξε επιτυχώς", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                                        } else {
                                                            Toast.makeText(ChangePasswordActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(ChangePasswordActivity.this, "Ο κωδικός πρόσβασης δεν υπάρχει", Toast.LENGTH_SHORT).show();
                                                pastPassInputText.requestFocus();
                                                pastPassInputText.setText("");
                                            }
                                        }
                                    });
                        }
                    }).start();
                }
            }
        });

        //If an input text has error, when user type inside the edit_text, then the error disable

        pastPassInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutPastText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutNewText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        startActivity(new Intent(ChangePasswordActivity.this, MyAccountActivity.class));
    }
}