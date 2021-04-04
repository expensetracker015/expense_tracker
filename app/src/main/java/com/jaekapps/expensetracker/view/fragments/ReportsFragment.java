package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.BEIAmount;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private BEIAmount beiAmount;
    private DatabaseReference userDBReference;
    private final String month, year;
    private List<String> dateList, expenseItemList, incomeItemList;
    private String cash_flow, date, expense, income, userId;
    private TextView avgDayExpenseTextView, avgDayIncomeTextView, avgRecordsExpenseTextView, avgRecordsIncomeTextView,
            cashFlowTextView, cashFlowMonthTextView, expenseItemCountTextView, incomeItemCountTextView, totalExpenseTextView, totalIncomeTextView;

    public ReportsFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private int getNoOfDaysInAMonth(String month, String year) {

        int noOfDays;

        switch (month) {
            case "Jan":
            case "Mar":
            case "May":
            case "July":
            case "Aug":
            case "Oct":
            case "Dec":
                noOfDays = 31;
                break;

            case "Apr":
            case "June":
            case "Sep":
            case "Nov":
                noOfDays = 30;
                break;

            case "Feb":

                int y = Integer.parseInt(year);

                if (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0)) {

                    noOfDays = 29;

                } else {

                    noOfDays = 28;

                }

                break;

            default:
                noOfDays = 0;
                break;
        }

        return noOfDays;
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

            } else {

                new_amount = amount;

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

    private void initializeViews(View view) {

        avgDayExpenseTextView = view.findViewById(R.id.avgDayExpenseTextView);
        avgDayIncomeTextView = view.findViewById(R.id.avgDayIncomeTextView);
        avgRecordsExpenseTextView = view.findViewById(R.id.avgRecordsExpenseTextView);
        avgRecordsIncomeTextView = view.findViewById(R.id.avgRecordsIncomeTextView);
        beiAmount = new BEIAmount();
        cashFlowTextView = view.findViewById(R.id.cashFlowTextView);
        cashFlowMonthTextView = view.findViewById(R.id.cashFlowMonthTextView);
        dateList = new ArrayList<>();
        expenseItemCountTextView = view.findViewById(R.id.expenseItemCountTextView);
        expenseItemList = new ArrayList<>();
        incomeItemCountTextView = view.findViewById(R.id.incomeItemCountTextView);
        incomeItemList = new ArrayList<>();
        totalExpenseTextView = view.findViewById(R.id.totalExpenseTextView);
        totalIncomeTextView = view.findViewById(R.id.totalIncomeTextView);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
    }

    public void updateTheViews(final Context context, final String month, final String year) {

        String m = month + ", " + year;
        cashFlowMonthTextView.setText(m);
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);

                            if (beiAmount != null) {

                                final String tempExpense = beiAmount.getExpense();
                                final String tempIncome = beiAmount.getIncome();
                                beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                beiAmount.setExpense(putComma(beiAmount.getExpense()));
                                beiAmount.setIncome(putComma(beiAmount.getIncome()));
                                cash_flow = context.getResources().getString(R.string.rupees) + " " + beiAmount.getBalance();
                                cashFlowTextView.setText(cash_flow);
                                expense = context.getResources().getString(R.string.rupees) + " " + beiAmount.getExpense();
                                income = context.getResources().getString(R.string.rupees) + " " + beiAmount.getIncome();
                                totalExpenseTextView.setText(expense);
                                totalIncomeTextView.setText(income);
                                userDBReference.child(userId)
                                        .child("Expense_Category")
                                        .child(year)
                                        .child(month)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {

                                                    if (snapshot.getChildrenCount() == 1) {

                                                        date = snapshot.getKey();

                                                        if (date != null) {

                                                            expenseItemList.clear();

                                                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    expenseItemList.add(subItemSnapshot.getKey());

                                                                }

                                                            }

                                                            double exp = Double.parseDouble(tempExpense);
                                                            double average_day = Double.parseDouble(new DecimalFormat("##.##").format(exp / getNoOfDaysInAMonth(month, year)));
                                                            double average_records = Double.parseDouble(new DecimalFormat("##.##").format(exp / expenseItemList.size()));
                                                            String average_day_expense = "-" + context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_day));
                                                            String average_records_expense = "-" + context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_records));
                                                            avgDayExpenseTextView.setText(average_day_expense);
                                                            avgRecordsExpenseTextView.setText(average_records_expense);
                                                            expenseItemCountTextView.setText(String.valueOf(expenseItemList.size()));

                                                        }

                                                    } else {

                                                        dateList.clear();
                                                        expenseItemList.clear();

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            dateList.add(dateSnapshot.getKey());

                                                        }

                                                        for (int i = 0; i < dateList.size(); i++) {

                                                            for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(i)).getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    expenseItemList.add(subItemSnapshot.getKey());

                                                                }

                                                            }

                                                        }

                                                        double exp = Double.parseDouble(tempExpense);
                                                        double average_day = Double.parseDouble(new DecimalFormat("##.##").format(exp / getNoOfDaysInAMonth(month, year)));
                                                        double average_records = Double.parseDouble(new DecimalFormat("##.##").format(exp / expenseItemList.size()));
                                                        String average_day_expense = "-" + context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_day));
                                                        String average_records_expense = "-" + context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_records));
                                                        avgDayExpenseTextView.setText(average_day_expense);
                                                        avgRecordsExpenseTextView.setText(average_records_expense);
                                                        expenseItemCountTextView.setText(String.valueOf(expenseItemList.size()));

                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                Log.e("database_error", error.getMessage());
                                            }
                                        });
                                userDBReference.child(userId)
                                        .child("Income_Category")
                                        .child(year)
                                        .child(month)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {

                                                    if (snapshot.getChildrenCount() == 1) {

                                                        date = snapshot.getKey();

                                                        if (date != null) {

                                                            incomeItemList.clear();

                                                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    incomeItemList.add(subItemSnapshot.getKey());

                                                                }

                                                            }

                                                            double inc = Double.parseDouble(tempIncome);
                                                            double average_day = Double.parseDouble(new DecimalFormat("##.##").format(inc / getNoOfDaysInAMonth(month, year)));
                                                            double average_records = Double.parseDouble(new DecimalFormat("##.##").format(inc / incomeItemList.size()));
                                                            String average_day_income = context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_day));
                                                            String average_records_income = context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_records));
                                                            avgDayIncomeTextView.setText(average_day_income);
                                                            avgRecordsIncomeTextView.setText(average_records_income);
                                                            incomeItemCountTextView.setText(String.valueOf(incomeItemList.size()));

                                                        }

                                                    } else {

                                                        dateList.clear();
                                                        incomeItemList.clear();

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            dateList.add(dateSnapshot.getKey());

                                                        }

                                                        for (int i = 0; i < dateList.size(); i++) {

                                                            for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(i)).getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    incomeItemList.add(subItemSnapshot.getKey());

                                                                }

                                                            }

                                                        }

                                                        double inc = Double.parseDouble(tempIncome);
                                                        double average_day = Double.parseDouble(new DecimalFormat("##.##").format(inc / getNoOfDaysInAMonth(month, year)));
                                                        double average_records = Double.parseDouble(new DecimalFormat("##.##").format(inc / incomeItemList.size()));
                                                        String average_day_income = context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_day));
                                                        String average_records_income = context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(average_records));
                                                        avgDayIncomeTextView.setText(average_day_income);
                                                        avgRecordsIncomeTextView.setText(average_records_income);
                                                        incomeItemCountTextView.setText(String.valueOf(incomeItemList.size()));

                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                Log.e("database_error", error.getMessage());
                                            }
                                        });

                            }

                        } else {

                            String avg_exp = context.getResources().getString(R.string.rupees) + " 0";
                            avgDayExpenseTextView.setText(avg_exp);
                            avgRecordsExpenseTextView.setText(avg_exp);
                            String avg_inc = context.getResources().getString(R.string.rupees) + " 0";
                            avgDayIncomeTextView.setText(avg_inc);
                            avgRecordsIncomeTextView.setText(avg_inc);
                            cash_flow = context.getResources().getString(R.string.rupees) + " 0";
                            cashFlowTextView.setText(cash_flow);
                            expense = context.getResources().getString(R.string.rupees) + " 0";
                            expenseItemCountTextView.setText("0");
                            incomeItemCountTextView.setText("0");
                            income = context.getResources().getString(R.string.rupees) + " 0";
                            totalExpenseTextView.setText(expense);
                            totalIncomeTextView.setText(income);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        initializeViews(view);
        updateTheViews(view.getContext(), month, year);
        return view;
    }
}
