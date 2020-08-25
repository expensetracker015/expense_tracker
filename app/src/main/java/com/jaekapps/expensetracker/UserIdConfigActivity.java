package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

public class UserIdConfigActivity {

    private SharedPreferences sharedPreferences;

    UserIdConfigActivity(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    String getUserID() {

        return sharedPreferences.getString("userId", "");
    }

    void setUserID(String userId) {

        sharedPreferences.edit().putString("userId", userId).apply();
    }
}
