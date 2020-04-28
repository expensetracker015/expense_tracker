package com.jaekapps.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseItemRecyclerAdapter extends RecyclerView.Adapter<ExpenseItemRecyclerAdapter.ExpenseItemViewHolder> {

    private List<Integer> expenseCategoryLisItemIconId;
    private List<String> expenseCategoryListItemName;
    private OnItemClickListener onItemClickListener;

    ExpenseItemRecyclerAdapter(List<Integer> expenseCategoryLisItemIconId, List<String> expenseCategoryListItemName) {

        this.expenseCategoryLisItemIconId = expenseCategoryLisItemIconId;
        this.expenseCategoryListItemName = expenseCategoryListItemName;

    }

    public interface OnItemClickListener {

        void onItemClick(int position, List<String> expenseCategoryListItemName);

    }

    void setOnClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_layout, parent, false);
        return new ExpenseItemViewHolder(view, onItemClickListener, expenseCategoryListItemName);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemViewHolder holder, int position) {

        holder.expenseItemImageView.setImageResource(expenseCategoryLisItemIconId.get(position));
        holder.expenseItemNameTextView.setText(expenseCategoryListItemName.get(position));

    }

    @Override
    public int getItemCount() {

        return expenseCategoryLisItemIconId.size();

    }

    static class ExpenseItemViewHolder extends RecyclerView.ViewHolder {

        ImageView expenseItemImageView;
        RelativeLayout expenseItemRelativeLayout;
        TextView expenseItemNameTextView;

        ExpenseItemViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener, final List<String> expenseCategoryListItemName) {
            super(itemView);

            expenseItemImageView = itemView.findViewById(R.id.expenseItemImageView);
            expenseItemNameTextView = itemView.findViewById(R.id.expenseItemNameTextView);
            expenseItemRelativeLayout = itemView.findViewById(R.id.expenseItemRelativeLayout);
            expenseItemRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onItemClickListener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            onItemClickListener.onItemClick(position, expenseCategoryListItemName);

                        }

                    }

                }
            });

        }
    }

}
