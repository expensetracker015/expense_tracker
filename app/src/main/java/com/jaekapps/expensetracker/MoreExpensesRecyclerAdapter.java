package com.jaekapps.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MoreExpensesRecyclerAdapter extends RecyclerView.Adapter<MoreExpensesRecyclerAdapter.ExpensesViewHolder> {

    private ArrayList<Integer> itemIconList;
    private ArrayList<String> amountList;
    private ArrayList<String> itemList;
    private Context context;
    private int[] colors;

    MoreExpensesRecyclerAdapter(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList, Context context, int[] colors) {

        this.amountList = amountList;
        this.colors = colors;
        this.context = context;
        this.itemIconList = itemIconList;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_5_expenses_income_layout, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {

        holder.expenseTypeTextView.setText("Cash");
        holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.red, context.getTheme()));
        holder.itemAmountTextView.setText(amountList.get(position));
        holder.itemIconCardView.setCardBackgroundColor(colors[position]);
        holder.itemIconImageView.setImageResource(itemIconList.get(position));
        holder.itemNameTextView.setText(itemList.get(position));
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView itemIconImageView;
        private CardView itemIconCardView;
        private TextView expenseTypeTextView, itemAmountTextView, itemNameTextView;

        public ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTypeTextView = itemView.findViewById(R.id.expenseIncomeTypeTextView);
            itemAmountTextView = itemView.findViewById(R.id.itemAmountTextView);
            itemIconCardView = itemView.findViewById(R.id.itemIconCardView);
            itemIconImageView = itemView.findViewById(R.id.itemIconImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }
}
