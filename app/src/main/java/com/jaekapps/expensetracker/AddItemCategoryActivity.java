package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AddItemCategoryActivity extends AppCompatActivity implements ExpenseFragment.ExpenseFragmentListener,
        IncomeFragment.IncomeFragmentListener {

    private CategorySharedPreferences categorySharedPreferences;
    private ExpenseFragment expenseFragment;
    private FrameLayout categoryFragmentContainer;
    private IncomeFragment incomeFragment;
    private String item_name = "";
    String toolbar_title;

    private void initialization() {

        categoryFragmentContainer = findViewById(R.id.categoryFragmentContainer);
        categorySharedPreferences = new CategorySharedPreferences(this);
        expenseFragment = new ExpenseFragment();
        incomeFragment = new IncomeFragment();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void loadTheFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.categoryFragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        initialization();

        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

            toolbar_title = "Expense Category";

        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

            toolbar_title = "Income Category";

        }

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(toolbar_title);

        }

        if (categoryFragmentContainer != null) {

            if (savedInstanceState != null) {

                return;

            }

            if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                loadTheFragment(expenseFragment);

            } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                loadTheFragment(incomeFragment);

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_new_user_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        } else if (item.getItemId() == R.id.saveInformation) {

            if (item_name.length() == 0) {

                Toast.makeText(
                        this,
                        "Please, select an item.",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Intent intent = new Intent();
                intent.putExtra("item_name", item_name);
                setResult(RESULT_OK, intent);
                finish();

            }

        }

        return true;
    }

    @Override
    public void selectItemFromExpenseCategory(String item_name) {

        this.item_name = item_name;
    }

    @Override
    public void selectItemFromIncomeCategory(String item_name) {

        this.item_name = item_name;
    }
}
