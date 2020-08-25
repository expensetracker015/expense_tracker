package com.jaekapps.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.Calendar;

public class CashFlowFragment extends Fragment implements View.OnClickListener {

    int currentMonth, expenseProgress, incomeProgress;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private CashFlowFragmentListener cashFlowFragmentListener;
    private DatabaseReference userDBReference;
    private int currentYear;
    private RoundedHorizontalProgressBar cashFlowExpenseProgressBar, cashFlowIncomeProgressBar;
    private String balance, current_month, expense, income, month, year, userId;
    private TextView cashFlowBalanceAmountTextView, cashFlowCurrentMonthTextView, cashFlowExpenseAmountTextView, cashFlowIncomeAmountTextView;
    TextView monthTitleTextView, yearTitleTextView;

    CashFlowFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
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

    private void initializeViews(View view) {

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
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        monthTitleTextView.setText(month);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(view.getContext());
        userId = userIdConfigActivity.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
        yearTitleTextView.setText(year);
    }

    public interface CashFlowFragmentListener {

        void showMonthListForCashFlow(String current_month, String current_year);
        void showYearListForCashFlow(String current_month, String current_year);
    }

    @SuppressLint("SetTextI18n")
    public void updateTheViews(final Context context, String month, String year) {

        this.month = month;
        this.year = year;
        monthTitleTextView.setText(month);
        yearTitleTextView.setText(year);

        if (currentYear == Integer.parseInt(year)) {

            if (current_month.equals(month)) {

                cashFlowCurrentMonthTextView.setText("THIS MONTH");

            } else {

                month = convertToFullName(month);
                cashFlowCurrentMonthTextView.setText(month + " " + year);

            }

        } else {

            cashFlowCurrentMonthTextView.setText(year);

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

                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    beiAmount.setBalance(builder.toString());
                                    balance = "-" + context
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();

                                } else {

                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    balance = context
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();

                                }

                                float exp = Float.parseFloat(beiAmount.getExpense());
                                float inc = Float.parseFloat(beiAmount.getIncome());
                                beiAmount.setExpense(putComma(beiAmount.getExpense()));
                                expense = "-" + context
                                        .getResources()
                                        .getString(R.string.rupees) + beiAmount.getExpense();
                                beiAmount.setIncome(putComma(beiAmount.getIncome()));
                                income = context
                                        .getResources()
                                        .getString(R.string.rupees) + beiAmount.getIncome();
                                cashFlowBalanceAmountTextView.setText(balance);
                                cashFlowExpenseAmountTextView.setText(expense);
                                cashFlowIncomeAmountTextView.setText(income);

                                if (exp > inc) {

                                    expenseProgress = 100;
                                    float income_prog = (inc / exp) * 100;
                                    incomeProgress = (int) income_prog;

                                } else {

                                    float expense_prog = (exp / inc) * 100;
                                    expenseProgress = (int) expense_prog;
                                    incomeProgress = 100;

                                }

                                cashFlowExpenseProgressBar.animateProgress(1000, 0, expenseProgress);
                                cashFlowIncomeProgressBar.animateProgress(1000, 0, incomeProgress);

                            }

                        } else {

                            balance = expense = income = context
                                    .getResources()
                                    .getString(R.string.rupees) + "0";
                            cashFlowBalanceAmountTextView.setText(balance);
                            cashFlowExpenseProgressBar.setProgress(0);
                            cashFlowExpenseAmountTextView.setText(expense);
                            cashFlowIncomeProgressBar.setProgress(0);
                            cashFlowIncomeAmountTextView.setText(income);

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

        final View view = inflater.inflate(R.layout.cash_flow_fragment, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(view.getContext(), month, year);

        /*
        if (currentYear == Integer.parseInt(year)) {

            if (current_month.equals(month)) {

                cashFlowCurrentMonthTextView.setText("THIS MONTH");

            } else {

                month = convertToFullName(month);
                cashFlowCurrentMonthTextView.setText(month + " " + year);

            }

        } else {

            cashFlowCurrentMonthTextView.setText(year);

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

                                    beiAmount.setBalance(builder.toString());
                                    balance = "-" + view.getContext()
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();

                                } else {

                                    balance = view.getContext()
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();

                                }

                                expense = view.getContext()
                                        .getResources()
                                        .getString(R.string.rupees) + beiAmount.getExpense();
                                income = view.getContext()
                                        .getResources()
                                        .getString(R.string.rupees) + beiAmount.getIncome();
                                cashFlowBalanceAmountTextView.setText(balance);
                                cashFlowExpenseAmountTextView.setText(expense);
                                cashFlowIncomeAmountTextView.setText(income);
                                float expense = Float.parseFloat(beiAmount.getExpense());
                                float income = Float.parseFloat(beiAmount.getIncome());

                                if (expense > income) {

                                    expenseProgress = 100;
                                    float income_prog = (income / expense) * 100;
                                    incomeProgress = (int) income_prog;

                                } else {

                                    float expense_prog = (expense / income) * 100;
                                    expenseProgress = (int) expense_prog;
                                    incomeProgress = 100;

                                }

                                cashFlowExpenseProgressBar.animateProgress(1000, 0, expenseProgress);
                                cashFlowIncomeProgressBar.animateProgress(1000, 0, incomeProgress);

                            }

                        } else {

                            balance = expense = income = view.getContext()
                                    .getResources()
                                    .getString(R.string.rupees) + "0";
                            cashFlowBalanceAmountTextView.setText(balance);
                            cashFlowExpenseAmountTextView.setText(expense);
                            cashFlowIncomeAmountTextView.setText(income);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
         */
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

        switch (v.getId()) {

            case R.id.monthListCardView:
                cashFlowFragmentListener.showMonthListForCashFlow(month, year);
                break;

            case R.id.yearListCardView:
                cashFlowFragmentListener.showYearListForCashFlow(month, year);
                break;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        cashFlowFragmentListener = null;
    }
}
