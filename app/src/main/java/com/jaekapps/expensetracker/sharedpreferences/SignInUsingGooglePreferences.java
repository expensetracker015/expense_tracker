package com.jaekapps.expensetracker.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SignInUsingGooglePreferences {

    private final SharedPreferences sharedPreferences;

    public SignInUsingGooglePreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public boolean getSignInUsingGoogleStatus() {

        return sharedPreferences.getBoolean("sign_in_using_google", false);
    }

    public void setSignInUsingGoogleStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_google", status).apply();
    }
}
