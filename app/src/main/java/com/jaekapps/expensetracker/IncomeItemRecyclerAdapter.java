package com.jaekapps.expensetracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class IncomeItemRecyclerAdapter extends RecyclerView.Adapter<IncomeItemRecyclerAdapter.IncomeItemViewHolder> {

    int row_no = -1;
    private Context context;
    private IncomeItemClickListener incomeItemClickListener;
    private List<Integer> incomeCategoryListItemIconId;
    private List<String> incomeCategoryListItemName;

    IncomeItemRecyclerAdapter(Context context, List<Integer> incomeCategoryListItemIconId, List<String> incomeCategoryListItemName) {

        this.context = context;
        this.incomeCategoryListItemIconId = incomeCategoryListItemIconId;
        this.incomeCategoryListItemName = incomeCategoryListItemName;
    }

    private void changeTheIconAndCardColor(IncomeItemViewHolder holder, int position) {

        switch (position) {

            case 0:
                Glide.with(context).load(R.drawable.salary_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

            case 1:
                Glide.with(context).load(R.drawable.awards_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#FFB74D"));
                break;

            case 2:
                Glide.with(context).load(R.drawable.gift_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 3:
                Glide.with(context).load(R.drawable.sale_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#7986CB"));
                break;

            case 4:
                Glide.with(context).load(R.drawable.home_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#64B5F6"));
                break;

            case 5:
                Glide.with(context).load(R.drawable.refunds_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#BA68C8"));
                break;

            case 6:
                Glide.with(context).load(R.drawable.coupons_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#F06292"));
                break;

            case 7:
                Glide.with(context).load(R.drawable.lottery_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#81C784"));
                break;

            case 8:
                Glide.with(context).load(R.drawable.dividends_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 9:
                Glide.with(context).load(R.drawable.investments_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#FF8A65"));
                break;

            case 10:
                Glide.with(context).load(R.drawable.others_light).into(holder.incomeItemImageView);
                holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

        }

    }

    public interface IncomeItemClickListener {

        void selectItemFromIncomeCategory(int position, List<String> incomeCategoryListItemName);
    }

    void setOnClickListener(IncomeItemClickListener incomeItemClickListener) {

        this.incomeItemClickListener = incomeItemClickListener;
    }

    @NonNull
    @Override
    public IncomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new IncomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeItemViewHolder holder, final int position) {

        Glide.with(context).load(incomeCategoryListItemIconId.get(position)).into(holder.incomeItemImageView);
        holder.incomeItemNameTextView.setText(incomeCategoryListItemName.get(position));
        holder.incomeItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeItemClickListener.selectItemFromIncomeCategory(position, incomeCategoryListItemName);
                row_no = position;
                notifyDataSetChanged();
            }
        });

        if (row_no == position) {

            holder.incomeItemNameTextView.setTextColor(Color.parseColor("#FFFFFF"));
            changeTheIconAndCardColor(holder, position);

        } else {

            holder.incomeItemCardView.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.incomeItemNameTextView.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public int getItemCount() {

        return incomeCategoryListItemName.size();
    }

    static class IncomeItemViewHolder extends RecyclerView.ViewHolder {

        CardView incomeItemCardView;
        ImageView incomeItemImageView;
        TextView incomeItemNameTextView;

        IncomeItemViewHolder(@NonNull View itemView) {
            super(itemView);

            incomeItemCardView = itemView.findViewById(R.id.itemCardView);
            incomeItemImageView = itemView.findViewById(R.id.itemImageView);
            incomeItemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }

}
