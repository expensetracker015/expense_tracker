package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class SignInUsingGoogleConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingGoogleConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    void writeSignInUsingGoogleStatus(boolean status) {

        sharedPreferences.edit().putBoolean("login_status", status).apply();

    }

    boolean readSignInUsingGoogleStatus() {

        return sharedPreferences.getBoolean("login_status", false);

    }
}
