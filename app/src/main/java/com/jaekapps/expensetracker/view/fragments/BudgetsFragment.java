package com.jaekapps.expensetracker.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.sharedpreferences.TabPositionPreferences;
import com.jaekapps.expensetracker.view.adapters.BudgetsViewPagerAdapter;

public class BudgetsFragment extends Fragment {

    private final String month, year;
    private OneTimeBudgetsFragment oneTimeBudgetsFragment;
    private PeriodicBudgetsFragment periodicBudgetsFragment;
    private TabLayout budgetsTabLayout;
    private TabPositionPreferences tabPositionPreferences;

    public BudgetsFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private void initializeViews(View view) {

        budgetsTabLayout = view.findViewById(R.id.budgetsTabLayout);
        oneTimeBudgetsFragment = new OneTimeBudgetsFragment(month, year);
        periodicBudgetsFragment = new PeriodicBudgetsFragment(month, year);
        ViewPager budgetsViewPager = view.findViewById(R.id.budgetsViewPager);
        BudgetsViewPagerAdapter budgetsViewPagerAdapter = new BudgetsViewPagerAdapter(getChildFragmentManager(), 1);
        budgetsViewPagerAdapter.addFragment(periodicBudgetsFragment, "PERIODIC");
        budgetsViewPagerAdapter.addFragment(oneTimeBudgetsFragment, "ONE TIME");
        budgetsViewPager.setAdapter(budgetsViewPagerAdapter);
        budgetsTabLayout.setupWithViewPager(budgetsViewPager);
        budgetsTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorTabIndicator));
        tabPositionPreferences = new TabPositionPreferences(view.getContext());
        tabPositionPreferences.setTabPosition(0);
    }

    public void updateTheFragment(int position, String month, String year) {

        if (position == 0) {

            periodicBudgetsFragment.updateTheViews(month, year);

        } else if (position == 1) {

            oneTimeBudgetsFragment.updateTheViews(month, year);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budgets, container, false);
        initializeViews(view);
        budgetsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPositionPreferences.setTabPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}
