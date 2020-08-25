package com.jaekapps.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;

    private void initialization() {

        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
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

                if (signInUsingEmailConfigActivity.getSignInUsingEmailStatus()
                        || signInUsingGoogleConfigActivity.getSignInUsingGoogleStatus()) {

                    startTheActivity(new HomeScreenActivity());

                } else {

                    startTheActivity(new MainActivity());

                }

            }
        }, 1000);
    }
}
