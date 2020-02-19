package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

class SignInUsingEmailConfigActivity {

    private SharedPreferences sharedPreferences;

    SignInUsingEmailConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    void writeSignInUsingEmailStatus(boolean status) {

        sharedPreferences.edit().putBoolean("login_status", status).apply();

    }

    boolean readSignInUsingEmailStatus() {

        return sharedPreferences.getBoolean("login_status", false);

    }

}
