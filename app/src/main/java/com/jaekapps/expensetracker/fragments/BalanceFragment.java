package com.jaekapps.expensetracker.fragments;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BalanceFragment extends Fragment implements View.OnClickListener {

    private ArrayList<PieEntry> monthArrayList;
    private ArrayList<String> monthList;
    private BalanceFragmentListener balanceFragmentListener;
    private BEIAmount beiAmount;
    private BigDecimal total_balance_amount;
    private BigDecimal[] balance_amount;
    private CardView monthListCardView, yearListCardView;
    private DatabaseReference userDBReference;
    private float[] balance_amount_percentage;
    private int[] balance_colors;
    private PieChart balancePieChart;
    private PieData balancePieData;
    private PieDataSet balancePieDataSet;
    private RoundedHorizontalProgressBar balanceByCurrenciesProgressBar, balanceProgressBar;
    private String balance, month, year, userId;
    private TextView balAmountTextView, balanceAmountTextView, balanceByCurrencyTextView, balanceProgressTextView, balanceStatusTextView;
    TextView monthTitleTextView, yearTitleTextView;

    BalanceFragment(String month, String year) {

        this.month = month;
        this.year = year;
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

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        balAmountTextView = view.findViewById(R.id.balAmountTextView);
        balanceAmountTextView = view.findViewById(R.id.balanceAmountTextView);
        balanceByCurrenciesProgressBar = view.findViewById(R.id.balanceByCurrenciesProgressBar);
        balanceByCurrencyTextView = view.findViewById(R.id.balanceByCurrencyTextView);
        balance_colors = new int[] {
                getResources().getColor(R.color.amber, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.blue, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.blue_gray, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.brown, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.cyan, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.deep_orange, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.deep_purple, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.green, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.indigo, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.light_blue, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.light_green, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.lime, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.orange, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.pink, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.purple, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.red, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.teal, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.yellow, Objects.requireNonNull(getActivity()).getTheme())
        };
        balancePieChart = view.findViewById(R.id.balancePieChart);
        balanceProgressBar = view.findViewById(R.id.balanceProgressBar);
        balanceProgressTextView = view.findViewById(R.id.balanceProgressTextView);
        balanceStatusTextView = view.findViewById(R.id.balanceStatusTextView);
        balanceStatusTextView.setVisibility(View.GONE);
        beiAmount = new BEIAmount();
        monthArrayList = new ArrayList<>();
        monthList = new ArrayList<>();
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
    }

    public interface BalanceFragmentListener {

        void showMonthListForBalance(String current_month, String current_year);
        void showYearListForBalance(String current_month, String current_year);
    }

    public void updateTheViews(final Context context, String month, final String year) {

        this.month = month;
        this.year = year;
        monthTitleTextView.setText(month);
        yearTitleTextView.setText(year);
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
                                    balanceStatusTextView.setVisibility(View.VISIBLE);
                                    String status = "NEGATIVE BALANCE";
                                    balanceStatusTextView.setText(status);

                                } else {

                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    balance = context.getResources()
                                            .getString(R.string.rupees) + " " + beiAmount.getBalance();
                                    balanceStatusTextView.setVisibility(View.GONE);

                                }

                                balanceAmountTextView.setText(balance);
                                balanceByCurrencyTextView.setText(balance);
                                balanceProgressTextView.setText(balance);

                                if (beiAmount.getBalance().equals("0")) {

                                    balanceByCurrenciesProgressBar.setProgress(0);
                                    balanceProgressBar.setProgress(0);

                                } else {

                                    balanceByCurrenciesProgressBar.animateProgress(1000, 0, 100);
                                    balanceProgressBar.animateProgress(1000, 0, 100);

                                }

                            }

                        } else {

                            balance = context.getResources()
                                    .getString(R.string.rupees) + " 0";
                            balanceAmountTextView.setText(balance);
                            balanceByCurrenciesProgressBar.setProgress(0);
                            balanceByCurrencyTextView.setText(balance);
                            balanceProgressBar.setProgress(0);
                            balanceProgressTextView.setText(balance);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            if (snapshot.getChildrenCount() == 1) {

                                String month_name = "";

                                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {

                                    month_name = monthSnapshot.getKey();

                                }

                                if (month_name != null) {

                                    beiAmount = snapshot.child(month_name).getValue(BEIAmount.class);

                                    if (beiAmount != null) {

                                        if (beiAmount.getBalance().equals("0")) {

                                            balance = context.getResources()
                                                    .getString(R.string.rupees) + " " + beiAmount.getBalance();
                                            balAmountTextView.setText(balance);
                                            balancePieChart.setData(null);
                                            balancePieChart.invalidate();

                                        } else {

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
                                                        .getString(R.string.rupees) + " " + beiAmount.getBalance();

                                            }

                                            balAmountTextView.setText(balance);
                                            balancePieChart.animateY(1000, Easing.EaseInOutCubic);
                                            balancePieChart.getDescription().setEnabled(false);
                                            balancePieChart.setCenterText("All\n" + month_name + "\n" + balance);
                                            balancePieChart.setDrawEntryLabels(false);
                                            balancePieChart.setDrawHoleEnabled(true);
                                            balancePieChart.setHoleRadius(65);
                                            monthArrayList.clear();
                                            monthArrayList.add(new PieEntry(100f, month_name));
                                            balancePieDataSet = new PieDataSet(monthArrayList, "");
                                            balancePieDataSet.setColors(balance_colors);
                                            balancePieDataSet.setDrawValues(false);
                                            balancePieData = new PieData(balancePieDataSet);
                                            balancePieChart.setData(balancePieData);

                                        }

                                    }

                                }

                            } else {

                                monthList.clear();
                                monthArrayList.clear();

                                for (DataSnapshot monthSnapshot : snapshot.getChildren()) {

                                    monthList.add(monthSnapshot.getKey());

                                }

                                List<String> arrangedMonthList = sortTheMonths(monthList);
                                balance_amount = new BigDecimal[arrangedMonthList.size()];

                                for (int i = 0; i < arrangedMonthList.size(); i++) {

                                    beiAmount = snapshot.child(arrangedMonthList.get(i)).getValue(BEIAmount.class);

                                    if (beiAmount != null) {

                                        if (beiAmount.getBalance().contains("-")) {

                                            char[] balance_array = beiAmount.getBalance().toCharArray();
                                            StringBuilder balanceBuilder = new StringBuilder();

                                            for (int j = 1; j < balance_array.length; j++) {

                                                balanceBuilder.append(balance_array[j]);

                                            }

                                            balance_amount[i] = new BigDecimal(balanceBuilder.toString());

                                        } else {

                                            balance_amount[i] = new BigDecimal(beiAmount.getBalance());

                                        }

                                    }

                                }

                                total_balance_amount = new BigDecimal(0);

                                for (BigDecimal balance : balance_amount) {

                                    total_balance_amount = total_balance_amount.add(balance);

                                }

                                balance_amount_percentage = new float[balance_amount.length];
                                balance = String.valueOf(total_balance_amount);
                                balance = context.getResources()
                                        .getString(R.string.rupees) + " " + putComma(balance);
                                balAmountTextView.setText(balance);
                                balancePieChart.animateY(1000, Easing.EaseInOutCubic);
                                balancePieChart.getDescription().setEnabled(false);
                                balancePieChart.getLegend().setWordWrapEnabled(true);
                                balancePieChart.setCenterText("All\n" + year + "\n" + balance);
                                balancePieChart.setDrawEntryLabels(false);
                                balancePieChart.setDrawHoleEnabled(true);
                                balancePieChart.setHoleRadius(65);
                                balancePieChart.setRotationEnabled(false);

                                for (int i = 0; i < balance_amount.length; i++) {

                                    BigDecimal percentage = (balance_amount[i].divide(total_balance_amount, 4, RoundingMode.HALF_EVEN)).multiply(new BigDecimal(100));
                                    balance_amount_percentage[i] = Float.parseFloat(percentage.toString());
                                    monthArrayList.add(new PieEntry(balance_amount_percentage[i], arrangedMonthList.get(i)));

                                }

                                balancePieDataSet = new PieDataSet(monthArrayList, "");
                                balancePieDataSet.setColors(balance_colors);
                                balancePieDataSet.setDrawValues(false);
                                balancePieData = new PieData(balancePieDataSet);
                                balancePieChart.setData(balancePieData);
                                balancePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {

                                        int index = (int) h.getX();
                                        String bal = String.valueOf(balance_amount[index]);
                                        bal = context.getResources()
                                                .getString(R.string.rupees) + " " + putComma(bal);
                                        balancePieChart.setCenterText(monthList.get(index) + "\n" + bal + "\n" + balance_amount_percentage[index] + "%");
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                        balancePieChart.setCenterText("All\n" + year + "\n" + balance);
                                    }
                                });

                            }

                        } else {

                            balance = context.getResources()
                                    .getString(R.string.rupees) + " 0";
                            balAmountTextView.setText(balance);
                            balancePieChart.setData(null);
                            balancePieChart.invalidate();

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

        final View view = inflater.inflate(R.layout.fragment_balance, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(
                view.getContext(),
                month,
                year
        );
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            balanceFragmentListener = (BalanceFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement BalanceFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        int view_id = v.getId();

        if (view_id == R.id.monthListCardView) {

            balanceFragmentListener.showMonthListForBalance(month, year);

        } else if (view_id == R.id.yearListCardView) {

            balanceFragmentListener.showYearListForBalance(month, year);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        balanceFragmentListener = null;
    }
}
