package com.jaekapps.expensetracker.view.adapters;

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

import com.jaekapps.expensetracker.R;

import java.util.ArrayList;

public class Top5ExpensesRecyclerAdapter extends RecyclerView.Adapter<Top5ExpensesRecyclerAdapter.Top5ExpensesViewHolder> {

    private final ArrayList<Integer> itemIconList;
    private final ArrayList<String> amountList;
    private final ArrayList<String> itemList;
    private final Context context;
    private final int[] colors;

    public Top5ExpensesRecyclerAdapter(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList, Context context, int[] colors) {

        this.amountList = amountList;
        this.colors = colors;
        this.context = context;
        this.itemIconList = itemIconList;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Top5ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_top_5_expenses_income, parent, false);
        return new Top5ExpensesViewHolder(view);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull Top5ExpensesViewHolder holder, int position) {

        holder.expenseTypeTextView.setText("Cash");
        holder.itemAmountTextView.setText(amountList.get(position));
        holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.red));
        holder.itemIconCardView.setCardBackgroundColor(colors[position]);
        holder.itemIconImageView.setImageResource(itemIconList.get(position));
        holder.itemNameTextView.setText(itemList.get(position));
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public static class Top5ExpensesViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView itemIconImageView;
        private final CardView itemIconCardView;
        private final TextView expenseTypeTextView, itemAmountTextView, itemNameTextView;

        public Top5ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTypeTextView = itemView.findViewById(R.id.expenseIncomeTypeTextView);
            itemAmountTextView = itemView.findViewById(R.id.itemAmountTextView);
            itemIconCardView = itemView.findViewById(R.id.itemIconCardView);
            itemIconImageView = itemView.findViewById(R.id.itemIconImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }
}
