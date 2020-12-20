package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity implements BalanceFragment.BalanceFragmentListener,
        CashFlowFragment.CashFlowFragmentListener, CategoryMenuDialogBox.CategoryPickerListener, DatePickerDialogBox.DatePickerListener,
        EarningFragment.EarningFragmentListener, HomeScreenFragment.HomeScreenFragmentListener, NavigationView.OnNavigationItemSelectedListener,
        RecordsFragment.RecordsFragmentListener, SpendingFragment.SpendingFragmentListener {

    int currentMonth, dayOfTheWeek;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private BudgetsFragment budgetsFragment;
    private CardView calendarCardView;
    private CategoryMenuDialogBox categoryMenuDialogBox;
    private CircleImageView profilePicImageView;
    private DatabaseReference userDBReference;
    private DatePickerDialogBox datePickerDialogBox;
    private DrawerLayout drawerLayout;
    private FirebaseUser firebaseUser;
    private FrameLayout fragment_container;
    private GoogleSignInClient googleSignInClient;
    private HomeScreenFragment homeScreenFragment;
    private int currentDay,  currentYear;
    private int[] expenses_colors, income_colors;
    private List<String> monthList, yearList;
    private NavigationView navigationView;
    private RecordsFragment recordsFragment;
    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    private StatisticsFragment statisticsFragment;
    private String chosenCategory = "", chosenMonth = "", chosenYear = "", day, email_address, month,  month_name, selected_month,
            selected_year, userId, username;
    private TabPosition tabPosition;
    private TextView monthNameTextView, usernameTextView, emailAddressTextView;

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

    private List<String> sortTheMonths(List<String> monthList) {

        int i, month_no, pos = 0;
        int[] month_nos = new int[monthList.size()];
        String[] temp_months = new String[monthList.size()];
        String[] months = new String[monthList.size()];

        for (i = 0; i < monthList.size(); i++) {

            temp_months[i] = monthList.get(i);

        }

        for (i = 0; i < temp_months.length; i++) {

            month_no = findMonthNo(temp_months[i]);
            month_nos[i] = month_no;

        }

        int [] sortedMonths = sortTheMonthNos(month_nos);

        for (i = 0; i < temp_months.length; i++) {

            month_no = findMonthNo(temp_months[i]);

            for (int j = 0; j < sortedMonths.length; j++) {

                if (month_no == sortedMonths[j]) {

                    pos = j;

                }

            }

            months[pos] = temp_months[i];

        }
        
        monthList.clear();
        
        for (i = 0; i < months.length; i++) {
            
            monthList.add(months[i]);
            
        }
        
        return monthList;
    }

    private int findMonthNo(String month) {

        int month_no;

        switch (month) {

            case "Jan":
                month_no = 1;
                break;

            case "Feb":
                month_no = 2;
                break;

            case "Mar":
                month_no = 3;
                break;

            case "Apr":
                month_no = 4;
                break;

            case "May":
                month_no = 5;
                break;

            case "June":
                month_no = 6;
                break;

            case "July":
                month_no = 7;
                break;

            case "Aug":
                month_no = 8;
                break;

            case "Sep":
                month_no = 9;
                break;

            case "Oct":
                month_no = 10;
                break;

            case "Nov":
                month_no = 11;
                break;

            case "Dec":
                month_no = 12;
                break;

            default:
                month_no = 0;
                break;

        }

        return month_no;
    }

    private int[] sortTheMonthNos(int[] month_nos) {

        int temp;

        for (int i = 0; i < month_nos.length; i++) {

            for (int j = i + 1; j < month_nos.length; j++) {

                if (month_nos[i] > month_nos[j]) {

                    temp = month_nos[i];
                    month_nos[i] = month_nos[j];
                    month_nos[j] = temp;

                }

            }

        }

        return month_nos;
    }

    private String findDay(int dayOfTheWeek) {

        String day = "";

        switch (dayOfTheWeek) {

            case 1:
                day = "Sun";
                break;

            case 2:
                day = "Mon";
                break;

            case 3:
                day = "Tue";
                break;

            case 4:
                day = "Wed";
                break;

            case 5:
                day = "Thu";
                break;

            case 6:
                day = "Fri";
                break;

            case 7:
                day = "Sat";
                break;

        }

        return day;
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

    private void changeTheActionBarTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);

        }

    }

    private void initialization() {

        budgetsFragment = new BudgetsFragment();
        Calendar calendar = Calendar.getInstance();
        calendarCardView = findViewById(R.id.calendarCardView);
        chosenCategory = "Expense_Category";
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        chosenYear = String.valueOf(currentYear);
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currentMonth = currentMonth + 1;
        day = findDay(dayOfTheWeek);
        month = findMonth(currentMonth);
        chosenMonth = month;
        datePickerDialogBox = new DatePickerDialogBox(
                chosenYear,
                day + ", " + month + " " + currentDay,
                month,
                String.valueOf(currentYear)
        );
        drawerLayout = findViewById(R.id.drawerLayout);
        expenses_colors = new int[] {
                getResources().getColor(R.color.amber, getTheme()),
                getResources().getColor(R.color.blue, getTheme()),
                getResources().getColor(R.color.blue_gray, getTheme()),
                getResources().getColor(R.color.brown, getTheme()),
                getResources().getColor(R.color.cyan, getTheme()),
                getResources().getColor(R.color.deep_orange, getTheme()),
                getResources().getColor(R.color.deep_purple, getTheme()),
                getResources().getColor(R.color.gray, getTheme()),
                getResources().getColor(R.color.green, getTheme()),
                getResources().getColor(R.color.indigo, getTheme()),
                getResources().getColor(R.color.light_blue, getTheme()),
                getResources().getColor(R.color.light_green, getTheme()),
                getResources().getColor(R.color.lime, getTheme()),
                getResources().getColor(R.color.orange, getTheme()),
                getResources().getColor(R.color.pink, getTheme()),
                getResources().getColor(R.color.purple, getTheme()),
                getResources().getColor(R.color.red, getTheme()),
                getResources().getColor(R.color.teal, getTheme()),
                getResources().getColor(R.color.yellow, getTheme())
        };
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fragment_container = findViewById(R.id.fragment_container);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        homeScreenFragment = new HomeScreenFragment(
                "Expense_Category",
                month,
                String.valueOf(currentYear)
        );
        income_colors = new int[] {
                getResources().getColor(R.color.teal, getTheme()),
                getResources().getColor(R.color.red, getTheme()),
                getResources().getColor(R.color.purple, getTheme()),
                getResources().getColor(R.color.pink, getTheme()),
                getResources().getColor(R.color.orange, getTheme()),
                getResources().getColor(R.color.light_green, getTheme()),
                getResources().getColor(R.color.lime, getTheme()),
                getResources().getColor(R.color.light_blue, getTheme()),
                getResources().getColor(R.color.indigo, getTheme()),
                getResources().getColor(R.color.green, getTheme()),
                getResources().getColor(R.color.gray, getTheme()),
                getResources().getColor(R.color.amber, getTheme()),
                getResources().getColor(R.color.blue, getTheme()),
                getResources().getColor(R.color.blue_gray, getTheme()),
                getResources().getColor(R.color.brown, getTheme()),
                getResources().getColor(R.color.cyan, getTheme()),
                getResources().getColor(R.color.deep_orange, getTheme()),
                getResources().getColor(R.color.deep_purple, getTheme()),
                getResources().getColor(R.color.yellow, getTheme())
        };
        monthList = new ArrayList<>();
        monthNameTextView = findViewById(R.id.monthNameTextView);
        monthNameTextView.setText(chosenMonth);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(this);
        recordsFragment = new RecordsFragment(
                "Expense_Category",
                month,
                String.valueOf(currentYear)
        );
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        statisticsFragment = new StatisticsFragment(chosenMonth, String.valueOf(currentYear));
        tabPosition = new TabPosition(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeTheActionBarTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(this);
        userId = userIdConfigActivity.getUserID();
        View mView = navigationView.inflateHeaderView(R.layout.navigation_header);
        emailAddressTextView = mView.findViewById(R.id.emailAddressTextView);
        profilePicImageView = mView.findViewById(R.id.profilePicImageView);
        usernameTextView = mView.findViewById(R.id.usernameTextView);
        yearList = new ArrayList<>();
    }

    private void loadTheFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void loadTheFragmentWithSlideLeftCustomAnimation(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void loadTheFragmentWithSlideRightCustomAnimation(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void showMonthList(final String current_month, final String current_year) {

        selected_month = current_month;
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(current_year)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            boolean found = false;
                            int i, checkedItem = 0;
                            monthList.clear();

                            for (DataSnapshot monthSnapshot : snapshot.getChildren()) {

                                monthList.add(monthSnapshot.getKey());

                            }

                            if (monthList.size() == 1) {

                                selected_month = monthList.get(0);

                            }

                            List<String> arrangedMonthList = sortTheMonths(monthList);
                            final String[] month = new String[monthList.size()];

                            for (i = 0; i < arrangedMonthList.size(); i++) {

                                month[i] = arrangedMonthList.get(i);

                            }

                            for (i = 0; i < month.length; i++) {

                                if (month[i].equals(current_month)) {

                                    checkedItem = i;
                                    found = true;
                                    break;

                                }

                            }

                            if (!found) {

                                checkedItem = -1;

                            }

                            builder = new AlertDialog.Builder(HomeScreenActivity.this);
                            builder.setTitle("Select a month");
                            builder.setSingleChoiceItems(month, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    selected_month = month[i];
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (recordsFragment != null && recordsFragment.isVisible()) {

                                        recordsFragment.updateViews(
                                                HomeScreenActivity.this,
                                                selected_month,
                                                current_year
                                        );

                                    } else if (statisticsFragment != null && statisticsFragment.isVisible()) {

                                        statisticsFragment.updateTheFragment(
                                                HomeScreenActivity.this,
                                                tabPosition.getTabPosition(),
                                                selected_month,
                                                current_year
                                        );

                                    }

                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog = builder.create();
                            dialog.show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
    }

    private void showToast(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showYearList(final String current_month, final String current_year) {

        selected_year = current_year;
        userDBReference.child(userId)
                .child("BEIAmount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            boolean found = false;
                            int i, checkedItem = 0;
                            yearList.clear();

                            for (DataSnapshot yearSnapshot : snapshot.getChildren()) {

                                yearList.add(yearSnapshot.getKey());

                            }

                            final String[] year = new String[yearList.size()];

                            for (i = 0; i < yearList.size(); i++) {

                                year[i] = yearList.get(i);

                            }

                            for (i = 0; i < year.length; i++) {

                                if (year[i].equals(current_year)) {

                                    checkedItem = i;
                                    found = true;
                                    break;

                                }

                            }

                            if (!found) {

                                checkedItem = -1;

                            }

                            builder = new AlertDialog.Builder(HomeScreenActivity.this);
                            builder.setTitle("Select a year");
                            builder.setSingleChoiceItems(year, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    selected_year = year[i];
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (recordsFragment != null && recordsFragment.isVisible()) {

                                        recordsFragment.updateViews(
                                                HomeScreenActivity.this,
                                                current_month,
                                                selected_year
                                        );

                                    } else if (statisticsFragment != null && statisticsFragment.isVisible()) {

                                        statisticsFragment.updateTheFragment(
                                                HomeScreenActivity.this,
                                                tabPosition.getTabPosition(),
                                                current_month,
                                                selected_year
                                        );

                                    }

                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog = builder.create();
                            dialog.show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                if (data != null) {

                    if (!chosenMonth.equals(data.getStringExtra("month")) &&
                            !chosenYear.equals(data.getStringExtra("year"))) {

                        month_name = data.getStringExtra("month") + " " + data.getStringExtra("year");
                        monthNameTextView.setText(month_name);

                    } else if (!chosenMonth.equals(data.getStringExtra("month")) &&
                            chosenYear.equals(data.getStringExtra("year"))) {

                        monthNameTextView.setText(data.getStringExtra("month"));

                    } else if (chosenMonth.equals(month) && !chosenYear.equals(data.getStringExtra("year"))) {

                        month_name = chosenMonth + " " + data.getStringExtra("year");
                        monthNameTextView.setText(month_name);

                    } else if (chosenMonth.equals(month) && chosenYear.equals(String.valueOf(currentYear))) {

                        monthNameTextView.setText(chosenMonth);

                    }

                    chosenCategory = data.getStringExtra("category");
                    chosenMonth = data.getStringExtra("month");
                    chosenYear = data.getStringExtra("year");
                    homeScreenFragment.updateTheViews(
                            this,
                            data.getStringExtra("category"),
                            data.getStringExtra("month"),
                            data.getStringExtra("year")
                    );
                    showToast("Updated successfully.");

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initialization();

        if (fragment_container != null) {

            if (savedInstanceState != null) {

                return;

            }

            loadTheFragment(homeScreenFragment);

        }

        if (firebaseUser != null) {

            if (signInUsingEmailConfigActivity.getSignInUsingEmailStatus()) {

                try {

                    userDBReference.child(userId)
                            .child("Account_Details")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {

                                        CreateNewUser user = snapshot.getValue(CreateNewUser.class);

                                        if (user != null) {

                                            email_address = user.getEmail_address();
                                            emailAddressTextView.setText(email_address);
                                            Glide.with(HomeScreenActivity.this).load(R.drawable.user_prof_pic).into(profilePicImageView);
                                            username = user.getUsername();
                                            usernameTextView.setText(username);

                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Log.e("database_error", error.getMessage());
                                }
                            });

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }

            } else if (signInUsingGoogleConfigActivity.getSignInUsingGoogleStatus()) {

                emailAddressTextView.setText(firebaseUser.getEmail());
                Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profilePicImageView);
                usernameTextView.setText(firebaseUser.getDisplayName());

            }

        }

        calendarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chosenYear.equals(String.valueOf(currentYear))) {

                    if (!chosenMonth.equals(month)) {

                        datePickerDialogBox = new DatePickerDialogBox(
                                chosenYear,
                                day + ", " + month + " " + currentDay,
                                chosenMonth,
                                String.valueOf(currentYear)
                        );

                    } else {

                        datePickerDialogBox = new DatePickerDialogBox(
                                chosenYear,
                                day + ", " + month + " " + currentDay,
                                month,
                                String.valueOf(currentYear)
                        );

                    }

                } else if (!chosenYear.equals(String.valueOf(currentYear))) {

                    if (!chosenMonth.equals(month)) {

                        datePickerDialogBox = new DatePickerDialogBox(
                                chosenYear,
                                day + ", " + month + " " + currentDay,
                                chosenMonth,
                                String.valueOf(currentYear)
                        );

                    } else {

                        datePickerDialogBox = new DatePickerDialogBox(
                                chosenYear,
                                day + ", " + month + " " + currentDay,
                                month,
                                String.valueOf(currentYear)
                        );

                    }

                }

                datePickerDialogBox.show(getSupportFragmentManager(), "date picker dialog");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.budgets) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Budgets");
            loadTheFragmentWithSlideLeftCustomAnimation(budgetsFragment);

        } else if (id == R.id.calendar) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Calendar");
            Toast.makeText(this, "calendar", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.goals) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Chart");
            Toast.makeText(this, "goals", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.help) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Help");
            Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.home) {

            calendarCardView.setVisibility(View.VISIBLE);
            changeTheActionBarTitle("");
            loadTheFragmentWithSlideLeftCustomAnimation(homeScreenFragment);
            monthNameTextView.setText(month);

        } else if (id == R.id.records) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Records");
            loadTheFragmentWithSlideLeftCustomAnimation(recordsFragment);

        } else if (id == R.id.settings) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Settings");
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.shopping_lists) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Shopping Lists");
            Toast.makeText(this, "shopping lists", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.sign_out) {

            if (signInUsingGoogleConfigActivity.getSignInUsingGoogleStatus()) {

                googleSignInClient.signOut().addOnCompleteListener(HomeScreenActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            showToast("Successfully signed out.");
                            signInUsingGoogleConfigActivity.setSignInUsingGoogleStatus(false);
                            startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                            finish();

                        } else {

                            showToast(Objects.requireNonNull(task.getException()).getMessage());

                        }

                    }
                });

            } else if (signInUsingEmailConfigActivity.getSignInUsingEmailStatus()) {

                FirebaseAuth.getInstance().signOut();
                showToast("Successfully signed out.");
                signInUsingEmailConfigActivity.setSignInUsingEmailStatus(false);
                signInUsingGoogleConfigActivity.setSignInUsingGoogleStatus(false);
                startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                finish();

            }

        } else if (id == R.id.statistics) {

            calendarCardView.setVisibility(View.GONE);
            changeTheActionBarTitle("Statistics");
            loadTheFragmentWithSlideLeftCustomAnimation(statisticsFragment);

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void cancel() {

        datePickerDialogBox.dismiss();
    }

    @Override
    public void cancel_menu() {

        categoryMenuDialogBox.dismiss();
    }

    @Override
    public void chooseMonth(String month) {

        chosenMonth = month;
    }

    @Override
    public void chooseNextYear() {

        if (datePickerDialogBox.getYear().equals(String.valueOf(currentYear))) {

            datePickerDialogBox.setYear(String.valueOf(currentYear + 1));

        } else {

            datePickerDialogBox.setYear(String.valueOf(Integer.parseInt(datePickerDialogBox.getYear()) + 1));

        }

        chosenYear = datePickerDialogBox.getYear();
    }

    @Override
    public void choosePreviousYear() {

        if (datePickerDialogBox.getYear().equals(String.valueOf(currentYear))) {

            datePickerDialogBox.setYear(String.valueOf(currentYear - 1));

        } else {

            datePickerDialogBox.setYear(String.valueOf(Integer.parseInt(datePickerDialogBox.getYear()) - 1));

        }

        chosenYear = datePickerDialogBox.getYear();
    }

    @Override
    public void confirm(String category) {

        if (category.equals("Expense_Category")) {

            chosenCategory = "Expense_Category";

        } else if (category.equals("Income_Category")) {

            chosenCategory = "Income_Category";

        }

        homeScreenFragment.updateTheViews(
                this,
                category,
                chosenMonth,
                chosenYear
        );

        categoryMenuDialogBox.dismiss();
    }

    @Override
    public void goToExpenseIncomeActivity() {

        startActivityForResult(new Intent(this, ExpenseIncomeActivity.class), 1);
    }

    @Override
    public void goToItemsActivity(String date) {

        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra("category", chosenCategory);
        intent.putExtra("date", date);
        intent.putExtra("month", chosenMonth);
        intent.putExtra("year", chosenYear);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else if ((budgetsFragment != null && budgetsFragment.isVisible())
                || (recordsFragment != null && recordsFragment.isVisible())
                || (statisticsFragment != null && statisticsFragment.isVisible())) {

            changeTheActionBarTitle("");
            calendarCardView.setVisibility(View.VISIBLE);
            homeScreenFragment = new HomeScreenFragment(
                    chosenCategory,
                    chosenMonth,
                    chosenYear
            );
            loadTheFragmentWithSlideRightCustomAnimation(homeScreenFragment);
            navigationView.setCheckedItem(R.id.home);

        } else if (homeScreenFragment != null && homeScreenFragment.isVisible()) {

            super.onBackPressed();

        }
    }

    @Override
    public void ok() {

        datePickerDialogBox.dismiss();

        if (!chosenMonth.equals(month) && !chosenYear.equals(String.valueOf(currentYear))) {

            month_name = chosenMonth + " " + chosenYear;
            monthNameTextView.setText(month_name);

        } else if (!chosenMonth.equals(month) && chosenYear.equals(String.valueOf(currentYear))) {

            monthNameTextView.setText(chosenMonth);

        } else if (chosenMonth.equals(month) && !chosenYear.equals(String.valueOf(currentYear))) {

            month_name = chosenMonth + " " + chosenYear;
            monthNameTextView.setText(month_name);

        } else if (chosenMonth.equals(month) && chosenYear.equals(String.valueOf(currentYear))) {

            monthNameTextView.setText(chosenMonth);

        }

        homeScreenFragment.updateTheViews(
                this,
                chosenCategory,
                chosenMonth,
                chosenYear
        );
    }

    @Override
    public void showCategoryMenu() {

        categoryMenuDialogBox = new CategoryMenuDialogBox(chosenCategory);
        categoryMenuDialogBox.show(getSupportFragmentManager(), "category menu dialog box");
    }

    @Override
    public void showMonthListForBalance(final String current_month, final String current_year) {

        showMonthList(current_month, current_year);
    }

    @Override
    public void showYearListForBalance(String current_month, String current_year) {

        showYearList(current_month, current_year);
    }

    @Override
    public void showMonthListForCashFlow(String current_month, String current_year) {

        showMonthList(current_month, current_year);
    }

    @Override
    public void showYearListForCashFlow(String current_month, String current_year) {

        showYearList(current_month, current_year);
    }

    @Override
    public void showMonthListForEarning(String current_month, String current_year) {

        showMonthList(current_month, current_year);
    }

    @Override
    public void showMoreIncomeForEarning(ArrayList<String> amountList, ArrayList<String> itemList) {

        IncomeItemsListDialogBox incomeItemsListDialogBox = new IncomeItemsListDialogBox(
                addIconsToIconList(itemList),
                amountList,
                itemList,
                income_colors
        );
        incomeItemsListDialogBox.show(getSupportFragmentManager(), "more_income");
    }

    @Override
    public void showYearListForEarning(String current_month, String current_year) {

        showYearList(current_month, current_year);
    }

    @Override
    public void showMonthListForRecords(String current_month, String current_year) {

        showMonthList(current_month, current_year);
    }

    @Override
    public void showYearListForRecords(String current_month, String current_year) {

        showYearList(current_month, current_year);
    }

    @Override
    public void showMonthListForSpending(String current_month, String current_year) {

        showMonthList(current_month, current_year);
    }

    @Override
    public void showMoreExpensesForSpending(ArrayList<String> amountList, ArrayList<String> itemList) {

        ExpenseItemsListDialogBox expenseItemsListDialogBox = new ExpenseItemsListDialogBox(
                addIconsToIconList(itemList),
                amountList,
                itemList,
                expenses_colors
        );
        expenseItemsListDialogBox.show(getSupportFragmentManager(), "more_expenses");
    }

    @Override
    public void showYearListForSpending(String current_month, String current_year) {

        showYearList(current_month, current_year);
    }
}
