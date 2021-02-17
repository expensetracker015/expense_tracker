package com.jaekapps.expensetracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingEmailPreferences;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingGooglePreferences;

public class SplashScreenActivity extends AppCompatActivity {

    private SignInUsingEmailPreferences signInUsingEmailPreferences;
    private SignInUsingGooglePreferences signInUsingGooglePreferences;

    private void initialization() {

        signInUsingEmailPreferences = new SignInUsingEmailPreferences(this);
        signInUsingGooglePreferences = new SignInUsingGooglePreferences(this);
    }

    private void startTheActivity(Activity activityClass) {

        Intent intent = new Intent(this, activityClass.getClass());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initialization();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (signInUsingEmailPreferences.getSignInUsingEmailStatus()
                        || signInUsingGooglePreferences.getSignInUsingGoogleStatus()) {

                    startTheActivity(new HomeScreenActivity());

                } else {

                    startTheActivity(new MainActivity());

                }

            }
        }, 1000);
    }
}
