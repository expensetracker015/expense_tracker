package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.Budgets;
import com.jaekapps.expensetracker.model.PeriodicBudgets;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;
import com.jaekapps.expensetracker.view.adapters.PeriodicBudgetsAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PeriodicBudgetsFragment extends Fragment {

    private DatabaseReference userDBReference;
    private final List<Budgets> budgetsList = new ArrayList<>();
    private final List<PeriodicBudgets> periodicBudgetsList = new ArrayList<>();
    private final String month, year;
    private FloatingActionButton periodicBudgetFab;
    private LinearLayout periodicBudgetsLinearLayout;
    private List<String> amountList, budgetsNameList, itemList;
    private PeriodicBudgets periodicBudgets;
    private PeriodicBudgetsAdapter periodicBudgetsAdapter;
    private PeriodicBudgetsListener periodicBudgetsListener;
    private ProgressBar loadingProgressbar;
    private String userId;

    public PeriodicBudgetsFragment(String month, String year) {

        this.month = month;
        this.year = year;
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

    private void initializeViews(View view) {

        amountList = new ArrayList<>();
        budgetsNameList = new ArrayList<>();
        itemList = new ArrayList<>();
        loadingProgressbar = view.findViewById(R.id.loadingProgressbar);
        periodicBudgets = new PeriodicBudgets();
        periodicBudgetsAdapter = new PeriodicBudgetsAdapter(getContext(), periodicBudgetsList);
        periodicBudgetFab = view.findViewById(R.id.periodicBudgetFab);
        periodicBudgetsLinearLayout = view.findViewById(R.id.periodicBudgetsLinearLayout);
        RecyclerView periodicBudgetsRecyclerView = view.findViewById(R.id.periodicBudgetsRecyclerView);
        periodicBudgetsRecyclerView.setHasFixedSize(true);
        periodicBudgetsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        periodicBudgetsRecyclerView.setAdapter(periodicBudgetsAdapter);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
    }

    public interface PeriodicBudgetsListener {

        void goToBudgetsActivityFromPBF(int position);
    }

    public void updateTheViews(final String month, final String year) {

        userDBReference.child(userId)
                .child("Budget")
                .child("Periodic")
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            budgetsList.clear();
                            budgetsNameList.clear();
                            loadingProgressbar.setVisibility(View.GONE);
                            periodicBudgetsLinearLayout.setVisibility(View.GONE);
                            periodicBudgetsList.clear();

                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                                Budgets budgets = snapshot.child(itemSnapshot.getKey()).getValue(Budgets.class);
                                budgetsList.add(budgets);
                                budgetsNameList.add(itemSnapshot.getKey());

                            }

                            userDBReference.child(userId)
                                    .child("Expense_Category")
                                    .child(year)
                                    .child(month)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()) {

                                                List<String> dateList = new ArrayList<>();

                                                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                                    dateList.add(dateSnapshot.getKey());

                                                }

                                                for (int i = 0; i < budgetsList.size(); i++) {

                                                    final Budgets budgets = budgetsList.get(i);

                                                    if (budgets.getPeriod().equals("This Week")) {

                                                        amountList.clear();
                                                        BigDecimal budget = new BigDecimal(budgets.getAmount());
                                                        BigDecimal expense;
                                                        boolean found = false;
                                                        int position = 0;
                                                        itemList.clear();
                                                        String budgetAmount = getResources().getString(R.string.rupees) + putComma(budgets.getAmount());
                                                        String totalExpenseAmount;
                                                        String period = budgets.getPeriod();
                                                        String startDate = replaceSlashWithUnderscore(budgets.getStartDate());

                                                        for (int j = 0; j < dateList.size(); j++) {

                                                            if (dateList.get(j).equals(startDate)) {

                                                                found = true;
                                                                position = j;
                                                                break;

                                                            }

                                                        }

                                                        if (found) {

                                                            int size = 0;

                                                            for (int j = position; j < dateList.size(); j++) {

                                                                size++;

                                                            }

                                                            if (size > 7) {

                                                                int counter = 0;

                                                                for (int j = position; j < dateList.size(); j++) {

                                                                    while (counter < 7) {

                                                                        for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(j)).getChildren()) {

                                                                            itemList.add(itemSnapshot.getKey());

                                                                        }

                                                                        for (int k = 0; k < itemList.size(); k++) {

                                                                            for (DataSnapshot subItemSnapshot : snapshot.child(dateList.get(j)).child(itemList.get(k)).getChildren()) {

                                                                                amountList.add(subItemSnapshot.getValue(String.class));

                                                                            }

                                                                        }

                                                                        counter++;

                                                                    }

                                                                }

                                                                BigDecimal total_amount = new BigDecimal(0);

                                                                for (int j = 0; j < amountList.size(); j++) {

                                                                    total_amount = total_amount.add(new BigDecimal(amountList.get(j)));

                                                                }

                                                                expense = total_amount;
                                                                totalExpenseAmount = getResources().getString(R.string.rupees) + putComma(total_amount.toString());

                                                            } else {

                                                                for (int j = position; j < dateList.size(); j++) {

                                                                    for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(j)).getChildren()) {

                                                                        itemList.add(itemSnapshot.getKey());

                                                                    }

                                                                    for (int k = 0; k < itemList.size(); k++) {

                                                                        for (DataSnapshot subItemSnapshot : snapshot.child(dateList.get(j)).child(itemList.get(k)).getChildren()) {

                                                                            amountList.add(subItemSnapshot.getValue(String.class));

                                                                        }

                                                                    }

                                                                }

                                                                BigDecimal total_amount = new BigDecimal(0);

                                                                for (int j = 0; j < amountList.size(); j++) {

                                                                    total_amount = total_amount.add(new BigDecimal(amountList.get(j)));

                                                                }

                                                                expense = total_amount;
                                                                totalExpenseAmount = getResources().getString(R.string.rupees) + putComma(total_amount.toString());

                                                            }

                                                            BigDecimal percentage;
                                                            String status;
                                                            percentage = expense.divide(budget, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));

                                                            if (budget.compareTo(expense) > 0) {

                                                                status = "Your budget limit is under control.";

                                                            } else {

                                                                status = "You have exceeded your budget limit!";

                                                            }

                                                            float percentageInFloat = Float.parseFloat(new DecimalFormat("##.##").format(percentage));
                                                            periodicBudgets = new PeriodicBudgets();
                                                            periodicBudgets.setBudgetAmount(budgetAmount);
                                                            periodicBudgets.setBudgetName(budgetsNameList.get(i));
                                                            periodicBudgets.setExpenseAmount(totalExpenseAmount);
                                                            periodicBudgets.setPercentage((int) percentageInFloat);
                                                            periodicBudgets.setPeriod(period);
                                                            periodicBudgets.setStatus(status);

                                                        }

                                                    } else if (budgets.getPeriod().equals("This Month")) {

                                                        amountList.clear();
                                                        BigDecimal budget = new BigDecimal(budgets.getAmount());
                                                        BigDecimal expense;
                                                        String budgetAmount = getResources().getString(R.string.rupees) + putComma(budgets.getAmount());
                                                        String totalExpenseAmount;
                                                        String period = budgets.getPeriod();

                                                        for (int j = 0; j < dateList.size(); j++) {

                                                            for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(j)).getChildren()) {

                                                                for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                                                    amountList.add(subItemSnapshot.getValue(String.class));

                                                                }

                                                            }

                                                        }

                                                        BigDecimal total_amount = new BigDecimal(0);

                                                        for (int j = 0; j < amountList.size(); j++) {

                                                            total_amount = total_amount.add(new BigDecimal(amountList.get(j)));

                                                        }

                                                        expense = total_amount;
                                                        totalExpenseAmount = getResources().getString(R.string.rupees) + putComma(total_amount.toString());
                                                        BigDecimal percentage;
                                                        String status;
                                                        percentage = expense.divide(budget, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));

                                                        if (budget.compareTo(expense) > 0) {

                                                            status = "Your budget limit is under control.";

                                                        } else {

                                                            status = "You have exceeded your budget limit!";

                                                        }

                                                        float percentageInFloat = Float.parseFloat(new DecimalFormat("##.##").format(percentage));
                                                        periodicBudgets = new PeriodicBudgets();
                                                        periodicBudgets.setBudgetAmount(budgetAmount);
                                                        periodicBudgets.setBudgetName(budgetsNameList.get(i));
                                                        periodicBudgets.setExpenseAmount(totalExpenseAmount);
                                                        periodicBudgets.setPercentage((int) percentageInFloat);
                                                        periodicBudgets.setPeriod(period);
                                                        periodicBudgets.setStatus(status);

                                                    }

                                                    periodicBudgetsList.add(periodicBudgets);

                                                }

                                                periodicBudgetsAdapter.updateTheAdapter(periodicBudgetsList);

                                            } else {

                                                loadingProgressbar.setVisibility(View.GONE);
                                                periodicBudgetsLinearLayout.setVisibility(View.VISIBLE);

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                            Log.e("database_error", error.getMessage());
                                        }
                                    });

                        } else {

                            loadingProgressbar.setVisibility(View.GONE);
                            periodicBudgetsLinearLayout.setVisibility(View.VISIBLE);

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

        View view = inflater.inflate(R.layout.fragment_periodic_budgets, container, false);
        initializeViews(view);
        updateTheViews(month, year);
        periodicBudgetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                periodicBudgetsListener.goToBudgetsActivityFromPBF(0);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            periodicBudgetsListener = (PeriodicBudgetsListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement PeriodicBudgetsListener!");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        periodicBudgetsListener = null;
    }
}
