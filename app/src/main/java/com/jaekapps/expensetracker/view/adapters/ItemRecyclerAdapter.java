package com.jaekapps.expensetracker.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.SubItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    private final ArrayList<Integer> itemIconList;
    private final ArrayList<String> amountList, itemList;
    private final Context context;
    private final int[] colors;
    private final String category, date;
    private final HashMap<String, SubItem[]> subItemHashMap;
    private ItemClickListener itemClickListener;

    public ItemRecyclerAdapter(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList, Context context, int[] colors, String category, String date, HashMap<String, SubItem[]> subItemHashMap) {

        this.amountList = amountList;
        this.category = category;
        this.colors = colors;
        this.context = context;
        this.date = date;
        this.itemIconList = itemIconList;
        this.itemList = itemList;
        this.subItemHashMap = subItemHashMap;
    }

    public interface ItemClickListener {

        void onItemClick(int color, int icon, String amount, String memo, String name);
    }

    public void setExpenseItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {

        holder.itemAmountTextView.setText(amountList.get(position));
        holder.dateTextView.setText(date);
        holder.itemIconCardView.setCardBackgroundColor(colors[position]);
        holder.itemIconImageView.setImageResource(itemIconList.get(position));
        holder.itemNameTextView.setText(itemList.get(position));
        SubItemRecyclerAdapter subItemRecyclerAdapter = new SubItemRecyclerAdapter(subItemHashMap.get(itemList.get(position)));
        holder.subItemRecyclerView.setAdapter(subItemRecyclerAdapter);
        subItemRecyclerAdapter.setSubItemListener(new SubItemRecyclerAdapter.SubItemListener() {
            @Override
            public void onSubItemClick(String amount, String memo) {

                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {

                    itemClickListener.onItemClick(
                            colors[holder.getAdapterPosition()],
                            itemIconList.get(holder.getAdapterPosition()),
                            amount,
                            memo,
                            itemList.get(position)
                    );

                }

            }
        });

        if (category.equals("Expense_Category")) {

            holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.red));

        } else if (category.equals("Income_Category")) {

            holder.itemAmountTextView.setTextColor(context.getResources().getColor(R.color.green));

        }

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView itemIconImageView;
        private final CardView itemIconCardView;
        private final RecyclerView subItemRecyclerView;
        private final TextView dateTextView, itemAmountTextView, itemNameTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            itemAmountTextView = itemView.findViewById(R.id.itemAmountTextView);
            itemIconCardView = itemView.findViewById(R.id.itemIconCardView);
            itemIconImageView = itemView.findViewById(R.id.itemIconImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            subItemRecyclerView = itemView.findViewById(R.id.subItemRecyclerView);
            subItemRecyclerView.setHasFixedSize(true);
            subItemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }
    }
}
