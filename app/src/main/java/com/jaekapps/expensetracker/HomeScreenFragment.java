package com.jaekapps.expensetracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeScreenFragment extends Fragment {

    private LinearLayout homeScreenFragmentLinearLayout;
    private OnItemClickListener onItemClickListener;
    private RecyclerView homeScreenFragmentDateRecyclerView;
    private RelativeLayout categoryRelativeLayout;
    private TextView balanceTextView, categoryTextView, expenseTextView, incomeTextView;

    HomeScreenFragment() {}

    public interface OnItemClickListener {

        void onItemClick(View view);

    }

    void hideTheHomeScreenFragmentLinearLayout(String category, List<String> dateList) {

        if (dateList != null) {

            categoryTextView.setText(category);
            homeScreenFragmentLinearLayout.setVisibility(View.GONE);
            DateOfEachMonthRecyclerAdapter dateOfEachMonthRecyclerAdapter = new DateOfEachMonthRecyclerAdapter(dateList);
            homeScreenFragmentDateRecyclerView.setAdapter(dateOfEachMonthRecyclerAdapter);
            dateOfEachMonthRecyclerAdapter.setOnDateClickListener(new DateOfEachMonthRecyclerAdapter.OnDateClickListener() {
                @Override
                public void onDateClick(int position, List<String> dateList, View view) {



                }
            });

        }

    }

    void showTheHomeScreenFragmentLinearLayout(String category) {

        categoryTextView.setText(category);
        homeScreenFragmentLinearLayout.setVisibility(View.VISIBLE);

    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }

    void setBEIAmount(String balance, String expense, String income) {

        balanceTextView.setText(String.valueOf(balance));
        expenseTextView.setText(String.valueOf(expense));
        incomeTextView.setText(String.valueOf(income));

    }

    void setBEIAmountForSignInUsingGoogle() {

        balanceTextView.setText("0");
        expenseTextView.setText("0");
        incomeTextView.setText("0");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_screen_fragment, container, false);
        balanceTextView = view.findViewById(R.id.balanceTextView);
        categoryRelativeLayout = view.findViewById(R.id.categoryRelativeLayout);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        categoryTextView.setText("Expense Category");
        expenseTextView = view.findViewById(R.id.expenseTextView);
        FloatingActionButton addFab = view.findViewById(R.id.expenseIncomeFab);
        incomeTextView = view.findViewById(R.id.incomeTextView);
        homeScreenFragmentLinearLayout = view.findViewById(R.id.homeScreenFragmentLinearLayout);
        homeScreenFragmentDateRecyclerView = view.findViewById(R.id.homeScreenFragmentDateRecyclerView);
        homeScreenFragmentDateRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeScreenFragmentDateRecyclerView.setHasFixedSize(true);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClickListener.onItemClick(view);

            }
        });
        categoryRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClickListener.onItemClick(view);

            }
        });
        return view;

    }
}
