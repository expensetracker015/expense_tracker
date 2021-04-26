package com.jaekapps.expensetracker.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.jaekapps.expensetracker.view.fragments.ExpenseFragment;
import com.jaekapps.expensetracker.view.fragments.IncomeFragment;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.sharedpreferences.CategorySharedPreferences;

import java.util.Objects;

public class ItemCategoryActivity extends AppCompatActivity implements ExpenseFragment.ExpenseFragmentListener,
        IncomeFragment.IncomeFragmentListener {

    private AppCompatEditText memoEditText;
    private CardView itemIconCardView, memoCardView;
    private CategorySharedPreferences categorySharedPreferences;
    private ExpenseFragment expenseFragment;
    private final int[] expenseItemColor = {
            Color.parseColor("#FFB74D"),
            Color.parseColor("#64B5F6"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#FFB74D"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#7986CB"),
            Color.parseColor("#F06292"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#81C784"),
            Color.parseColor("#E57373"),
            Color.parseColor("#81C784"),
            Color.parseColor("#FF8A65"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#E57373"),
            Color.parseColor("#F06292"),
            Color.parseColor("#64B5F6"),
            Color.parseColor("#7986CB"),
            Color.parseColor("#81C784"),
            Color.parseColor("#E57373"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#FFB74D"),
            Color.parseColor("#7986CB"),
            Color.parseColor("#FF8A65"),
            Color.parseColor("#E57373")
    };
    private final int[] expenseCategoryItemIconId = {
            R.drawable.food_light,
            R.drawable.bills_light,
            R.drawable.transportation_light,
            R.drawable.home_light,
            R.drawable.car_light,
            R.drawable.entertainment_light,
            R.drawable.shopping_light,
            R.drawable.cloth_light,
            R.drawable.insurance_light,
            R.drawable.tax_light,
            R.drawable.phone_light,
            R.drawable.cigarette_light,
            R.drawable.health_light,
            R.drawable.sports_light,
            R.drawable.baby_light,
            R.drawable.pet_light,
            R.drawable.beauty_light,
            R.drawable.electronics_light,
            R.drawable.wine_light,
            R.drawable.vegetables_light,
            R.drawable.gift_light,
            R.drawable.social_light,
            R.drawable.travel_light,
            R.drawable.education_light,
            R.drawable.book_light,
            R.drawable.office_light,
            R.drawable.others_light,
    };
    private final int[] incomeItemColor = {
            Color.parseColor("#E57373"),
            Color.parseColor("#FFB74D"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#7986CB"),
            Color.parseColor("#64B5F6"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#F06292"),
            Color.parseColor("#81C784"),
            Color.parseColor("#4DB6AC"),
            Color.parseColor("#FF8A65"),
            Color.parseColor("#E57373"),
    };
    private final int[] incomeCategoryItemIconId = {
            R.drawable.salary_light,
            R.drawable.awards_light,
            R.drawable.gift_light,
            R.drawable.sale_light,
            R.drawable.home_light,
            R.drawable.refunds_light,
            R.drawable.coupons_light,
            R.drawable.lottery_light,
            R.drawable.dividends_light,
            R.drawable.investments_light,
            R.drawable.others_light,
    };
    private FrameLayout categoryFragmentContainer;
    private ImageView itemIconImageView;
    private IncomeFragment incomeFragment;
    private String item_name = "", subcategory = "";
    String toolbar_title;

    private void hideTheKeyboard() {

        View view = getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }

    private void initialization() {

        categoryFragmentContainer = findViewById(R.id.categoryFragmentContainer);
        categorySharedPreferences = new CategorySharedPreferences(this);
        expenseFragment = new ExpenseFragment();
        incomeFragment = new IncomeFragment();
        itemIconCardView = findViewById(R.id.itemIconCardView);
        itemIconImageView = findViewById(R.id.itemIconImageView);
        memoCardView = findViewById(R.id.memoCardView);
        memoCardView.setTranslationY(3000f);
        memoCardView.setVisibility(View.GONE);
        memoEditText = findViewById(R.id.memoEditText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void goToExpenseIncomeActivity(String item_name, String memo, String subcategory) {

        Intent intent = new Intent();
        intent.putExtra("item_name", item_name);
        intent.putExtra("memo", memo);
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
        setContentView(R.layout.activity_item_category);

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

            finish();

        } else if (item.getItemId() == R.id.select_item) {

            if (item_name.length() == 0) {

                Toast.makeText(
                        this,
                        "Please, select an item!",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                String memo;

                if (Objects.requireNonNull(memoEditText.getText()).toString().isEmpty()) {

                    hideTheKeyboard();
                    memo = item_name;
                    goToExpenseIncomeActivity(item_name, memo, subcategory);

                } else if (memoEditText.getText().toString().length() > 20) {

                    Toast.makeText(
                            this,
                            "Memo is too long. Please, write a short memo!",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    hideTheKeyboard();
                    memo = memoEditText.getText().toString();
                    goToExpenseIncomeActivity(item_name, memo, subcategory);

                }

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

        if (memoCardView.getVisibility() == View.VISIBLE) {

            memoCardView.setTranslationY(3000f);
            memoCardView.setVisibility(View.GONE);

        } else {

            super.onBackPressed();

        }

    }

    @Override
    public void selectItemFromExpenseCategory(int position, String item_name) {

        this.item_name = item_name;

        if (memoCardView.getVisibility() != View.VISIBLE) {

            memoCardView.animate().translationYBy(-3000f).setDuration(100);
            memoCardView.setVisibility(View.VISIBLE);

        }

        itemIconCardView.setCardBackgroundColor(expenseItemColor[position]);
        itemIconImageView.setImageResource(expenseCategoryItemIconId[position]);
    }

    @Override
    public void selectItemFromIncomeCategory(int position, String item_name) {

        this.item_name = item_name;

        if (memoCardView.getVisibility() != View.VISIBLE) {

            memoCardView.animate().translationYBy(-3000f).setDuration(100);
            memoCardView.setVisibility(View.VISIBLE);

        }

        itemIconCardView.setCardBackgroundColor(incomeItemColor[position]);
        itemIconImageView.setImageResource(incomeCategoryItemIconId[position]);
    }
}
