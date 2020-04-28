package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.Calendar;

public class CreateAnAccountActivity extends AppCompatActivity {

    int currentMonth, currentYear;
    String month;
    private BEIAmount beiAmount;
    private boolean emailAddressStatus = false, passwordStatus = false, usernameStatus = false;
    private CreateNewUser createNewUser;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextInputEditText usernameTextInputEditText, passwordTextInputEditText, emailAddressTextInputEditText;
    private TextInputLayout passwordTextInputLayout;
    private SignUpDialogBox signUpDialogBox;
    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;

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

    private void checkEmailAddress() {

        if (emailAddressTextInputEditText.getText().toString().isEmpty()) {

            emailAddressTextInputEditText.setError("Email address is required.");
            checkPassword();
            emailAddressStatus = false;

        } else {

             if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                checkPassword();
                emailAddressStatus = false;

            } else if (checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                 checkPassword();
                 emailAddressStatus = true;

             }

        }

    }

    private void checkPassword() {

        if (passwordTextInputEditText.getText().toString().isEmpty()) {

            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
            passwordTextInputEditText.setError("Password is required.");
            passwordStatus = false;

        } else {

            if (passwordTextInputEditText.getText().toString().contains(" ")) {

                passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                passwordTextInputEditText.setError("Password can not contain space.");
                passwordStatus = false;

            } else {

                if (passwordTextInputEditText.getText().toString().length() < 5) {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password is not strong. Password length should be more than 5.");
                    passwordStatus = false;

                } else if (passwordTextInputEditText.getText().toString().length() > 15) {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password can not be more than 15 characters.");
                    passwordStatus = false;

                } else {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);
                    passwordStatus = true;

                }

            }

        }

    }

    private void findMonth(int currentMonth) {

        switch (currentMonth) {

            case 1:
                month = "Jan";
                break;

            case 2:
                month = "Feb";
                break;

            case 3:
                month = "Mar";
                break;

            case 4:
                month = "Apr";
                break;

            case 5:
                month = "May";
                break;

            case 6:
                month = "June";
                break;

            case 7:
                month = "July";
                break;

            case 8:
                month = "Aug";
                break;

            case 9:
                month = "Sep";
                break;

            case 10:
                month = "Oct";
                break;

            case 11:
                month = "Nov";
                break;

            case 12:
                month = "Dec";
                break;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);

        beiAmount = new BEIAmount();
        Calendar calendar = Calendar.getInstance();
        createNewUser = new CreateNewUser();
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        currentYear = calendar.get(Calendar.YEAR);
        emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);
        findMonth(currentMonth);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usernameTextInputEditText = findViewById(R.id.usernameTextInputEditText);

        try {

            getSupportActionBar().setTitle("Sign up using email");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_new_user_action_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        } else if (item.getItemId() == R.id.saveInformation) {

            try {

                if (usernameTextInputEditText.getText().toString().isEmpty()) {

                    usernameTextInputEditText.setError("Username is required.");
                    checkEmailAddress();
                    usernameStatus = false;

                } else {

                    checkEmailAddress();
                    boolean specialCharFound = false;

                    for (Character character : usernameTextInputEditText.getText().toString().toCharArray()) {

                        if ((character >= 33 && character <= 47) || (character >= 58 && character <= 64) || (character >= 91 && character <= 96) || (character >= 123 && character <= 126)) {

                            specialCharFound = true;
                            break;

                        }

                    }

                    if (specialCharFound) {

                        usernameTextInputEditText.setError("Username can not contain special characters.");
                        usernameStatus = false;

                    } else {

                        if (emailAddressStatus && passwordStatus) {

                            if (checkInternetConnection()) {

                                String username = usernameTextInputEditText.getText().toString();
                                String emailAddress = emailAddressTextInputEditText.getText().toString();
                                String password = passwordTextInputEditText.getText().toString();
                                createNewUser.setUsername(username);
                                createNewUser.setEmail_address(emailAddress);
                                beiAmount.setBalance("0");
                                beiAmount.setExpense("0");
                                beiAmount.setIncome("0");
                                signUpDialogBox = new SignUpDialogBox();
                                signUpDialogBox.show(getSupportFragmentManager(), "signing_up");
                                hideTheKeyboard(getCurrentFocus().getRootView());
                                firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            databaseReference.child(firebaseUser.getUid()).child("Account_Details").setValue(createNewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        databaseReference.child(firebaseUser.getUid()).child("BEIAmount").child(String.valueOf(currentYear)).child(month).setValue(beiAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    signUpDialogBox.dismiss();
                                                                    signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(true);
                                                                    signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(false);
                                                                    MainActivity.mainActivity.finish();
                                                                    Toast.makeText(CreateAnAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                                                                    finish();

                                                                }

                                                            }
                                                        });

                                                    }

                                                }
                                            });

                                        } else {

                                            signUpDialogBox.dismiss();
                                            Toast.makeText(CreateAnAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            } else {

                                Toast.makeText(this, "Please, check your internet connection", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }

                }

            } catch (NullPointerException e)  {

                e.printStackTrace();

            }

        }

        return super.onOptionsItemSelected(item);

    }
}
