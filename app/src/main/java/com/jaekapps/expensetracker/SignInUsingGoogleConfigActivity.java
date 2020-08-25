package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class SignInUsingGoogleConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingGoogleConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    boolean getSignInUsingGoogleStatus() {

        return sharedPreferences.getBoolean("sign_in_using_google", false);
    }

    void setSignInUsingGoogleStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_google", status).apply();
    }
}
