package com.jaekapps.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DateOfEachMonthRecyclerAdapter extends RecyclerView.Adapter<DateOfEachMonthRecyclerAdapter.DateOfEachMonthViewHolder> {

    private List<String> dateList;
    private OnDateClickListener onDateClickListener;

    DateOfEachMonthRecyclerAdapter(List<String> dateList) {

        this.dateList = dateList;

    }

    public interface OnDateClickListener {

        void onDateClick(int position, List<String> dateList, View view);

    }

    void setOnDateClickListener(OnDateClickListener onDateClickListener) {

        this.onDateClickListener = onDateClickListener;

    }

    @NonNull
    @Override
    public DateOfEachMonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_layout_for_each_month, parent, false);
        return new DateOfEachMonthViewHolder(onDateClickListener, dateList, view);

    }

    @Override
    public void onBindViewHolder(@NonNull DateOfEachMonthViewHolder holder, int position) {

        holder.dateTextView.setText(dateList.get(position));
        String number = String.valueOf(position + 1);
        holder.numberTextView.setText(number);

    }

    @Override
    public int getItemCount() {

        return dateList.size();

    }

    static class DateOfEachMonthViewHolder extends RecyclerView.ViewHolder {

        CircleImageView delete, edit;
        LinearLayout numberDateLinearLayout;
        TextView dateTextView, numberTextView;

        DateOfEachMonthViewHolder(final OnDateClickListener onDateClickListener, final List<String> dateList, @NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.dateTextView);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            numberDateLinearLayout = itemView.findViewById(R.id.numberDateLinearLayout);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onDateClickListener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            onDateClickListener.onDateClick(position, dateList, view);

                        }

                    }

                }
            });
            numberDateLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onDateClickListener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            onDateClickListener.onDateClick(position, dateList, view);

                        }

                    }

                }
            });

        }
    }
}
