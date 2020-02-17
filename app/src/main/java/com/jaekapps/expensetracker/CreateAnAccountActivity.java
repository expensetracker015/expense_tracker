package com.jaekapps.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAnAccountActivity extends AppCompatActivity {

    private boolean emailAddressStatus = false, passwordStatus = false, usernameStatus = false;
    private TextInputEditText usernameTextInputEditText, passwordTextInputEditText, emailAddressTextInputEditText;
    private TextInputLayout passwordTextInputLayout;
    private Toolbar toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);

        emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usernameTextInputEditText = findViewById(R.id.usernameTextInputEditText);

        try {

            getSupportActionBar().setTitle("Create An Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

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

        }

        return super.onOptionsItemSelected(item);

    }
}
