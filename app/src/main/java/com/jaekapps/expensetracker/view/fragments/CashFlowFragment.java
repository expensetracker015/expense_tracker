package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaekapps.expensetracker.model.BEIAmount;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CashFlowFragment extends Fragment implements View.OnClickListener {

    float[] expense_amount_percentage, income_amount_percentage;
    int currentMonth, expenseProgress, incomeProgress;
    private ArrayList<PieEntry> expenseDateArrayList, incomeDateArrayList;
    private ArrayList<String> amountList, expenseAmountList, expenseDateList, incomeAmountList, incomeDateList;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private CashFlowFragmentListener cashFlowFragmentListener;
    private DatabaseReference userDBReference;
    private DecimalFormat decimalFormat;
    private int[] expenses_colors, income_colors;
    private int currentYear;
    private PieChart expensesPieChart, incomePieChart;
    private PieData expensesPieData, incomePieData;
    private PieDataSet expensesPieDataSet, incomePieDataSet;
    private RoundedHorizontalProgressBar cashFlowExpenseProgressBar, cashFlowIncomeProgressBar;
    private String balance, current_month, date, expense, income, month, year, userId;
    private StringBuilder dateBuilder;
    private TextView cashFlowBalanceAmountTextView, cashFlowCurrentMonthTextView, cashFlowExpenseAmountTextView,
            cashFlowIncomeAmountTextView, expensesAmountTextView, expensesCurrentMonthTextView, incomeAmountTextView,
            incomeCurrentMonthTextView, monthTitleTextView, yearTitleTextView;

    CashFlowFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private String convertToFullName(String month) {

        switch (month) {

            case "Jan":
                month = "January";
                break;

            case "Feb":
                month = "February";
                break;

            case "Mar":
                month = "March";
                break;

            case "Apr":
                month = "April";
                break;

            case "May":
                month = "May";
                break;

            case "June":
                month = "June";
                break;

            case "July":
                month = "July";
                break;

            case "Aug":
                month = "August";
                break;

            case "Sep":
                month = "September";
                break;

            case "Oct":
                month = "October";
                break;

            case "Nov":
                month = "November";
                break;

            case "Dec":
                month = "December";
                break;

        }

        return month;
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

    private String removeYearFromDateIfYearIsSame(String date, String year) {

        if (String.valueOf(currentYear).equals(year)) {

            char[] date_array = date.toCharArray();
            int flag = 0, slash_pos = 0;

            for (int  i = 0; i < date_array.length; i++) {

                if (date_array[i] == '/') {

                    flag++;

                }

                if (flag == 2) {

                    slash_pos = i;
                    break;

                }

            }

            for (int i = 0; i < slash_pos; i++) {

                dateBuilder.append(date_array[i]);

            }

            date = dateBuilder.toString();
            dateBuilder = new StringBuilder();

        }

        return date;
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

    private void changeMonthNameIfMonthOrYearIsNotSame(String month_info) {

        cashFlowCurrentMonthTextView.setText(month_info);
        expensesCurrentMonthTextView.setText(month_info);
        incomeCurrentMonthTextView.setText(month_info);
    }

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        amountList = new ArrayList<>();
        beiAmount = new BEIAmount();
        Calendar calendar = Calendar.getInstance();
        cashFlowBalanceAmountTextView = view.findViewById(R.id.cashFlowBalanceAmountTextView);
        cashFlowCurrentMonthTextView = view.findViewById(R.id.cashFlowCurrentMonthTextView);
        cashFlowExpenseAmountTextView = view.findViewById(R.id.cashFlowExpenseAmountTextView);
        cashFlowExpenseProgressBar = view.findViewById(R.id.cashFlowExpenseProgressBar);
        cashFlowIncomeAmountTextView = view.findViewById(R.id.cashFlowIncomeAmountTextView);
        cashFlowIncomeProgressBar = view.findViewById(R.id.cashFlowIncomeProgressBar);
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        current_month = findMonth(currentMonth);
        currentYear = calendar.get(Calendar.YEAR);
        dateBuilder = new StringBuilder();
        decimalFormat = new DecimalFormat("##.##");
        expenseAmountList = new ArrayList<>();
        expensesAmountTextView = view.findViewById(R.id.expensesAmountTextView);
        expenses_colors = new int[] {
                getResources().getColor(R.color.amber),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.blue_gray),
                getResources().getColor(R.color.brown),
                getResources().getColor(R.color.cyan),
                getResources().getColor(R.color.deep_orange),
                getResources().getColor(R.color.deep_purple),
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.indigo),
                getResources().getColor(R.color.light_blue),
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.lime),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.pink),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.teal),
                getResources().getColor(R.color.yellow)
        };
        expensesCurrentMonthTextView = view.findViewById(R.id.expensesCurrentMonthTextView);
        expenseDateArrayList = new ArrayList<>();
        expenseDateList = new ArrayList<>();
        expensesPieChart = view.findViewById(R.id.expensesPieChart);
        incomeAmountList = new ArrayList<>();
        incomeAmountTextView = view.findViewById(R.id.incomeAmountTextView);
        income_colors = new int[] {
                getResources().getColor(R.color.teal),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.pink),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.lime),
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.light_blue),
                getResources().getColor(R.color.indigo),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.amber),
                getResources().getColor(R.color.deep_purple),
                getResources().getColor(R.color.deep_orange),
                getResources().getColor(R.color.cyan),
                getResources().getColor(R.color.brown),
                getResources().getColor(R.color.blue_gray),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.yellow)
        };
        incomeCurrentMonthTextView = view.findViewById(R.id.incomeCurrentMonthTextView);
        incomeDateArrayList = new ArrayList<>();
        incomeDateList = new ArrayList<>();
        incomePieChart = view.findViewById(R.id.incomePieChart);
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
    }

    public interface CashFlowFragmentListener {

        void showMonthListForCashFlow(String current_month, String current_year);
        void showYearListForCashFlow(String current_month, String current_year);
    }

    public void updateTheViews(final Context context, final String month, final String year) {

        String convertedMonth;
        this.month = month;
        this.year = year;
        monthTitleTextView.setText(month);
        yearTitleTextView.setText(year);

        if (currentYear == Integer.parseInt(year)) {

            if (current_month.equals(month)) {

                changeMonthNameIfMonthOrYearIsNotSame("THIS MONTH");


            } else {

                convertedMonth = convertToFullName(month);
                changeMonthNameIfMonthOrYearIsNotSame(convertedMonth + " " + year);

            }

        } else {

            convertedMonth = convertToFullName(month);
            changeMonthNameIfMonthOrYearIsNotSame(convertedMonth + " " + year);

        }

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

                                if (beiAmount.getBalance().contains("-")) {

                                    char[] bal = beiAmount.getBalance().toCharArray();
                                    StringBuilder builder = new StringBuilder();

                                    for (int i = 1; i < bal.length; i++) {

                                        builder.append(bal[i]);

                                    }

                                    beiAmount.setBalance(putComma(builder.toString()));
                                    balance = "-" + context.getResources()
                                            .getString(R.string.rupees) + " " + beiAmount.getBalance();

                                } else {

                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    balance = context.getResources()
                                            .getString(R.string.rupees) + " " +  beiAmount.getBalance();

                                }

                                final float exp = Float.parseFloat(beiAmount.getExpense());
                                final float inc = Float.parseFloat(beiAmount.getIncome());
                                beiAmount.setExpense(putComma(beiAmount.getExpense()));

                                if (beiAmount.getExpense().equals("0")) {

                                    expense = context.getResources()
                                            .getString(R.string.rupees) + " 0";

                                } else {

                                    expense = "-" + context.getResources()
                                            .getString(R.string.rupees) + " " + beiAmount.getExpense();

                                }

                                beiAmount.setIncome(putComma(beiAmount.getIncome()));
                                income = context.getResources()
                                        .getString(R.string.rupees) + " " + beiAmount.getIncome();
                                cashFlowBalanceAmountTextView.setText(balance);
                                cashFlowExpenseAmountTextView.setText(expense);
                                cashFlowIncomeAmountTextView.setText(income);
                                expense = context
                                        .getResources()
                                        .getString(R.string.rupees) + " " + beiAmount.getExpense();
                                expensesAmountTextView.setText(expense);
                                incomeAmountTextView.setText(income);

                                if (exp > inc) {

                                    expenseProgress = 100;
                                    float income_prog = (inc / exp) * 100;
                                    incomeProgress = (int) income_prog;

                                } else if (exp < inc) {

                                    float expense_prog = (exp / inc) * 100;
                                    expenseProgress = (int) expense_prog;
                                    incomeProgress = 100;

                                } else {

                                    expenseProgress = 0;
                                    incomeProgress = 0;

                                }

                                cashFlowExpenseProgressBar.animateProgress(1000, 0, expenseProgress);
                                cashFlowIncomeProgressBar.animateProgress(1000, 0, incomeProgress);
                                userDBReference.child(userId)
                                        .child("Expense_Category")
                                        .child(year)
                                        .child(month)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {

                                                    if (snapshot.getChildrenCount() == 1) {

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            date = dateSnapshot.getKey();

                                                        }

                                                        if (date != null) {

                                                            date = replaceUnderscoreWithSlash(date);
                                                            date = removeYearFromDateIfYearIsSame(date, year);
                                                            expensesPieChart.animateY(1000, Easing.EaseInOutCubic);
                                                            expensesPieChart.getDescription().setEnabled(false);
                                                            expensesPieChart.setCenterText("All\n" + date + "\n" + expense);
                                                            expensesPieChart.setDrawEntryLabels(false);
                                                            expensesPieChart.setDrawHoleEnabled(true);
                                                            expensesPieChart.setHoleRadius(65);
                                                            expensesPieChart.setRotationEnabled(false);
                                                            expenseDateArrayList.clear();
                                                            expenseDateArrayList.add(new PieEntry(100f, date));
                                                            expensesPieDataSet = new PieDataSet(expenseDateArrayList, "");
                                                            expensesPieDataSet.setColors(expenses_colors);
                                                            expensesPieDataSet.setDrawValues(false);
                                                            expensesPieData = new PieData(expensesPieDataSet);
                                                            expensesPieChart.setData(expensesPieData);
                                                            expensesPieData.setValueTextColor(Color.WHITE);
                                                            expensesPieData.setValueTextSize(10f);

                                                        }

                                                    } else {

                                                        expenseAmountList.clear();
                                                        expenseDateList.clear();

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            expenseDateList.add(dateSnapshot.getKey());

                                                        }

                                                        expense_amount_percentage = new float[expenseDateList.size()];

                                                        for (int i = 0; i < expenseDateList.size(); i++) {

                                                            amountList.clear();
                                                            BigDecimal total_expense_amount = new BigDecimal(0);

                                                            for (DataSnapshot itemSnapshot : snapshot.child(expenseDateList.get(i)).getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    amountList.add(subItemSnapshot.getValue(String.class));

                                                                }

                                                            }

                                                            for (int j = 0; j < amountList.size(); j++) {

                                                                total_expense_amount = total_expense_amount.add(new BigDecimal(amountList.get(j)));

                                                            }

                                                            BigDecimal percentage = total_expense_amount.divide(new BigDecimal(String.valueOf(exp)), 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                                            expenseAmountList.add(context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(total_expense_amount)));
                                                            expense_amount_percentage[i] = Float.parseFloat(percentage.toString());

                                                        }

                                                        for (int i = 0; i < expenseDateList.size(); i++) {

                                                            expenseDateList.set(i, replaceUnderscoreWithSlash(expenseDateList.get(i)));
                                                            expenseDateList.set(i, removeYearFromDateIfYearIsSame(expenseDateList.get(i), year));

                                                        }

                                                        expensesPieChart.animateY(1000, Easing.EaseInOutCubic);
                                                        expensesPieChart.getDescription().setEnabled(false);
                                                        expensesPieChart.getLegend().setWordWrapEnabled(true);
                                                        expensesPieChart.setCenterText("All\n" + month + "\n" + expense);
                                                        expensesPieChart.setDrawEntryLabels(false);
                                                        expensesPieChart.setDrawHoleEnabled(true);
                                                        expensesPieChart.setHoleRadius(65);
                                                        expensesPieChart.setRotationEnabled(false);
                                                        expenseDateArrayList.clear();

                                                        for (int i = 0; i < expenseDateList.size(); i++) {

                                                            expenseDateArrayList.add(new PieEntry(expense_amount_percentage[i], expenseDateList.get(i)));

                                                        }

                                                        expensesPieDataSet = new PieDataSet(expenseDateArrayList, "");
                                                        expensesPieDataSet.setColors(expenses_colors);
                                                        expensesPieDataSet.setDrawValues(false);
                                                        expensesPieData = new PieData(expensesPieDataSet);
                                                        expensesPieChart.setData(expensesPieData);
                                                        expensesPieData.setValueTextColor(Color.WHITE);
                                                        expensesPieData.setValueTextSize(10f);
                                                        expensesPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                                            @Override
                                                            public void onValueSelected(Entry e, Highlight h) {

                                                                int index = (int) h.getX();
                                                                expensesPieChart.setCenterText(expenseDateList.get(index) + "\n" + expenseAmountList.get(index) + "\n" + expense_amount_percentage[index] + "%");
                                                            }

                                                            @Override
                                                            public void onNothingSelected() {

                                                                expensesPieChart.setCenterText("All\n" + month + "\n" + expense);
                                                            }
                                                        });

                                                    }

                                                } else {

                                                    expensesPieChart.setData(null);
                                                    expensesPieChart.invalidate();

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

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            date = dateSnapshot.getKey();

                                                        }

                                                        if (date != null) {

                                                            date = replaceUnderscoreWithSlash(date);
                                                            date = removeYearFromDateIfYearIsSame(date, year);
                                                            incomePieChart.animateY(1000, Easing.EaseInOutCubic);
                                                            incomePieChart.getDescription().setEnabled(false);
                                                            incomePieChart.setCenterText("All\n" + date + "\n" + income);
                                                            incomePieChart.setDrawEntryLabels(false);
                                                            incomePieChart.setDrawHoleEnabled(true);
                                                            incomePieChart.setHoleRadius(65);
                                                            incomePieChart.setRotationEnabled(false);
                                                            incomeDateArrayList.clear();
                                                            incomeDateArrayList.add(new PieEntry(100f, date));
                                                            incomePieDataSet = new PieDataSet(incomeDateArrayList, "");
                                                            incomePieDataSet.setColors(income_colors);
                                                            incomePieDataSet.setDrawValues(false);
                                                            incomePieData = new PieData(incomePieDataSet);
                                                            incomePieChart.setData(incomePieData);
                                                            incomePieData.setValueTextColor(Color.WHITE);
                                                            incomePieData.setValueTextSize(10f);

                                                        }

                                                    } else {

                                                        incomeAmountList.clear();
                                                        incomeDateList.clear();

                                                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                            incomeDateList.add(dateSnapshot.getKey());

                                                        }

                                                        income_amount_percentage = new float[incomeDateList.size()];

                                                        //new code after data schema change
                                                        for (int i = 0; i < incomeDateList.size(); i++) {

                                                            amountList.clear();
                                                            BigDecimal total_income_amount = new BigDecimal(0);

                                                            for (DataSnapshot itemSnapshot : snapshot.child(incomeDateList.get(i)).getChildren()) {


                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    amountList.add(subItemSnapshot.getValue(String.class));

                                                                }

                                                            }

                                                            for (int j = 0; j < amountList.size(); j++) {

                                                                total_income_amount = total_income_amount.add(new BigDecimal(amountList.get(j)));

                                                            }

                                                            BigDecimal percentage = total_income_amount.divide(new BigDecimal(String.valueOf(inc)), 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                                            incomeAmountList.add(context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(total_income_amount)));
                                                            income_amount_percentage[i] = Float.parseFloat(decimalFormat.format(percentage));

                                                        }

                                                        for (int i = 0; i < incomeDateList.size(); i++) {

                                                            incomeDateList.set(i, replaceUnderscoreWithSlash(incomeDateList.get(i)));
                                                            incomeDateList.set(i, removeYearFromDateIfYearIsSame(incomeDateList.get(i), year));

                                                        }

                                                        incomePieChart.animateY(1000, Easing.EaseInOutCubic);
                                                        incomePieChart.getDescription().setEnabled(false);
                                                        incomePieChart.getLegend().setWordWrapEnabled(true);
                                                        incomePieChart.setCenterText("All\n" + month + "\n" + income);
                                                        incomePieChart.setDrawEntryLabels(false);
                                                        incomePieChart.setDrawHoleEnabled(true);
                                                        incomePieChart.setHoleRadius(65);
                                                        incomePieChart.setRotationEnabled(false);
                                                        incomeDateArrayList.clear();

                                                        for (int i = 0; i < incomeDateList.size(); i++) {

                                                            incomeDateArrayList.add(new PieEntry(income_amount_percentage[i], incomeDateList.get(i)));

                                                        }

                                                        incomePieDataSet = new PieDataSet(incomeDateArrayList, "");
                                                        incomePieDataSet.setColors(income_colors);
                                                        incomePieDataSet.setDrawValues(false);
                                                        incomePieData = new PieData(incomePieDataSet);
                                                        incomePieChart.setData(incomePieData);
                                                        incomePieData.setValueTextColor(Color.WHITE);
                                                        incomePieData.setValueTextSize(10f);
                                                        incomePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                                            @Override
                                                            public void onValueSelected(Entry e, Highlight h) {

                                                                int index = (int) h.getX();
                                                                incomePieChart.setCenterText(incomeDateList.get(index) + "\n" + incomeAmountList.get(index) + "\n" + income_amount_percentage[index] + "%");
                                                            }

                                                            @Override
                                                            public void onNothingSelected() {

                                                                incomePieChart.setCenterText("All\n" + month + "\n" + income);
                                                            }
                                                        });

                                                    }

                                                } else {

                                                    incomePieChart.setData(null);
                                                    incomePieChart.invalidate();

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                Log.e("database_error", error.getMessage());
                                            }
                                        });

                            }

                        } else {

                            balance = expense = income = context
                                    .getResources()
                                    .getString(R.string.rupees) + " 0";
                            cashFlowBalanceAmountTextView.setText(balance);
                            cashFlowExpenseProgressBar.setProgress(0);
                            cashFlowExpenseAmountTextView.setText(expense);
                            cashFlowIncomeProgressBar.setProgress(0);
                            cashFlowIncomeAmountTextView.setText(income);
                            expensesAmountTextView.setText(expense);
                            expensesPieChart.setData(null);
                            expensesPieChart.invalidate();
                            incomeAmountTextView.setText(income);
                            incomePieChart.setData(null);
                            incomePieChart.invalidate();

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

        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(view.getContext(), month, year);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            cashFlowFragmentListener = (CashFlowFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement CashFlowFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        int view_id = v.getId();

        if (view_id == R.id.monthListCardView) {

            cashFlowFragmentListener.showMonthListForCashFlow(month, year);

        } else if (view_id == R.id.yearListCardView) {

            cashFlowFragmentListener.showYearListForCashFlow(month, year);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        cashFlowFragmentListener = null;
    }
}
