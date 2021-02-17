package com.jaekapps.expensetracker.adapters;

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
import com.jaekapps.expensetracker.R;

import java.util.List;

public class ExpenseItemRecyclerAdapter extends RecyclerView.Adapter<ExpenseItemRecyclerAdapter.ExpenseItemViewHolder> {

    int row_no = -1;
    private final Context context;
    private ExpenseItemClickListener expenseItemClickListener;
    private final List<Integer> expenseCategoryListItemIconId;
    private final List<String> expenseCategoryListItemName;

    public ExpenseItemRecyclerAdapter(Context context, List<Integer> expenseCategoryListItemIconId, List<String> expenseCategoryListItemName) {

        this.context = context;
        this.expenseCategoryListItemIconId = expenseCategoryListItemIconId;
        this.expenseCategoryListItemName = expenseCategoryListItemName;
    }

    private void changeTheIconAndCardColor(ExpenseItemViewHolder holder, int position) {

        switch (position) {

            case 0:
                Glide.with(context).load(R.drawable.food_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#FFB74D"));
                break;

            case 1:
                Glide.with(context).load(R.drawable.bills_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#64B5F6"));
                break;

            case 2:
                Glide.with(context).load(R.drawable.transportation_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 3:
                Glide.with(context).load(R.drawable.home_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#FFB74D"));
                break;

            case 4:
                Glide.with(context).load(R.drawable.car_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#BA68C8"));
                break;

            case 5:
                Glide.with(context).load(R.drawable.entertainment_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#7986CB"));
                break;

            case 6:
                Glide.with(context).load(R.drawable.shopping_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#F06292"));
                break;

            case 7:
                Glide.with(context).load(R.drawable.cloth_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 8:
                Glide.with(context).load(R.drawable.insurance_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#BA68C8"));
                break;

            case 9:
                Glide.with(context).load(R.drawable.tax_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 10:
                Glide.with(context).load(R.drawable.phone_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#81C784"));
                break;

            case 11:
                Glide.with(context).load(R.drawable.cigarette_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

            case 12:
                Glide.with(context).load(R.drawable.health_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#81C784"));
                break;

            case 13:
                Glide.with(context).load(R.drawable.sports_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#FF8A65"));
                break;

            case 14:
                Glide.with(context).load(R.drawable.baby_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#BA68C8"));
                break;

            case 15:
                Glide.with(context).load(R.drawable.pet_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

            case 16:
                Glide.with(context).load(R.drawable.beauty_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#F06292"));
                break;

            case 17:
                Glide.with(context).load(R.drawable.electronics_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#64B5F6"));
                break;

            case 18:
                Glide.with(context).load(R.drawable.wine_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#7986CB"));
                break;

            case 19:
                Glide.with(context).load(R.drawable.vegetables_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#81C784"));
                break;

            case 20:
                Glide.with(context).load(R.drawable.gift_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

            case 21:
                Glide.with(context).load(R.drawable.social_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#BA68C8"));
                break;

            case 22:
                Glide.with(context).load(R.drawable.travel_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#4DB6AC"));
                break;

            case 23:
                Glide.with(context).load(R.drawable.education_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#FFB74D"));
                break;

            case 24:
                Glide.with(context).load(R.drawable.book_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#7986CB"));
                break;

            case 25:
                Glide.with(context).load(R.drawable.office_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#FF8A65"));
                break;

            case 26:
                Glide.with(context).load(R.drawable.others_light).into(holder.expenseItemImageView);
                holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#E57373"));
                break;

        }

    }

    public interface ExpenseItemClickListener {

        void selectItemFromExpenseCategory(int position, List<String> expenseCategoryListItemName);
    }

    public void setOnClickListener(ExpenseItemClickListener expenseItemClickListener) {

        this.expenseItemClickListener = expenseItemClickListener;
    }

    @NonNull
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subcategory_item, parent, false);
        return new ExpenseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemViewHolder holder, final int position) {

        Glide.with(context).load(expenseCategoryListItemIconId.get(position)).into(holder.expenseItemImageView);
        holder.expenseItemNameTextView.setText(expenseCategoryListItemName.get(position));
        holder.expenseItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseItemClickListener.selectItemFromExpenseCategory(position, expenseCategoryListItemName);
                row_no = position;
                notifyDataSetChanged();
            }
        });

        if (row_no == position) {

            holder.expenseItemNameTextView.setTextColor(Color.parseColor("#FFFFFF"));
            changeTheIconAndCardColor(holder, position);

        } else {

            holder.expenseItemCardView.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.expenseItemNameTextView.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public int getItemCount() {

        return expenseCategoryListItemName.size();
    }

    public static class ExpenseItemViewHolder extends RecyclerView.ViewHolder {

        CardView expenseItemCardView;
        ImageView expenseItemImageView;
        TextView expenseItemNameTextView;

        ExpenseItemViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseItemCardView = itemView.findViewById(R.id.itemCardView);
            expenseItemImageView = itemView.findViewById(R.id.itemImageView);
            expenseItemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }

}
