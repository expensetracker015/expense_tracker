package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SignInUsingEmailConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingEmailConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    public void writeSignInUsingEmailStatus(boolean status) {

        sharedPreferences.edit().putBoolean("sign_in_using_email", status).apply();

    }

    public boolean readSignInUsingEmailStatus() {

        return sharedPreferences.getBoolean("sign_in_using_email", false);

    }

}
