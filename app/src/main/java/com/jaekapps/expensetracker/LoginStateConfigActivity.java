package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class LoginStateConfigActivity {

    private SharedPreferences sharedPreferences;

    LoginStateConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    void writeLogInStatus(boolean status) {

        sharedPreferences.edit().putBoolean("login_status", status).apply();

    }

    boolean readLoginStatus() {

        return sharedPreferences.getBoolean("login_status", false);

    }

}
