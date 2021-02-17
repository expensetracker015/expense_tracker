package com.jaekapps.expensetracker.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class TabPositionPreferences {

    SharedPreferences sharedPreferences;

    public TabPositionPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public int getTabPosition() {

        return sharedPreferences.getInt("tab_position", 0);
    }

    public void setTabPosition(int tabPosition) {

        sharedPreferences.edit()
                .putInt("tab_position", tabPosition)
                .apply();
    }
}
