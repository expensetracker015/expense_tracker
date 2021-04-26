package com.jaekapps.expensetracker.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.Budgets;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BudgetsActivity extends AppCompatActivity implements View.OnClickListener {

    private Calendar calendar;
    private CardView cancelCardView, saveCardView;
    private DatabaseReference userDBReference;
    private final List<String> dateList = new ArrayList<>();
    private final String[] periodList = new String[] {
            "Week",
            "Month",
            "Year",
            "One TIme"
    };
    private int dayOfTheWeek, position;
    private Spinner periodSpinner;
    private String amount= "", endDate, name = "", period = "", startDate, userId;
    private TextInputEditText amountTextInputEditText, nameTextInputEditText;

    private boolean checkAmount() {

        return !Objects.requireNonNull(amountTextInputEditText.getText()).toString().isEmpty();
    }

    private boolean checkName() {

        return !Objects.requireNonNull(nameTextInputEditText.getText()).toString().isEmpty();
    }

    private String findMonth(int currentMonth) {

        String month = "";

        switch (currentMonth) {

            case 1:
                month = "Jan";
                break;

            case 2:
                month = "Feb";
                break;

            case 3:
                month = "Mar";
                break;

            case 4:
                month = "Apr";
                break;

            case 5:
                month = "May";
                break;

            case 6:
                month = "June";
                break;

            case 7:
                month = "July";
                break;

            case 8:
                month = "Aug";
                break;

            case 9:
                month = "Sep";
                break;

            case 10:
                month = "Oct";
                break;

            case 11:
                month = "Nov";
                break;

            case 12:
                month = "Dec";
                break;

        }

        return month;
    }

    private String getAmount() {

        return Objects.requireNonNull(amountTextInputEditText.getText()).toString();
    }

    private String getName() {

        return Objects.requireNonNull(nameTextInputEditText.getText()).toString();
    }

    private void getDates() {

        switch (dayOfTheWeek) {

            case 1:
                dayOfTheWeek = dayOfTheWeek + 1;
                break;

            case 3:
                dayOfTheWeek = dayOfTheWeek - 1;
                break;

            case 4:
                dayOfTheWeek = dayOfTheWeek - 2;
                break;

            case 5:
                dayOfTheWeek = dayOfTheWeek - 3;
                break;

            case 6:
                dayOfTheWeek = dayOfTheWeek - 4;
                break;

            case 7:
                dayOfTheWeek = dayOfTheWeek - 5;
                break;

        }

        calendar.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        for (int i = 0; i < 7; i++) {

            dateList.add(df.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);

        }

        endDate = dateList.get(6);
        startDate = dateList.get(0);

    }

    private void initializeOnClickListener() {

        cancelCardView.setOnClickListener(this);
        saveCardView.setOnClickListener(this);
    }

    private void initializeViews() {

        amountTextInputEditText = findViewById(R.id.amountTextInputEditText);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, periodList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendar = Calendar.getInstance();
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        cancelCardView = findViewById(R.id.cancelCardView);
        nameTextInputEditText = findViewById(R.id.nameTextInputEditText);
        periodSpinner = findViewById(R.id.periodSpinner);
        periodSpinner.setAdapter(arrayAdapter);
        position = getIntent().getIntExtra("position", 0);
        saveCardView = findViewById(R.id.saveCardView);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(this);
        userId = userIdPreferences.getUserID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);

        initializeViews();
        initializeOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                period = periodList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cancelCardView) {

            finish();

        } else if (v.getId() == R.id.saveCardView) {

            if (checkName()) {

                if (checkAmount()) {

                    amountTextInputEditText.setHint(null);
                    nameTextInputEditText.setHint(null);
                    amount = getAmount();
                    name = getName();
                    Calendar calendar = Calendar.getInstance();
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentYear = calendar.get(Calendar.YEAR);
                    currentMonth = currentMonth + 1;
                    final String budgetCategory;
                    final String month = findMonth(currentMonth);
                    final String year = String.valueOf(currentYear);

                    if (!period.equals("One Time")) {

                        budgetCategory = "Periodic";

                    } else {

                        budgetCategory = "One_Time";

                    }

                    if (period.equals("Week")) {

                        getDates();

                    }

                    userDBReference.child(userId)
                            .child("Budget")
                            .child(budgetCategory)
                            .child(month)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {

                                        boolean found = false;

                                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                                            if (Objects.equals(itemSnapshot.getKey(), name)) {

                                                found = true;
                                                break;

                                            }

                                        }

                                        if (found) {

                                            Toast.makeText(
                                                    BudgetsActivity.this,
                                                    "Budget exists with " + name + " title. Please, select a different budget title!",
                                                    Toast.LENGTH_LONG
                                            ).show();

                                        } else {

                                            Budgets budgets = new Budgets();
                                            budgets.setAmount(amount);
                                            budgets.setEndDate(endDate);
                                            budgets.setPeriod("This " + period);
                                            budgets.setStartDate(startDate);
                                            userDBReference.child(userId)
                                                    .child("Budget")
                                                    .child(budgetCategory)
                                                    .child(month)
                                                    .child(name)
                                                    .setValue(budgets);
                                            endDate = "";
                                            startDate = "";
                                            Intent intent = new Intent();
                                            intent.putExtra("month", month);
                                            intent.putExtra("position", position);
                                            intent.putExtra("year", year);
                                            setResult(RESULT_OK, intent);
                                            finish();

                                        }

                                    } else {

                                        Budgets budgets = new Budgets();
                                        budgets.setAmount(amount);
                                        budgets.setEndDate(endDate);
                                        budgets.setPeriod("This " + period);
                                        budgets.setStartDate(startDate);
                                        userDBReference.child(userId)
                                                .child("Budget")
                                                .child(budgetCategory)
                                                .child(month)
                                                .child(name)
                                                .setValue(budgets);
                                        endDate = "";
                                        startDate = "";
                                        Intent intent = new Intent();
                                        intent.putExtra("month", month);
                                        intent.putExtra("position", position);
                                        intent.putExtra("year", year);
                                        setResult(RESULT_OK, intent);
                                        finish();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Log.e("database_error", error.getMessage());
                                }
                            });

                } else {

                    amountTextInputEditText.setHint("Please, enter an amount!");
                    amountTextInputEditText.setHintTextColor(getResources().getColor(R.color.red));

                }

            } else {

                nameTextInputEditText.setHint("Please, enter a budget title!");
                nameTextInputEditText.setHintTextColor(getResources().getColor(R.color.red));

                if (checkAmount()) {

                    amountTextInputEditText.setHint(null);

                } else {

                    amountTextInputEditText.setHint("Please, enter an amount!");
                    amountTextInputEditText.setHintTextColor(getResources().getColor(R.color.red));

                }

            }

        }

    }
}