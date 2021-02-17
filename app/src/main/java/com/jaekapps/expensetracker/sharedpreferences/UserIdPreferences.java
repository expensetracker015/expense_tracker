package com.jaekapps.expensetracker.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserIdPreferences {

    private final SharedPreferences sharedPreferences;

    public UserIdPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public String getUserID() {

        return sharedPreferences.getString("userId", "");
    }

    public void setUserID(String userId) {

        sharedPreferences.edit().putString("userId", userId).apply();
    }
}
