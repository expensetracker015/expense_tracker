package com.jaekapps.expensetracker.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class CategorySharedPreferences {

    private final SharedPreferences sharedPreferences;

    public CategorySharedPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void writeCategoryName(String name) {

        sharedPreferences.edit().putString("category_name", name).apply();
    }

    public String readCategoryName() {

        return sharedPreferences.getString("category_name", "");
    }
}
