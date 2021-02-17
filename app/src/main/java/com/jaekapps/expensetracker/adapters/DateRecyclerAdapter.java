package com.jaekapps.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jaekapps.expensetracker.R;

import java.util.List;

public class DateRecyclerAdapter extends RecyclerView.Adapter<DateRecyclerAdapter.DateOfEachMonthViewHolder> {

    private final List<String> dateList;
    private DateClickListener dateClickListener;

    public DateRecyclerAdapter(List<String> dateList) {

        this.dateList = dateList;
    }

    public interface DateClickListener {

        void onDateClick(String date);
        void deleteDate(String date);
    }

    public void setDateClickListener(DateClickListener dateClickListener) {

        this.dateClickListener = dateClickListener;
    }

    @NonNull
    @Override
    public DateOfEachMonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_date, parent, false);
        return new DateOfEachMonthViewHolder(dateClickListener, dateList, view);

    }

    @Override
    public void onBindViewHolder(@NonNull DateOfEachMonthViewHolder holder, final int position) {

        holder.dateTextView.setText(dateList.get(position));
        String number = String.valueOf(position + 1);
        holder.numberTextView.setText(number);
        holder.dateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateClickListener.onDateClick(dateList.get(position));
            }
        });
        holder.deleteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateClickListener.deleteDate(dateList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {

        return dateList.size();
    }

    static class DateOfEachMonthViewHolder extends RecyclerView.ViewHolder {

        CardView dateCardView, deleteCardView;
        TextView dateTextView, numberTextView;

        DateOfEachMonthViewHolder(final DateClickListener dateClickListener, final List<String> dateList, @NonNull View itemView) {
            super(itemView);

            dateCardView = itemView.findViewById(R.id.dateCardView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            deleteCardView = itemView.findViewById(R.id.deleteCardView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
        }
    }
}
