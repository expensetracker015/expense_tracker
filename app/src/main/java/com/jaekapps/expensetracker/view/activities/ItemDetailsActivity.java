package com.jaekapps.expensetracker.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaekapps.expensetracker.R;

public class ItemDetailsActivity extends AppCompatActivity {

    private AppCompatImageView itemIconImageView;
    private CardView itemIconCardView;
    private FloatingActionButton editFab;
    private int color, icon;
    private String amount, category, date, memo, name;
    private TextView amountTextView, categoryTypeTextView, dateTextView, itemNameTextView, memoTextView;

    private void initialization() {

        amountTextView = findViewById(R.id.amountTextView);
        categoryTypeTextView = findViewById(R.id.categoryTypeTextView);
        dateTextView = findViewById(R.id.dateTextView);
        editFab = findViewById(R.id.editFab);
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        category = intent.getStringExtra("category");
        color = intent.getIntExtra("color", 0);
        date = intent.getStringExtra("date");
        icon = intent.getIntExtra("icon", 0);
        memo = intent.getStringExtra("memo");
        name = intent.getStringExtra("name");
        itemIconCardView = findViewById(R.id.itemIconCardView);
        itemIconImageView = findViewById(R.id.itemIconImageView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        memoTextView = findViewById(R.id.memoTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        initialization();
        amountTextView.setText(amount);
        categoryTypeTextView.setText(category);
        dateTextView.setText(date);
        itemIconCardView.setCardBackgroundColor(color);
        itemIconImageView.setImageResource(icon);
        itemNameTextView.setText(name);
        memoTextView.setText(memo);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        }

        return true;
    }
}