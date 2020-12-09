package com.jaekapps.expensetracker;

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

public class ExpenseIncomeItemOfEachDateAdapter extends RecyclerView.Adapter<ExpenseIncomeItemOfEachDateAdapter.ExpenseIncomeItemOfEachDateVH> {

    private final ArrayList<Integer> itemIconList;
    private final ArrayList<String> amountList, itemList;
    private final Context context;
    private ExpenseItemClickListener expenseItemClickListener;
    private final int[] colors;
    private final String category;

    ExpenseIncomeItemOfEachDateAdapter(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList, Context context, int[] colors, String category) {

        this.amountList = amountList;
        this.category = category;
        this.colors = colors;
        this.context = context;
        this.itemIconList = itemIconList;
        this.itemList = itemList;
    }

    public interface ExpenseItemClickListener {

        void onExpenseItemClick(int color, int icon, String amount, String name);
    }

    public void setExpenseItemClickListener(ExpenseItemClickListener expenseItemClickListener) {

        this.expenseItemClickListener = expenseItemClickListener;
    }

    @NonNull
    @Override
    public ExpenseIncomeItemOfEachDateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_income_item_of_each_date_layout, parent, false);
        return new ExpenseIncomeItemOfEachDateVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseIncomeItemOfEachDateVH holder, int position) {

        holder.itemAmountTextView.setText(amountList.get(position));
        holder.itemIconCardView.setCardBackgroundColor(colors[position]);
        holder.itemIconImageView.setImageResource(itemIconList.get(position));
        holder.itemNameTextView.setText(itemList.get(position));
        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {

                    expenseItemClickListener.onExpenseItemClick(
                            colors[holder.getAdapterPosition()],
                            itemIconList.get(holder.getAdapterPosition()),
                            amountList.get(holder.getAdapterPosition()),
                            itemList.get(holder.getAdapterPosition())
                    );

                }

            }
        });

        if (category.equals("Expense_Category")) {

            holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.red, context.getTheme()));

        } else if (category.equals("Income_Category")) {

            holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));

        }

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }


    public static class ExpenseIncomeItemOfEachDateVH extends RecyclerView.ViewHolder {

        private final AppCompatImageView itemIconImageView;
        private final CardView itemCardView, itemIconCardView;
        private final TextView itemAmountTextView, itemNameTextView;

        public ExpenseIncomeItemOfEachDateVH(@NonNull View itemView) {
            super(itemView);

            itemAmountTextView = itemView.findViewById(R.id.itemAmountTextView);
            itemCardView = itemView.findViewById(R.id.itemCardView);
            itemIconCardView = itemView.findViewById(R.id.itemIconCardView);
            itemIconImageView = itemView.findViewById(R.id.itemIconImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }
}
