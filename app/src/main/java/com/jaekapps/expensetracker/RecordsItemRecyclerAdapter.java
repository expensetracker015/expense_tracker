package com.jaekapps.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordsItemRecyclerAdapter extends RecyclerView.Adapter<RecordsItemRecyclerAdapter.RecordsItemViewHolder> {

    private final ArrayList<Integer> itemIconList;
    private final ArrayList<String> itemAmountList, itemDateList, itemNameList;
    private final int[] colors;

    RecordsItemRecyclerAdapter(ArrayList<Integer> itemIconList, ArrayList<String> itemAmountList, ArrayList<String> itemDateList, ArrayList<String> itemNameList, int[] colors) {

        this.colors = colors;
        this.itemAmountList = itemAmountList;
        this.itemDateList = itemDateList;
        this.itemIconList = itemIconList;
        this.itemNameList = itemNameList;
    }

    @NonNull
    @Override
    public RecordsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_item_layout, parent, false);
        return new RecordsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsItemViewHolder holder, int position) {

        holder.recordItemAmountTextView.setText(itemAmountList.get(position));
        holder.recordItemDateTextView.setText(itemDateList.get(position));
        holder.recordItemIconCardView.setCardBackgroundColor(colors[position]);
        holder.recordItemIconImageView.setImageResource(itemIconList.get(position));
        holder.recordItemNameTextView.setText(itemNameList.get(position));

        if (position == itemNameList.size() - 1) {

            holder.divider.setVisibility(View.GONE);

        }

        if (position == 0) {

            holder.divider.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {

        return itemNameList.size();
    }

    public static class RecordsItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView recordItemIconImageView;
        private CardView recordItemIconCardView;
        private TextView recordItemAmountTextView, recordItemDateTextView, recordItemNameTextView;
        private View divider;

        public RecordsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            divider = itemView.findViewById(R.id.divider);
            recordItemAmountTextView = itemView.findViewById(R.id.recordItemAmountTextView);
            recordItemDateTextView = itemView.findViewById(R.id.recordItemDateTextView);
            recordItemIconCardView = itemView.findViewById(R.id.recordItemIconCardView);
            recordItemIconImageView = itemView.findViewById(R.id.recordItemIconImageView);
            recordItemNameTextView = itemView.findViewById(R.id.recordItemNameTextView);
        }
    }
}
