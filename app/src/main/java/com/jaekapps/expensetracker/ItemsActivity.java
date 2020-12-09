package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ItemsActivity extends AppCompatActivity implements ExpenseIncomeItemOfEachDateAdapter.ExpenseItemClickListener {

    private ArrayList<Integer> itemIconList;
    private ArrayList<String> amountList, itemList;
    private BEIAmount beiAmount;
    private DatabaseReference userDBReference;
    private int[] colors;
    private RecyclerView expenseItemOfEachDateRecyclerView;
    private TextView amountTextView, dateTextView;
    private String amount_category, category, date, month, year, userId;

    private ArrayList<Integer> addIconsToIconList(ArrayList<String> itemList) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {

            itemIconList.add(findTheIcon(itemList.get(i)));

        }

        return itemIconList;
    }

    private int findTheIcon(String item_name) {

        int icon_id = 0;

        switch (item_name) {

            case "Food":
                icon_id = R.drawable.food_light;
                break;
            case "Bills":
                icon_id = R.drawable.bills_light;
                break;
            case "Transportation":
                icon_id = R.drawable.transportation_light;
                break;
            case "Home":
            case "Rental":
                icon_id = R.drawable.home_light;
                break;
            case "Car":
                icon_id = R.drawable.car_light;
                break;
            case "Entertainment":
                icon_id = R.drawable.entertainment_light;
                break;
            case "Shopping":
                icon_id = R.drawable.shopping_light;
                break;
            case "Clothing":
                icon_id = R.drawable.cloth_light;
                break;
            case "Insurance":
                icon_id = R.drawable.insurance_light;
                break;
            case "Tax":
                icon_id = R.drawable.tax_light;
                break;
            case "Telephone":
                icon_id = R.drawable.phone_light;
                break;
            case "Cigarette":
                icon_id = R.drawable.cigarette_light;
                break;
            case "Health":
                icon_id = R.drawable.health_light;
                break;
            case "Sports":
                icon_id = R.drawable.sports_light;
                break;
            case "Baby":
                icon_id = R.drawable.baby_light;
                break;
            case "Pet":
                icon_id = R.drawable.pet_light;
                break;
            case "Beauty":
                icon_id = R.drawable.beauty_light;
                break;
            case "Electronics":
                icon_id = R.drawable.electronics_light;
                break;
            case "Wine":
                icon_id = R.drawable.wine_light;
                break;
            case "Vegetables":
                icon_id = R.drawable.vegetables_light;
                break;
            case "Gift":
            case "Grants":
                icon_id = R.drawable.gift_light;
                break;
            case "Social":
                icon_id = R.drawable.social_light;
                break;
            case "Travel":
                icon_id = R.drawable.travel_light;
                break;
            case "Education":
                icon_id = R.drawable.education_light;
                break;
            case "Book":
                icon_id = R.drawable.book_light;
                break;
            case "Office":
                icon_id = R.drawable.office_light;
                break;
            case "Salary":
                icon_id = R.drawable.salary_light;
                break;
            case "Awards":
                icon_id = R.drawable.awards_light;
                break;
            case "Sale":
                icon_id = R.drawable.sale_light;
                break;
            case "Refunds":
                icon_id = R.drawable.refunds_light;
                break;
            case "Coupons":
                icon_id = R.drawable.coupons_light;
                break;
            case "Lottery":
                icon_id = R.drawable.lottery_light;
                break;
            case "Dividends":
                icon_id = R.drawable.dividends_light;
                break;
            case "Investments":
                icon_id = R.drawable.investments_light;
                break;
            case "Others":
                icon_id = R.drawable.others_light;
                break;

        }

        return icon_id;
    }

    private String putComma(String amount) {

        char[] amt;
        int flag = 0, i, pos = 0;
        String new_amount;
        StringBuilder amountBuilder = new StringBuilder();

        if (amount.contains(".")) {

            amt = amount.toCharArray();

            for (i = 0; i < amt.length; i++) {

                if (amt[i] == '.') {

                    pos = i;
                    break;

                } else {

                    amountBuilder.append(amt[i]);

                }

            }

            new_amount = amountBuilder.toString();
            amountBuilder = new StringBuilder();

            if (new_amount.length() >= 4) {

                amt = new_amount.toCharArray();
                char[] new_amt = amount.toCharArray();

                for (i = amt.length - 1; i >= 0; i--) {

                    if (flag < 3) {

                        amountBuilder.append(amt[i]);
                        flag++;

                    } else {

                        amountBuilder.append(',');
                        amountBuilder.append(amt[i]);
                        flag = 1;

                    }

                }

                amountBuilder.reverse();

                for (i = pos; i < new_amt.length; i++) {

                    amountBuilder.append(new_amt[i]);

                }

                new_amount = amountBuilder.toString();

            }

        } else {

            amt = amount.toCharArray();

            for (i = amt.length - 1; i >= 0; i--) {

                if (flag < 3) {

                    amountBuilder.append(amt[i]);
                    flag++;

                } else {

                    amountBuilder.append(",");
                    amountBuilder.append(amt[i]);
                    flag = 1;

                }

            }

            amountBuilder.reverse();
            new_amount = amountBuilder.toString();

        }

        return new_amount;
    }

    private String replaceSlashWithUnderscore(String date) {

        char[] dateArray = date.toCharArray();

        for (int i = 0; i < dateArray.length; i++) {

            if (dateArray[i] == '/') {

                dateArray[i] = '_';

            }

        }

        date = String.valueOf(dateArray);
        return date;
    }

    private void initialization() {

        amountList = new ArrayList<>();
        amountTextView = findViewById(R.id.amountTextView);
        beiAmount = new BEIAmount();
        colors = new int[] {
                getResources().getColor(R.color.amber, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.blue, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.blue_gray, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.brown, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.cyan, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.deep_orange, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.deep_purple, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.gray, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.green, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.indigo, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.light_blue, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.light_green, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.lime, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.orange, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.pink, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.purple, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.red, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.teal, Objects.requireNonNull(getTheme())),
                getResources().getColor(R.color.yellow, Objects.requireNonNull(getTheme()))
        };
        Intent intent = getIntent();

        if (Objects.equals(intent.getStringExtra("category"), "Expense_Category")) {

            amount_category = "Expense";

        } else if (Objects.equals(intent.getStringExtra("category"), "Income_Category")) {

            amount_category = "Income";

        }

        category = intent.getStringExtra("category");
        date = intent.getStringExtra("date");
        dateTextView = findViewById(R.id.dateTextView);
        expenseItemOfEachDateRecyclerView = findViewById(R.id.expenseItemOfEachDateRecyclerView);
        expenseItemOfEachDateRecyclerView.setHasFixedSize(true);
        expenseItemOfEachDateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemIconList = new ArrayList<>();
        itemList = new ArrayList<>();
        month = intent.getStringExtra("month");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Items");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(this);
        userId = userIdConfigActivity.getUserID();
        year = intent.getStringExtra("year");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        initialization();
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    @SuppressLint("SetTextI18n")
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);

                            if (beiAmount != null) {

                                if (amount_category.equals("Expense")) {

                                    beiAmount.setExpense(getResources().getString(R.string.rupees) + " " + putComma(beiAmount.getExpense()));
                                    amountTextView.setText(amount_category + " : " + beiAmount.getExpense());

                                } else if (amount_category.equals("Income")) {

                                    beiAmount.setIncome(getResources().getString(R.string.rupees) + " " + putComma(beiAmount.getIncome()));
                                    amountTextView.setText(amount_category + " : " + beiAmount.getIncome());

                                }

                                dateTextView.setText(date);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        String temp_date = replaceSlashWithUnderscore(date);
        userDBReference.child(userId)
                .child(category)
                .child(year)
                .child(month)
                .child(temp_date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                amountList.add(dataSnapshot.getValue(String.class));
                                itemList.add(dataSnapshot.getKey());

                            }

                            if (category.equals("Expense_Category")) {

                                for (int i = 0; i < amountList.size(); i++) {

                                    amountList.set(i, "-" + getResources().getString(R.string.rupees) + " " + putComma(amountList.get(i)));

                                }

                            } else if (category.equals("Income_Category")) {

                                for (int i = 0; i < amountList.size(); i++) {

                                    amountList.set(i, getResources().getString(R.string.rupees) + " " + putComma(amountList.get(i)));

                                }

                            }

                            itemIconList = addIconsToIconList(itemList);
                            ExpenseIncomeItemOfEachDateAdapter expenseIncomeItemOfEachDateAdapter = new ExpenseIncomeItemOfEachDateAdapter(
                                    itemIconList,
                                    amountList,
                                    itemList,
                                    ItemsActivity.this,
                                    colors,
                                    category
                            );
                            expenseItemOfEachDateRecyclerView.setAdapter(expenseIncomeItemOfEachDateAdapter);
                            expenseIncomeItemOfEachDateAdapter.setExpenseItemClickListener(ItemsActivity.this);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        }

        return true;
    }

    @Override
    public void onExpenseItemClick(int color, int icon, String amount, String name) {

        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("category", amount_category);
        intent.putExtra("color", color);
        intent.putExtra("date", date);
        intent.putExtra("icon", icon);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}