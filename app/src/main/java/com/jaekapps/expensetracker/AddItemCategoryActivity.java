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
    private String item_name = "", subcategory = "";
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

    private void goToExpenseIncomeActivity(String item_name, String subcategory) {

        Intent intent = new Intent();
        intent.putExtra("item_name", item_name);
        intent.putExtra("subcategory", subcategory);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void loadTheFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.categoryFragmentContainer, fragment)
                .commit();
    }

    private void loadTheFragmentWithSlideLeftCustomAnimation(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
                .replace(R.id.categoryFragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        initialization();

        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

            toolbar_title = "Expense";

        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

            toolbar_title = "Income";

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
                subcategory = "Expense";

            } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                loadTheFragment(incomeFragment);
                subcategory = "Income";

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.select_item_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            goToExpenseIncomeActivity(item_name, subcategory);

        } else if (item.getItemId() == R.id.select_item) {

            if (item_name.length() == 0) {

                Toast.makeText(
                        this,
                        "Please, select an item!",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                goToExpenseIncomeActivity(item_name, subcategory);

            }

        } else if (item.getItemId() == R.id.swap_sub_category) {

            if (!item_name.isEmpty()) {

                item_name = "";

            }

            if (expenseFragment != null && expenseFragment.isVisible()) {

                loadTheFragmentWithSlideLeftCustomAnimation(incomeFragment);
                subcategory = "Income";
                toolbar_title = "Income";

            } else if (incomeFragment != null && incomeFragment.isVisible()) {

                loadTheFragmentWithSlideLeftCustomAnimation(expenseFragment);
                subcategory = "Expense";
                toolbar_title = "Expense";

            }

            if (getSupportActionBar() != null) {

                getSupportActionBar().setTitle(toolbar_title);

            }

        }

        return true;
    }

    @Override
    public void onBackPressed() {

        goToExpenseIncomeActivity(item_name, subcategory);
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
