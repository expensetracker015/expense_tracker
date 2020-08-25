package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SignInOrSignUpModeConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInOrSignUpModeConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    String getModeOfSignIn() {

        String mode;
        mode = sharedPreferences.getString("mode_of_sign_in", "");
        return mode;
    }

    void setModeOfSignIn(String mode) {

        sharedPreferences.edit().putString("mode_of_sign_in", mode).apply();
    }
}
