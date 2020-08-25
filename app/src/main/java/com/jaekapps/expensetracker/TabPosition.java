package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

public class TabPosition {

    SharedPreferences sharedPreferences;

    TabPosition(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    int getTabPosition() {

        return sharedPreferences.getInt("tab_position", 0);
    }

    void setTabPosition(int tabPosition) {

        sharedPreferences.edit()
                .putInt("tab_position", tabPosition)
                .apply();
    }
}
