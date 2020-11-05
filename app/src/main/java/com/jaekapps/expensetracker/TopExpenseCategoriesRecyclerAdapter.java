package com.jaekapps.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.ArrayList;

public class TopExpenseCategoriesRecyclerAdapter extends RecyclerView.Adapter<TopExpenseCategoriesRecyclerAdapter.TopExpenseCategoriesViewHolder> {

    ArrayList<Integer> topCategoriesItemPercentageList;
    ArrayList<String> amountList, itemList;
    Context context;

    TopExpenseCategoriesRecyclerAdapter(ArrayList<Integer> topCategoriesItemPercentageList, ArrayList<String> amountList, ArrayList<String> itemList, Context context) {

        this.amountList = amountList;
        this.context = context;
        this.itemList = itemList;
        this.topCategoriesItemPercentageList = topCategoriesItemPercentageList;
    }

    @NonNull
    @Override
    public TopExpenseCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_categories_layout, parent, false);
        return new TopExpenseCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopExpenseCategoriesViewHolder holder, int position) {

        holder.expenseProgressBar.setProgress(topCategoriesItemPercentageList.get(position));
        holder.topCategoryItemAmount.setText(amountList.get(position));
        holder.topCategoryItemName.setText(itemList.get(position));
    }

    @Override
    public int getItemCount() {

        return amountList.size();
    }

    public static class TopExpenseCategoriesViewHolder extends RecyclerView.ViewHolder {

        private RoundedHorizontalProgressBar expenseProgressBar;
        private TextView topCategoryItemAmount, topCategoryItemName;

        public TopExpenseCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseProgressBar = itemView.findViewById(R.id.expenseProgressBar);
            topCategoryItemAmount = itemView.findViewById(R.id.topCategoryItemAmount);
            topCategoryItemName = itemView.findViewById(R.id.topCategoryItemName);
        }
    }
}
