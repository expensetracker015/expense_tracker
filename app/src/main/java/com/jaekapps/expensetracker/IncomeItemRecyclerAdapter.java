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

public class IncomeItemRecyclerAdapter extends RecyclerView.Adapter<IncomeItemRecyclerAdapter.IncomeItemViewHolder> {

    private List<Integer> incomeCategoryLisItemIconId;
    private List<String> incomeCategoryListItemName;
    private OnItemClickListener onItemClickListener;

    IncomeItemRecyclerAdapter(List<Integer> incomeCategoryLisItemIconId, List<String> incomeCategoryListItemName) {

        this.incomeCategoryLisItemIconId = incomeCategoryLisItemIconId;
        this.incomeCategoryListItemName = incomeCategoryListItemName;

    }

    public interface OnItemClickListener {

        void onItemClick(int position, List<String> incomeCategoryListItemName);

    }

    void setOnClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public IncomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item_layout, parent, false);
        return new IncomeItemViewHolder(view, onItemClickListener, incomeCategoryListItemName);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeItemViewHolder holder, int position) {

        holder.incomeItemImageView.setImageResource(incomeCategoryLisItemIconId.get(position));
        holder.incomeItemNameTextView.setText(incomeCategoryListItemName.get(position));

    }

    @Override
    public int getItemCount() {

        return incomeCategoryLisItemIconId.size();

    }

    static class IncomeItemViewHolder extends RecyclerView.ViewHolder {

        ImageView incomeItemImageView;
        RelativeLayout incomeItemRelativeLayout;
        TextView incomeItemNameTextView;

        IncomeItemViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener, final List<String> incomeCategoryListItemName) {
            super(itemView);

            incomeItemImageView = itemView.findViewById(R.id.incomeItemImageView);
            incomeItemNameTextView = itemView.findViewById(R.id.incomeItemNameTextView);
            incomeItemRelativeLayout = itemView.findViewById(R.id.incomeItemRelativeLayout);
            incomeItemRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onItemClickListener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            onItemClickListener.onItemClick(position, incomeCategoryListItemName);

                        }

                    }

                }
            });

        }
    }

}
