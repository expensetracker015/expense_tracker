package com.jaekapps.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class MainActivity extends AppCompatActivity {

    AppCompatButton signInButton, forgotPasswordButton, signUpButton;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    GoogleSignInClient googleSignInClient;
    GoogleSignInButton googleSignInButton;
    int RC_SIGN_IN = 1;
    LoggingInDialog loggingInDialog;
    LoginStateConfigActivity loginStateConfigActivity;
    String userId;
    TextInputLayout emailAddressTextInputLayout, passwordTextInputLayout;
    TextInputEditText emailAddressTextInputEditText, passwordTextInputEditText;

    private boolean checkIfEmailAddressIsValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private boolean checkInternetConnection() {

        boolean internetConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                internetConnected = true;

            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                internetConnected = true;

            }

        }

        return internetConnected;

    }

    private void hideTheKeyboard(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

    private void login(View view) {

        try {

            if (emailAddressTextInputEditText.getText().toString().isEmpty()) {

                if (passwordTextInputEditText.getText().toString().isEmpty()) {

                    emailAddressTextInputEditText.setError("Email address is required.");
                    loggingInDialog.dismiss();
                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password is required.");

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    emailAddressTextInputEditText.setError("Email address is required.");
                    loggingInDialog.dismiss();

                    if (passwordTextInputEditText.getText().toString().length() > 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                    } else if (passwordTextInputEditText.getText().toString().length() <= 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                    }

                }

            } else if (!emailAddressTextInputEditText.getText().toString().isEmpty()) {

                if (passwordTextInputEditText.getText().toString().isEmpty()) {

                    if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                        emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                        loggingInDialog.dismiss();
                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password is required.");

                    } else {

                        loggingInDialog.dismiss();
                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password is required.");

                    }

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    if (passwordTextInputEditText.getText().toString().length() > 15) {

                        if (checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            loggingInDialog.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                        } else {

                            loggingInDialog.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                        }

                    } else if (passwordTextInputEditText.getText().toString().length() <= 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                        if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            loggingInDialog.dismiss();

                        } else {

                            hideTheKeyboard(view);
                            String email_address = emailAddressTextInputEditText.getText().toString();
                            String password = passwordTextInputEditText.getText().toString();
                            Log.i("Email address", email_address);
                            Log.i("Password", password);
                            loginStateConfigActivity.writeLogInStatus(true);

                        }

                    }

                }

            }

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    private void signInUsingGoogle() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);
        emailAddressTextInputLayout = findViewById(R.id.emailAddressTextInputLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("User");
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        loggingInDialog = new LoggingInDialog();
        loginStateConfigActivity = new LoginStateConfigActivity(this);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        try {

            userId = firebaseUser.getUid();

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));

            }
        });
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //signInUsingGoogle();
                startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                finish();

            }
        });
        passwordTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternetConnection()) {

                    loggingInDialog.show(getSupportFragmentManager(), "Logging In");
                    login(v);

                } else {

                    Toast.makeText(MainActivity.this, "Please, check your internet connection.", Toast.LENGTH_SHORT).show();

                }

            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, CreateAnAccountActivity.class));

            }
        });

    }
}
