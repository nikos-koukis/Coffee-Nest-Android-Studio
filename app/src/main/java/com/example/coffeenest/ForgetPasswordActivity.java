package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.coffeenest.Listener.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    //Init Variables
    ImageButton forgetPassBackButton;
    EditText emailInputText;
    TextInputLayout layoutEmail;
    Button buttonResetPass;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //Hook Elements From xml File - activity_forget_password.xml
        forgetPassBackButton = findViewById(R.id.forget_pass_back_button);
        emailInputText = findViewById(R.id.email_input_text);
        layoutEmail = findViewById(R.id.layout_email);
        buttonResetPass = findViewById(R.id.btn_reset_pass);
        mAuth = FirebaseAuth.getInstance();

        //Forget Back Button Listener
        //When Forget Back Button pressed, then back to Login Activity
        forgetPassBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start Launch Activity
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
            }
        });

        //Reset Button Listener
        //When Reset Button pressed, then email was sent for change password
        buttonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text = emailInputText.getText().toString().trim();

                //Bellow i create an input validation for all fields
                //After Validation,  Email was sent
                if (email_text.isEmpty()) {

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Παρακαλώ εισάγεται το Email σας!");
                    emailInputText.requestFocus();

                } else if (!email_text.isEmpty()) {
                    layoutEmail.setErrorEnabled(false);
                    emailInputText.setText("");

                    progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
                    progressDialog.setMessage("Επαναφορά Κωδικού Πρόσβασης..."); // Setting Message
                    progressDialog.setTitle("Εξέλιξη Διαδικασίας"); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                            mAuth.sendPasswordResetEmail(email_text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPasswordActivity.this, "Ελέγξτε το email σας για να επαναφέρετε τον κωδικό πρόσβασης", Toast.LENGTH_SHORT).show();
                                        //Start New Activity
                                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(ForgetPasswordActivity.this, "Κάτι πήγε στραβά! Προσπαθήστε πάλι.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }).start();
                }
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
        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
    }
}