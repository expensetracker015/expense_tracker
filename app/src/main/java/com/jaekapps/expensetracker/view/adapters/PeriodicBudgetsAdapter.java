package com.jaekapps.expensetracker.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.PeriodicBudgets;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.List;

public class PeriodicBudgetsAdapter extends RecyclerView.Adapter<PeriodicBudgetsAdapter.PeriodicBudgetsHolder> {

    private final Context context;
    private List<PeriodicBudgets> periodicBudgetsList;

    public PeriodicBudgetsAdapter(Context context, List<PeriodicBudgets> periodicBudgetsList) {

        this.context = context;
        this.periodicBudgetsList = periodicBudgetsList;
    }

    public void updateTheAdapter(List<PeriodicBudgets> periodicBudgetsList) {

        this.periodicBudgetsList = periodicBudgetsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeriodicBudgetsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_budgets, parent, false);
        return new PeriodicBudgetsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodicBudgetsHolder holder, int position) {

        PeriodicBudgets periodicBudgets = periodicBudgetsList.get(position);
        holder.balanceProgressBar.setProgress((periodicBudgets.getPercentage()));
        holder.budgetNameTextView.setText(periodicBudgets.getBudgetName());
        holder.budgetStatusTextView.setText(periodicBudgets.getStatus());
        String expenseAmount = "Expenses " + periodicBudgets.getExpenseAmount() + " / " + periodicBudgets.getBudgetAmount();
        holder.expenseTextView.setText(expenseAmount);
        holder.periodTitleTextView.setText(periodicBudgets.getPeriod());

        if (periodicBudgets.getStatus().equals("Your budget limit is under control.")) {

            holder.balanceProgressBar.setProgressColors(context.getResources().getColor(R.color.gray_200), context.getResources().getColor(R.color.green));
            holder.budgetStatusTextView.setTextColor(context.getResources().getColor(R.color.green));

        } else {

            holder.balanceProgressBar.setProgressColors(context.getResources().getColor(R.color.gray_200), context.getResources().getColor(R.color.red));
            holder.budgetStatusTextView.setTextColor(context.getResources().getColor(R.color.red));

        }

    }

    @Override
    public int getItemCount() {

        return periodicBudgetsList.size();
    }

    public static class PeriodicBudgetsHolder extends RecyclerView.ViewHolder {

        private final RoundedHorizontalProgressBar balanceProgressBar;
        private final TextView budgetNameTextView, budgetStatusTextView, expenseTextView, periodTitleTextView;

        public PeriodicBudgetsHolder(@NonNull View itemView) {
            super(itemView);

            balanceProgressBar = itemView.findViewById(R.id.balanceProgressBar);
            budgetNameTextView = itemView.findViewById(R.id.budgetNameTextView);
            budgetStatusTextView = itemView.findViewById(R.id.budgetStatusTextView);
            expenseTextView = itemView.findViewById(R.id.expenseTextView);
            periodTitleTextView = itemView.findViewById(R.id.periodTitleTextView);
        }
    }
}
