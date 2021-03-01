package com.example.coffeenest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeenest.Listener.NetworkChangeListener;
import com.example.coffeenest.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    //Init Variables
    ImageButton registerBackButton;
    TextView alreadyMemberText;
    EditText usernameInputText;
    EditText emailInputText;
    EditText passwordInputText;
    EditText conPasswordInputText;

    TextInputLayout layoutUsername;
    TextInputLayout layoutEmail;
    TextInputLayout layoutPassword;
    TextInputLayout layoutConPassword;

    Button buttonRegister;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hook Elements From xml File - activity_register.xml
        registerBackButton = findViewById(R.id.register_back_btn);
        alreadyMemberText = findViewById(R.id.already_member_text);
        usernameInputText = findViewById(R.id.username_input_text);
        emailInputText = findViewById(R.id.email_input_text);
        passwordInputText = findViewById(R.id.password_input_text);
        conPasswordInputText = findViewById(R.id.con_password_input_text);
        layoutUsername = findViewById(R.id.layout_username);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPassword = findViewById(R.id.layout_password);
        layoutConPassword = findViewById(R.id.layout_con_password);
        buttonRegister = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();


        //Register Back Button Listener
        //When Register Back Button pressed, then back to Launch Activity
        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start Launch Activity
                startActivity(new Intent(RegisterActivity.this, LaunchActivity.class));
            }
        });


        //Convert TextView in Clickable Format
        String text = "Έχεις ήδη εγγραφεί; Σύνδεση";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //Start Login Activity
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        };

        ss.setSpan(clickableSpan, 20, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alreadyMemberText.setText(ss);
        alreadyMemberText.setMovementMethod(LinkMovementMethod.getInstance());

        //Register Button Listener
        //When Register Button pressed, then a new user will created
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username_text = usernameInputText.getText().toString().trim();
                String email_text = emailInputText.getText().toString().trim();
                String password_text = passwordInputText.getText().toString().trim();
                String con_password_text = conPasswordInputText.getText().toString().trim();

                //Regex for password
                //Min 8 char
                //1 capital char
                //1 digit
                String PATTERN_PASSWORD = ("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
                Pattern pattern_password = Pattern.compile(PATTERN_PASSWORD);
                Matcher match_password = pattern_password.matcher(passwordInputText.getText());

                //Regex for email
                String PATTERN_EMAIL = ("[a-zA-Z0-9._-]+@[a-z]+.+[a-z]");
                Pattern pattern_email = Pattern.compile(PATTERN_EMAIL);
                Matcher match_email = pattern_email.matcher(emailInputText.getText());

                //Bellow i create an input validation for all fields
                //After Validation, the User will create

                if (username_text.isEmpty() && email_text.isEmpty() && password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");


                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");


                } else if (!username_text.isEmpty() && email_text.isEmpty() && password_text.isEmpty() && con_password_text.isEmpty()) {
                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (username_text.isEmpty() && !email_text.isEmpty() && password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (username_text.isEmpty() && email_text.isEmpty() && !password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (username_text.isEmpty() && email_text.isEmpty() && password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(false);

                } else if (!username_text.isEmpty() && !email_text.isEmpty() && password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");
                    passwordInputText.requestFocus();

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (!username_text.isEmpty() && email_text.isEmpty() && !password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (!username_text.isEmpty() && email_text.isEmpty() && password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(false);

                } else if (username_text.isEmpty() && !email_text.isEmpty() && !password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");

                } else if (username_text.isEmpty() && !email_text.isEmpty() && password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                    layoutConPassword.setErrorEnabled(false);

                } else if (username_text.isEmpty() && email_text.isEmpty() && !password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(false);

                } else if (!username_text.isEmpty() && !email_text.isEmpty() && !password_text.isEmpty() && con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(true);
                    layoutConPassword.setError("Το πεδίο Confirm Password είναι υποχρεωτικό!");
                    conPasswordInputText.requestFocus();

                } else if (!username_text.isEmpty() && !email_text.isEmpty() && password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");
                    passwordInputText.requestFocus();

                    layoutConPassword.setErrorEnabled(false);

                } else if (username_text.isEmpty() && !email_text.isEmpty() && !password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("Το πεδίο Username είναι υποχρεωτικό!");
                    usernameInputText.requestFocus();

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(false);

                } else if (!username_text.isEmpty() && email_text.isEmpty() && !password_text.isEmpty() && !con_password_text.isEmpty()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(false);

                } else if (!username_text.isEmpty() && !email_text.isEmpty() && !password_text.isEmpty() && !con_password_text.isEmpty() && !password_text.equals(con_password_text)) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Οι κωδικοί πρόσβασης δε ταιριάζουν");
                    passwordInputText.requestFocus();

                    layoutConPassword.setErrorEnabled(true);
                    conPasswordInputText.setText("");

                } else if (!match_password.matches()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Ο κωδικός πρόσβασης πρέπει να είναι τουλάχιστον 8 χαρακτήρες, να περίεχει τουλάχιστον 1 ψηφίο και τουλάχιστον 1 κεφαλαίο γράμμα");
                    passwordInputText.requestFocus();

                    layoutConPassword.setErrorEnabled(true);

                } else if (!match_email.matches()) {

                    layoutUsername.setErrorEnabled(false);

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Εισάγεται ένα έγκυρο Email");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(false);

                    layoutConPassword.setErrorEnabled(false);

                } else if (!username_text.isEmpty() && !email_text.isEmpty() && !password_text.isEmpty() && !con_password_text.isEmpty() && password_text.equals(con_password_text) && match_password.matches() && match_email.matches()) {

                    //Once the input validation has been done, the user is registered and then we are transferred to LoginActivity
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Εγγραφή..."); // Setting Message
                    progressDialog.setTitle("Εξέλιξη Εγγραφής"); // Setting Title
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
                            mAuth.createUserWithEmailAndPassword(email_text, password_text)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                User user = new User(username_text, email_text, password_text);

                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplication(), "Η εγγραφή σας πραγματοποιήθηκε επιτυχώς!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                        } else {
                                                            Toast.makeText(getApplication(), "Σφάλμα! Η εγγραφή σας δε πραγματοποιήθηκε επιτυχώς", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                            } else {
                                                layoutUsername.setErrorEnabled(false);

                                                emailInputText.requestFocus();
                                                emailInputText.setText("");
                                                layoutPassword.setErrorEnabled(false);
                                                layoutConPassword.setErrorEnabled(false);
                                                Toast.makeText(getApplication(), "Το email χρησιμοποιείται ηδη!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }).start();
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

        emailInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        conPasswordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutConPassword.setErrorEnabled(false);
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
        startActivity(new Intent(RegisterActivity.this, LaunchActivity.class));
    }
}