package com.jaekapps.expensetracker.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SignInUsingEmailPreferences {

    private final SharedPreferences sharedPreferences;

    public SignInUsingEmailPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public boolean getSignInUsingEmailStatus() {

        return sharedPreferences.getBoolean("sign_in_using_email", false);
    }

    public void setSignInUsingEmailStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_email", status).apply();
    }
}
