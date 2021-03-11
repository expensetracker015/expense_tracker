package com.jaekapps.expensetracker.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.SubItem;

public class SubItemRecyclerAdapter extends RecyclerView.Adapter<SubItemRecyclerAdapter.SubItemViewHolder> {

    private final SubItem[] subItems;
    private SubItemListener subItemListener;

    public SubItemRecyclerAdapter(SubItem[] subItems) {

        this.subItems = subItems;
    }

    public interface SubItemListener {

        void onSubItemClick(String amount, String memo);
    }

    public void setSubItemListener(SubItemListener subItemListener) {

        this.subItemListener = subItemListener;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sub_item, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubItemViewHolder holder, final int position) {

        holder.itemAmountTextView.setText(subItems[position].getAmount());
        holder.subItemNameTextView.setText(subItems[position].getItem_name());
        holder.subItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {

                    subItemListener.onSubItemClick(subItems[holder.getAdapterPosition()].getAmount(), subItems[holder.getAdapterPosition()].getItem_name());

                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return subItems.length;
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        private CardView subItemCardView;
        private final TextView itemAmountTextView, subItemNameTextView;

        public SubItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemAmountTextView = itemView.findViewById(R.id.itemAmountTextView);
            subItemCardView = itemView.findViewById(R.id.subItemCardView);
            subItemNameTextView = itemView.findViewById(R.id.subItemNameTextView);
        }
    }
}
