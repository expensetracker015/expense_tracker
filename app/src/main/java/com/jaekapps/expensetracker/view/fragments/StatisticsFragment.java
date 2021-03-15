package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
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
import com.jaekapps.expensetracker.view.adapters.ViewPagerAdapter;

public class StatisticsFragment extends Fragment {

    private BalanceFragment balanceFragment;
    private CashFlowFragment cashFlowFragment;
    private EarningFragment earningFragment;
    private ReportsFragment reportsFragment;
    private SpendingFragment spendingFragment;
    private final String month, year;
    private TabLayout tabLayout;
    private TabPositionPreferences tabPositionPreferences;

    public StatisticsFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private void initializeViews(View view) {

        balanceFragment = new BalanceFragment(month, year);
        cashFlowFragment = new CashFlowFragment(month, year);
        earningFragment = new EarningFragment(month, year);
        reportsFragment = new ReportsFragment(month, year);
        spendingFragment = new SpendingFragment(month, year);
        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(balanceFragment, "BALANCE");
        viewPagerAdapter.addFragment(cashFlowFragment, "CASH-FLOW");
        viewPagerAdapter.addFragment(spendingFragment, "SPENDING");
        viewPagerAdapter.addFragment(earningFragment, "EARNING");
        viewPagerAdapter.addFragment(reportsFragment, "REPORTS");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorTabIndicator));
        tabPositionPreferences = new TabPositionPreferences(view.getContext());
        tabPositionPreferences.setTabPosition(0);
    }

    public void updateTheFragment(Context context, int position, String month, String year) {

        if (position == 0) {

            balanceFragment.updateTheViews(context, month, year);

        } else if (position == 1) {

            cashFlowFragment.updateTheViews(context, month, year);

        } else if (position == 2) {

            spendingFragment.updateTheViews(context, month, year);

        } else if (position == 3) {

            earningFragment.updateTheViews(context, month, year);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initializeViews(view);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
