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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Init Variables
    ImageButton loginBackButton;

    TextView notMemberText;
    TextView resetPasswordText;

    EditText emailInputText;
    EditText passwordInputText;

    TextInputLayout layoutEmail;
    TextInputLayout layoutPassword;

    Button buttonLogin;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    //Create a new Network Listener
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hook Elements From xml File - activity_login.xml
        loginBackButton = findViewById(R.id.login_back_button);
        notMemberText = findViewById(R.id.not_member_text);
        resetPasswordText = findViewById(R.id.reset_password_text);
        emailInputText = findViewById(R.id.email_input_text);
        passwordInputText = findViewById(R.id.password_input_text);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPassword = findViewById(R.id.layout_password);
        buttonLogin = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        //Login Back Button Listener
        //When Login Back Button pressed, then back to Launch Activity
        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start Launch Activity
                startActivity(new Intent(LoginActivity.this, LaunchActivity.class));
            }
        });

        //Convert TextView in Clickable Format
        resetPasswordText = (TextView) findViewById(R.id.reset_password_text);
        notMemberText = (TextView) findViewById(R.id.not_member_text);
        String reset_text = "Ξέχασες τον κωδικό σου; Επαναφορά";
        String member_text = "Ξέχασες να κάνεις εγγραφή; Εγγραφή";
        SpannableString rt = new SpannableString(reset_text);
        SpannableString mt = new SpannableString(member_text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View w) {
                switch (w.getId()) {
                    case R.id.reset_password_text:
                        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                        break;
                    case R.id.not_member_text:
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        break;
                }
            }

        };

        rt.setSpan(clickableSpan, 24, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mt.setSpan(clickableSpan, 27, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        resetPasswordText.setText(rt);
        notMemberText.setText(mt);
        resetPasswordText.setMovementMethod(LinkMovementMethod.getInstance());
        notMemberText.setMovementMethod(LinkMovementMethod.getInstance());

        //When Login Button pressed, then user will logged in
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text = emailInputText.getText().toString().trim();
                String password_text = passwordInputText.getText().toString().trim();

                //Bellow i create an input validation for all fields
                //After Validation,  User will logged in
                if (email_text.isEmpty() && password_text.isEmpty()) {

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");

                } else if (!email_text.isEmpty() && password_text.isEmpty()) {

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("Το πεδίο Password είναι υποχρεωτικό!");
                    passwordInputText.requestFocus();

                } else if (email_text.isEmpty() && !password_text.isEmpty()) {

                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Το πεδίο Email είναι υποχρεωτικό!");
                    emailInputText.requestFocus();

                    layoutPassword.setErrorEnabled(false);

                } else if (!email_text.isEmpty() && !password_text.isEmpty()) {

                    layoutEmail.setErrorEnabled(false);

                    layoutPassword.setErrorEnabled(false);

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Σύνδεση..."); // Setting Message
                    progressDialog.setTitle("Εξέλιξη Σύνδεσης"); // Setting Title
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
                            mAuth.signInWithEmailAndPassword(email_text,password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplication(),"Επιτυχής Σύνδεση", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                                    }else{
                                        Toast.makeText(LoginActivity.this,"Εσφαλμένη Σύνδεση! Ελέγξτε το Email ή τον Κωδικό Πρόσβασης", Toast.LENGTH_SHORT).show();
                                        emailInputText.requestFocus();
                                    }
                                }
                            });
                        }
                    }).start();
                }

            }
        });


        //If an input text has error, when user type inside the edit_text, then the error disable

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
        startActivity(new Intent(LoginActivity.this, LaunchActivity.class));
    }
}