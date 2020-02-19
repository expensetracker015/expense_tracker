package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class MainActivity extends AppCompatActivity {

    AppCompatButton signInButton, forgotPasswordButton, signUpButton;
    CreateNewUser createNewUser;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    GoogleSignInClient googleSignInClient;
    GoogleSignInButton googleSignInButton;
    int RC_SIGN_IN = 1;
    LogInDialogBox logInDialogBox;
    SignInDialogBox signInDialogBox;
    SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    public static MainActivity mainActivity;
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        if (checkInternetConnection()) {

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            signInDialogBox = new SignInDialogBox();
            signInDialogBox.show(getSupportFragmentManager(), "progress_dialog");
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        firebaseUser = firebaseAuth.getCurrentUser();
                        createNewUser.setEmail_address(firebaseUser.getEmail());
                        createNewUser.setUsername(firebaseUser.getDisplayName());
                        databaseReference.child(firebaseUser.getUid()).setValue(createNewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    signInDialogBox.dismiss();
                                    signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(false);
                                    signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(true);
                                    Toast.makeText(MainActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                    finish();

                                }

                            }
                        });

                    } else {

                        signInDialogBox.dismiss();
                        Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } else {

            Toast.makeText(this, "Please, check your internet connection", Toast.LENGTH_SHORT).show();

        }
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

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    emailAddressTextInputEditText.setError("Email address is required.");
                    logInDialogBox.dismiss();

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

                    } else {

                        logInDialogBox.dismiss();
                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                        passwordTextInputEditText.setError("Password is required.");

                    }

                } else if (!passwordTextInputEditText.getText().toString().isEmpty()) {

                    if (passwordTextInputEditText.getText().toString().length() > 15) {

                        if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            logInDialogBox.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                        } else {

                            logInDialogBox.dismiss();
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                            passwordTextInputEditText.setError("Password length can not be more than 15 characters.");

                        }

                    } else if (passwordTextInputEditText.getText().toString().length() <= 15) {

                        passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                        if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                            emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                            logInDialogBox.dismiss();

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
                                        Toast.makeText(MainActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                        finish();

                                    } else {
                                        
                                        hideTheKeyboard(view);
                                        logInDialogBox.dismiss();
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void signInUsingGoogle() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNewUser = new CreateNewUser();
        emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);
        emailAddressTextInputLayout = findViewById(R.id.emailAddressTextInputLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        logInDialogBox = new LogInDialogBox();
        mainActivity = this;
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        signInDialogBox = new SignInDialogBox();
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);

        if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus() || signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
            finish();

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

                signInUsingGoogle();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                e.printStackTrace();

            }

        }

    }
}
