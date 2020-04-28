package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    CategorySharedPreferences categorySharedPreferences;
    DatePickerDialog datePickerDialog;
    DatabaseReference accountDetailsDatabaseReference, beiAmountDatabaseReference, beiAmountMonthDatabaseReference;
    DatabaseReference beiAmountYearDatabaseReference, categoryDatabaseReference, categoryYearDatabaseReference, categoryMonthDatabaseReference;
    DatabaseReference userDatabaseReference, userIdDatabaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FrameLayout fragmentContainer;
    int checkedItem = 0, currentDay, currentMonth, currentYear, selectedDay, selectedMonth, selectedYear;
    LinearLayout calendarDropDownLinearLayout;
    List<String> dateList = new ArrayList<>();
    NavigationView navigationView;
    TextView monthNameTextView;
    private BEIAmount beiAmount;
    private BudgetsFragment budgetsFragment;
    private CircleImageView profilePicImageView;
    private DrawerLayout drawerLayout;
    private GoogleSignInClient googleSignInClient;
    private HomeScreenFragment homeScreenFragment;
    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    private StatisticsFragment statisticsFragment;
    private String balanceAmount, chose_category, email_address, expenseAmount, incomeAmount, month, userId, username;
    private TextView usernameTextView, emailAddressTextView;
    Toolbar toolbar;

    private void findMonth(int currentMonth) {

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

    }

    private void findMonthIfChanged(int selectedMonth, int year) {

        String tempMonth = "";

        switch (selectedMonth) {

            case 1:
                tempMonth = "Jan";
                break;

            case 2:
                tempMonth = "Feb";
                break;

            case 3:
                tempMonth = "Mar";
                break;

            case 4:
                tempMonth = "Apr";
                break;

            case 5:
                tempMonth = "May";
                break;

            case 6:
                tempMonth = "June";
                break;

            case 7:
                tempMonth = "July";
                break;

            case 8:
                tempMonth = "Aug";
                break;

            case 9:
                tempMonth = "Sep";
                break;

            case 10:
                tempMonth = "Oct";
                break;

            case 11:
                tempMonth = "Nov";
                break;

            case 12:
                tempMonth = "Dec";
                break;

        }

        monthNameTextView.setText(tempMonth);

        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

            getDateOfItemsFromDatabase("Expense_Category", tempMonth, String.valueOf(selectedYear));

        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

            getDateOfItemsFromDatabase("Income_Category", tempMonth, String.valueOf(selectedYear));

        }

        getBEIAmountFromDatabase(year, tempMonth);

    }

    private int findMonthNumber(String month) {

        int no = 0;

        switch (month) {

            case "Jan":
                no = 0;
                break;

            case "Feb":
                no = 1;
                break;

            case "Mar":
                no = 2;
                break;

            case "Apr":
                no = 3;
                break;

            case "May":
                no = 4;
                break;

            case "June":
                no = 5;
                break;

            case "July":
                no = 6;
                break;

            case "Aug":
                no = 7;
                break;

            case "Sep":
                no = 8;
                break;

            case "Oct":
                no = 9;
                break;

            case "Nov":
                no = 10;
                break;

            case "Dec":
                no = 11;
                break;

        }

        return no;

    }

    private void getBEIAmountFromDatabase(int year, String month) {

        try {

            beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
            beiAmountYearDatabaseReference = beiAmountDatabaseReference.child(String.valueOf(year));
            beiAmountMonthDatabaseReference = beiAmountYearDatabaseReference.child(month);
            beiAmountMonthDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        beiAmount = dataSnapshot.getValue(BEIAmount.class);
                        balanceAmount = beiAmount.getBalance();
                        expenseAmount = beiAmount.getExpense();
                        incomeAmount = beiAmount.getIncome();
                        homeScreenFragment.setBEIAmount(balanceAmount, expenseAmount, incomeAmount);

                    } else {

                        homeScreenFragment.setBEIAmountForSignInUsingGoogle();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    private void getDateOfItemsFromDatabase(String category, String month, String year) {

        try {

            dateList.clear();
            categoryDatabaseReference = userIdDatabaseReference.child(category);
            categoryYearDatabaseReference = categoryDatabaseReference.child(year);
            categoryMonthDatabaseReference = categoryYearDatabaseReference.child(month);
            categoryMonthDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot dateDataSnapshot : dataSnapshot.getChildren()) {

                            String date;
                            date = replaceUnderscoreWithSlash(dateDataSnapshot.getKey());
                            dateList.add(date);

                        }

                        if (category.equals("Expense_Category")) {

                            homeScreenFragment.hideTheHomeScreenFragmentLinearLayout("Expense Category", dateList);

                        } else {

                            homeScreenFragment.hideTheHomeScreenFragmentLinearLayout("Income Category", dateList);

                        }

                    } else {

                        if (category.equals("Expense_Category")) {

                            homeScreenFragment.showTheHomeScreenFragmentLinearLayout("Expense Category");

                        } else {

                            homeScreenFragment.showTheHomeScreenFragmentLinearLayout("Income Category");

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    private String replaceUnderscoreWithSlash(String date) {

        char[] dateArray = date.toCharArray();

        for (int i = 0; i < dateArray.length; i++) {

            if (dateArray[i] == '_') {

                dateArray[i] = '/';

            }

        }

        date = String.valueOf(dateArray);
        return date;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                if (data != null) {

                    String balance = data.getStringExtra("balance");
                    String category = data.getStringExtra("category");
                    String date = data.getStringExtra("date");
                    int day_no = data.getIntExtra("day_no", 0);
                    String expense = data.getStringExtra("expense");
                    String income = data.getStringExtra("income");
                    String item_name = data.getStringExtra("item_name");
                    String month = data.getStringExtra("month");
                    String total_amount = data.getStringExtra("total_amount");
                    String year = data.getStringExtra("year");
                    beiAmount.setBalance(balance);
                    beiAmount.setExpense(expense);
                    beiAmount.setIncome(income);
                    userDatabaseReference.child(userId).child("BEIAmount").child(year).child(month).setValue(beiAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                userDatabaseReference.child(userId).child(category).child(year).child(month).child(date).child(item_name).setValue(total_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            int no = findMonthNumber(month);
                                            datePickerDialog = DatePickerDialog.newInstance(HomeScreenActivity.this, Integer.parseInt(year), no, day_no);
                                            monthNameTextView.setText(month);
                                            getBEIAmountFromDatabase(Integer.parseInt(year), month);
                                            getDateOfItemsFromDatabase(category, month, year);
                                            Toast.makeText(HomeScreenActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            }

                        }
                    });

                }

            }

        }

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

            finish();

        } else if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

            finish();

        } else {

            super.onBackPressed();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        beiAmount = new BEIAmount();
        budgetsFragment = new BudgetsFragment();
        Calendar calendar = Calendar.getInstance();
        calendarDropDownLinearLayout = findViewById(R.id.calendarDropDownLinearLayout);
        categorySharedPreferences = new CategorySharedPreferences(this);
        categorySharedPreferences.writeCategoryName("Expense");
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = DatePickerDialog.newInstance(this, currentYear, currentMonth, currentDay);
        currentMonth = currentMonth + 1;
        selectedDay = currentDay;
        selectedMonth = currentMonth;
        selectedYear = currentYear;
        drawerLayout = findViewById(R.id.drawerLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();
        findMonth(currentMonth);
        userDatabaseReference = firebaseDatabase.getReference("User");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fragmentContainer = findViewById(R.id.fragmentContainer);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        homeScreenFragment = new HomeScreenFragment();
        monthNameTextView = findViewById(R.id.monthNameTextView);
        monthNameTextView.setText(month);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View mView = navigationView.inflateHeaderView(R.layout.navigation_header);
        profilePicImageView = mView.findViewById(R.id.profilePicImageView);
        usernameTextView = mView.findViewById(R.id.usernameTextView);
        emailAddressTextView = mView.findViewById(R.id.emailAddressTextView);
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        statisticsFragment = new StatisticsFragment();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        try {

            getSupportActionBar().setTitle("");
            userId = firebaseUser.getUid();

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

        if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

            if (firebaseUser != null) {

                try {

                    emailAddressTextView.setText(firebaseUser.getEmail());
                    usernameTextView.setText(firebaseUser.getDisplayName());
                    Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profilePicImageView);
                    userIdDatabaseReference = userDatabaseReference.child(userId);
                    userIdDatabaseReference = userDatabaseReference.child(userId);
                    beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
                    beiAmountDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                getBEIAmountFromDatabase(selectedYear, month);

                            } else {

                                homeScreenFragment.setBEIAmountForSignInUsingGoogle();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    getDateOfItemsFromDatabase("Expense_Category", month, String.valueOf(selectedYear));

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }

            }

        } else if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

            if (firebaseUser != null) {

                try {

                    userIdDatabaseReference = userDatabaseReference.child(userId);
                    accountDetailsDatabaseReference = userIdDatabaseReference.child("Account_Details");
                    accountDetailsDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                CreateNewUser createNewUser = dataSnapshot.getValue(CreateNewUser.class);
                                email_address = createNewUser.getEmail_address();
                                username = createNewUser.getUsername();
                                emailAddressTextView.setText(email_address);
                                usernameTextView.setText(username);
                                profilePicImageView.setImageResource(R.drawable.user_prof_pic);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Log.e("error", databaseError.getMessage());

                        }
                    });
                    getBEIAmountFromDatabase(selectedYear, month);
                    getDateOfItemsFromDatabase("Expense_Category", month, String.valueOf(selectedYear));

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }

            }

        }

        calendarDropDownLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.setTitle("CALENDAR");
                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorDatePickerDialog));
                datePickerDialog.showYearPickerFirst(true);
                datePickerDialog.show(getSupportFragmentManager(), "date picker dialog");

            }
        });

        if (fragmentContainer != null) {

            if (savedInstanceState != null) {

                return;

            }

            navigationView.setCheckedItem(R.id.home);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, homeScreenFragment).commit();

        }

        homeScreenFragment.setOnItemClickListener(new HomeScreenFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {

                if (view.getId() == R.id.expenseIncomeFab) {

                    Intent intent = new Intent(HomeScreenActivity.this, ExpenseIncomeActivity.class);
                    startActivityForResult(intent, 1);

                } else if (view.getId() == R.id.categoryRelativeLayout) {

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder;
                    String[] items = {"Expenses", "Income"};
                    builder = new AlertDialog.Builder(HomeScreenActivity.this);
                    builder.setTitle("Select the category");
                    builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            checkedItem = i;
                            chose_category = items[i];

                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String tempMonth = "";

                            switch (selectedMonth) {

                                case 1:
                                    tempMonth = "Jan";
                                    break;

                                case 2:
                                    tempMonth = "Feb";
                                    break;

                                case 3:
                                    tempMonth = "Mar";
                                    break;

                                case 4:
                                    tempMonth = "Apr";
                                    break;

                                case 5:
                                    tempMonth = "May";
                                    break;

                                case 6:
                                    tempMonth = "June";
                                    break;

                                case 7:
                                    tempMonth = "July";
                                    break;

                                case 8:
                                    tempMonth = "Aug";
                                    break;

                                case 9:
                                    tempMonth = "Sep";
                                    break;

                                case 10:
                                    tempMonth = "Oct";
                                    break;

                                case 11:
                                    tempMonth = "Nov";
                                    break;

                                case 12:
                                    tempMonth = "Dec";
                                    break;

                            }

                            if (chose_category.equals("Expenses")) {

                                categorySharedPreferences.writeCategoryName("Expense");
                                getDateOfItemsFromDatabase("Expense_Category", tempMonth, String.valueOf(selectedYear));

                            } else if (chose_category.equals("Income")) {

                                categorySharedPreferences.writeCategoryName("Income");
                                getDateOfItemsFromDatabase("Income_Category", tempMonth, String.valueOf(selectedYear));

                            }

                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();

                }

            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (year != currentYear) {

            monthOfYear = monthOfYear + 1;

            if (monthOfYear != currentMonth) {

                selectedYear = year;
                selectedMonth = monthOfYear;

            } else {

                selectedYear = year;
                selectedMonth = monthOfYear;

            }

            findMonthIfChanged(selectedMonth, selectedYear);

        } else {

            monthOfYear = monthOfYear + 1;

            if (monthOfYear != currentMonth) {

                selectedYear = year;
                selectedMonth = monthOfYear;

            } else {

                selectedYear = year;
                selectedMonth = monthOfYear;

            }

            findMonthIfChanged(selectedMonth, selectedYear);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.home:
                getSupportActionBar().setTitle("");
                calendarDropDownLinearLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeScreenFragment).commit();

                try {

                    getBEIAmountFromDatabase(selectedYear, month);
                    getDateOfItemsFromDatabase("Expense_Category", month, String.valueOf(selectedYear));

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }

                break;

            case R.id.statistics:
                getSupportActionBar().setTitle("Statistics");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, statisticsFragment).commit();
                break;

            case R.id.budgets:
                getSupportActionBar().setTitle("Budgets");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, budgetsFragment).commit();
                break;

            case R.id.calendar:
                getSupportActionBar().setTitle("Calendar");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "calendar", Toast.LENGTH_SHORT).show();
                break;

            case R.id.chart:
                getSupportActionBar().setTitle("Chart");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "chart", Toast.LENGTH_SHORT).show();
                break;

            case R.id.goals:
                getSupportActionBar().setTitle("Goals");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "goals", Toast.LENGTH_SHORT).show();
                break;

            case R.id.shopping_lists:
                getSupportActionBar().setTitle("Shopping Lists");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "shopping lists", Toast.LENGTH_SHORT).show();
                break;

            case R.id.help:
                getSupportActionBar().setTitle("Help");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings:
                getSupportActionBar().setTitle("Settings");
                calendarDropDownLinearLayout.setVisibility(View.GONE);
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sign_out:

                if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

                    googleSignInClient.signOut().addOnCompleteListener(HomeScreenActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(false);
                                Toast.makeText(HomeScreenActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                                finish();

                            } else {

                                Toast.makeText(HomeScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                } else if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

                    FirebaseAuth.getInstance().signOut();
                    signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(false);
                    signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(false);
                    Toast.makeText(HomeScreenActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                    finish();

                }

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
