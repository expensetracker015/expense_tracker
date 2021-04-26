package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaekapps.expensetracker.R;

public class OneTimeBudgetsFragment extends Fragment {

    private FloatingActionButton oneTimeBudgetFab;
    private LinearLayout one_time_budgets_linearLayout;
    private OneTimeBudgetsListener oneTimeBudgetsListener;

    private void initializeViews(View view) {

        oneTimeBudgetFab = view.findViewById(R.id.oneTimeBudgetFab);
        one_time_budgets_linearLayout = view.findViewById(R.id.one_time_budgets_linearLayout);
    }

    public interface OneTimeBudgetsListener {

        void goToBudgetsActivityFromOTBF();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_time_budgets, container, false);
        initializeViews(view);
        oneTimeBudgetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oneTimeBudgetsListener.goToBudgetsActivityFromOTBF();
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
