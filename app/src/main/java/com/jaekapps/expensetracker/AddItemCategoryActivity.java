package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class AddItemCategoryActivity extends AppCompatActivity {

    CategorySharedPreferences categorySharedPreferences;
    ExpenseFragment expenseFragment;
    FrameLayout categoryFragmentContainer;
    IncomeFragment incomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        categoryFragmentContainer = findViewById(R.id.categoryFragmentContainer);
        categorySharedPreferences = new CategorySharedPreferences(this);
        expenseFragment = new ExpenseFragment();
        incomeFragment = new IncomeFragment();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

            getSupportActionBar().setTitle("Expense Category");

        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

            getSupportActionBar().setTitle("Income Category");

        }

        if (categoryFragmentContainer != null) {

            if (savedInstanceState != null) {

                return;

            }

            if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                getSupportFragmentManager().beginTransaction().replace(R.id.categoryFragmentContainer, expenseFragment).commit();

            } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                getSupportFragmentManager().beginTransaction().replace(R.id.categoryFragmentContainer, incomeFragment).commit();

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        }

        return true;

    }
}
