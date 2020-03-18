package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInUsingEmailActivity extends AppCompatActivity {

    AppCompatButton forgotPasswordButton, signInButton;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    LogInDialogBox logInDialogBox;
    SignInDialogBox signInDialogBox;
    SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    TextInputLayout emailAddressTextInputLayout, passwordTextInputLayout;
    TextInputEditText emailAddressTextInputEditText, passwordTextInputEditText;
    Toolbar toolbar;

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

    private void login(final View view) {

        try {

            if (emailAddressTextInputEditText.getText().toString().isEmpty()) {

                if (passwordTextInputEditText.getText().toString().isEmpty()) {

                    emailAddressTextInputEditText.setError("Email address is required.");
                    logInDialogBox.dismiss();
                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password is required.");
                    hideTheKeyboard(view);

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    emailAddressTextInputEditText.setError("Email address is required.");
                    logInDialogBox.dismiss();
                    hideTheKeyboard(view);

                    if (passwordTextInputEditText.getText().toString().length() > 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                    } else if (passwordTextInputEditText.getText().toString().length() <= 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                    }

                }

            } else if (!emailAddressTextInputEditText.getText().toString().isEmpty()) {

                if (passwordTextInputEditText.getText().toString().isEmpty()) {

                    if (checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                        emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                        logInDialogBox.dismiss();
                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password is required.");
                        hideTheKeyboard(view);

                    } else {

                        logInDialogBox.dismiss();
                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password is required.");
                        hideTheKeyboard(view);

                    }

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    if (passwordTextInputEditText.getText().toString().length() > 15) {

                        if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            logInDialogBox.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");
                            hideTheKeyboard(view);

                        } else {

                            logInDialogBox.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");
                            hideTheKeyboard(view);

                        }

                    } else if (passwordTextInputEditText.getText().toString().length() <= 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                        if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            logInDialogBox.dismiss();
                            hideTheKeyboard(view);

                        } else {

                            String email_address = emailAddressTextInputEditText.getText().toString();
                            String password = passwordTextInputEditText.getText().toString();

                            firebaseAuth.signInWithEmailAndPassword(email_address, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        hideTheKeyboard(view);
                                        logInDialogBox.dismiss();
                                        signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(true);
                                        Toast.makeText(SignInUsingEmailActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInUsingEmailActivity.this, HomeScreenActivity.class));
                                        finish();

                                    } else {

                                        hideTheKeyboard(view);
                                        logInDialogBox.dismiss();
                                        Toast.makeText(SignInUsingEmailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        emailAddressTextInputEditText.getText().clear();
                                        passwordTextInputEditText.getText().clear();

                                    }

                                }
                            });

                        }

                    }

                }

            }

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_using_email);

        emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);
        emailAddressTextInputLayout = findViewById(R.id.emailAddressTextInputLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        logInDialogBox = new LogInDialogBox();
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signInButton = findViewById(R.id.signInButton);
        signInDialogBox = new SignInDialogBox();
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            getSupportActionBar().setTitle("Forgot Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    logInDialogBox.show(getSupportFragmentManager(), "Logging In");
                    login(v);

                } else {

                    Toast.makeText(SignInUsingEmailActivity.this, "Please, check your internet connection.", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);

    }
}
