package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class SignInUsingGoogleConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingGoogleConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    void writeSignInUsingGoogleStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_google", status).apply();

    }

    boolean readSignInUsingGoogleStatus() {

        return sharedPreferences.getBoolean("sign_in_using_google", false);

    }
}
