package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class SignInUsingEmailConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingEmailConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    boolean getSignInUsingEmailStatus() {

        return sharedPreferences.getBoolean("sign_in_using_email", false);
    }

    void setSignInUsingEmailStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_email", status).apply();
    }
}
