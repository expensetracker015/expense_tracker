package com.jaekapps.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreenFragment extends Fragment implements View.OnClickListener {

    private BEIAmount beiAmount;
    private CardView beiAmountCardView, expenseCardView, incomeCardView;
    private DatabaseReference userDBReference;
    private FloatingActionButton expenseIncomeFab;
    private HomeScreenFragmentListener homeScreenFragmentListener;
    private LinearLayout noTransactionLayout;
    private List<String> dateList;
    private ProgressBar loadingProgressbar;
    private RecyclerView homeScreenFragmentDateRecyclerView;
    private RelativeLayout categoryLayout;
    private String category, month, userId, year;
    private TextView balanceTextView, categoryTextView, expenseTextView, incomeTextView;

    HomeScreenFragment(String category, String month, String year) {

        this.category = category;
        this.month = month;
        this.year = year;
    }

    private String putComma(String amount) {

        char[] amt;
        int flag = 0, i, pos = 0;
        String new_amount = "";
        StringBuilder amountBuilder = new StringBuilder(new_amount);

        if (amount.contains(".")) {

            amt = amount.toCharArray();

            for (i = 0; i < amt.length; i++) {

                if (amt[i] == '.') {

                    pos = i;
                    break;

                }

            }

            for (i = pos - 1; i >= 0; i--) {

                flag++;

                if (flag <= 3) {

                    amountBuilder.append(amt[i]);

                } else {

                    amountBuilder.append(",").append(amt[i]);
                    flag = 0;

                }

            }

            amountBuilder.reverse();

            for (i = pos; i < amt.length; i++) {

                amountBuilder.append(amt[i]);

            }

            new_amount = amountBuilder.toString();

        } else {

            amt = amount.toCharArray();

            for (i = amt.length - 1; i >= 0; i--) {

                flag++;

                if (flag <= 3) {

                    amountBuilder.append(amt[i]);

                } else {

                    amountBuilder.append(",").append(amt[i]);
                    flag = 0;

                }

            }

            amountBuilder.reverse();
            new_amount = amountBuilder.toString();

        }

        return new_amount;
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

    private void initializeOnClickListener() {

        expenseIncomeFab.setOnClickListener(this);
        expenseCardView.setOnClickListener(this);
        incomeCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        balanceTextView = view.findViewById(R.id.balanceTextView);
        beiAmount = new BEIAmount();
        beiAmountCardView = view.findViewById(R.id.beiAmountCardView);
        beiAmountCardView.setVisibility(View.GONE);
        categoryLayout = view.findViewById(R.id.categoryLayout);
        categoryLayout.setVisibility(View.GONE);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        dateList = new ArrayList<>();
        expenseCardView = view.findViewById(R.id.expenseCardView);
        expenseIncomeFab = view.findViewById(R.id.expenseIncomeFab);
        expenseTextView = view.findViewById(R.id.expenseTextView);
        homeScreenFragmentDateRecyclerView = view.findViewById(R.id.homeScreenFragmentDateRecyclerView);
        homeScreenFragmentDateRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeScreenFragmentDateRecyclerView.setHasFixedSize(true);
        incomeCardView = view.findViewById(R.id.incomeCardView);
        incomeTextView = view.findViewById(R.id.incomeTextView);
        loadingProgressbar = view.findViewById(R.id.loadingProgressbar);
        noTransactionLayout = view.findViewById(R.id.noTransactionLayout);
        noTransactionLayout.setVisibility(View.GONE);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(view.getContext());
        userId = userIdConfigActivity.getUserID();
    }

    public interface HomeScreenFragmentListener {

        void goToExpenseIncomeActivity();
        void showExpensePercentage();
        void showIncomePercentage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.home_screen_fragment, container, false);
        initializeViews(view);
        initializeOnClickListener();
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);
                            beiAmountCardView.setVisibility(View.VISIBLE);
                            categoryLayout.setVisibility(View.VISIBLE);
                            String category_title = "";

                            if (category.equals("Expense_Category")) {

                                category_title = view.getContext()
                                        .getString(R.string.category) + " - " + view.getContext().getString(R.string.expenses);

                            } else if (category.equals("Income_Category")) {

                                category_title = view.getContext()
                                        .getString(R.string.category) + " - " + view.getContext().getString(R.string.income);

                            }

                            categoryTextView.setText(category_title);
                            loadingProgressbar.setVisibility(View.GONE);

                            if (beiAmount != null) {

                                beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                beiAmount.setExpense(putComma(beiAmount.getExpense()));
                                beiAmount.setIncome(putComma(beiAmount.getIncome()));
                                balanceTextView.setText(beiAmount.getBalance());
                                expenseTextView.setText(beiAmount.getExpense());
                                incomeTextView.setText(beiAmount.getIncome());


                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        userDBReference.child(userId)
                .child(category)
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            dateList.clear();

                            for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                String date;
                                date = replaceUnderscoreWithSlash(Objects.requireNonNull(dateSnapshot.getKey()));
                                dateList.add(date);

                            }

                            DateOfEachMonthRecyclerAdapter dateOfEachMonthRecyclerAdapter = new DateOfEachMonthRecyclerAdapter(dateList);
                            homeScreenFragmentDateRecyclerView.setAdapter(dateOfEachMonthRecyclerAdapter);

                        } else {

                            loadingProgressbar.setVisibility(View.GONE);
                            noTransactionLayout.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            homeScreenFragmentListener = (HomeScreenFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement HomeScreenFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.expenseCardView:
                homeScreenFragmentListener.showExpensePercentage();
                break;

            case R.id.expenseIncomeFab:
                homeScreenFragmentListener.goToExpenseIncomeActivity();
                break;

            case R.id.incomeCardView:
                homeScreenFragmentListener.showIncomePercentage();
                break;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        homeScreenFragmentListener = null;
    }
}
