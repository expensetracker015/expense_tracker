package com.jaekapps.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;

class CategorySharedPreferences {

    private SharedPreferences sharedPreferences;

    CategorySharedPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    void writeCategoryName(String name) {

        sharedPreferences.edit().putString("category_name", name).apply();

    }

    String readCategoryName() {

        return sharedPreferences.getString("category_name", "");

    }

}
