package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;

public class OneTimeBudgetsFragment extends Fragment {

    private final String month, year;
    private FloatingActionButton oneTimeBudgetFab;
    private LinearLayout oneTimeBudgetsLinearLayout;
    private OneTimeBudgetsListener oneTimeBudgetsListener;
    private ProgressBar loadingProgressbar;
    private String userId;

    public OneTimeBudgetsFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private void initializeViews(View view) {

        loadingProgressbar = view.findViewById(R.id.loadingProgressbar);
        oneTimeBudgetFab = view.findViewById(R.id.oneTimeBudgetFab);
        oneTimeBudgetsLinearLayout = view.findViewById(R.id.oneTimeBudgetsLinearLayout);
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
    }

    public interface OneTimeBudgetsListener {

        void goToBudgetsActivityFromOTBF(int position, String budgetCategory);
    }

    public void updateTheViews(final String month, final String year) {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_time_budgets, container, false);
        initializeViews(view);
        updateTheViews(month, year);
        oneTimeBudgetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oneTimeBudgetsListener.goToBudgetsActivityFromOTBF(1, "ONE TIME");
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            oneTimeBudgetsListener = (OneTimeBudgetsListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement OneTimeBudgetsListener!");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        oneTimeBudgetsListener = null;
    }
}
