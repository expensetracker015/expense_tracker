package com.jaekapps.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.adapters.IncomeItemRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment implements IncomeItemRecyclerAdapter.IncomeItemClickListener {

    private final int[] incomeCategoryItemIconId = {
            R.drawable.salary_dark,
            R.drawable.awards_dark,
            R.drawable.gift_dark,
            R.drawable.sale_dark,
            R.drawable.home_dark,
            R.drawable.refunds_dark,
            R.drawable.coupons_dark,
            R.drawable.lottery_dark,
            R.drawable.dividends_dark,
            R.drawable.investments_dark,
            R.drawable.others_dark
    };
    private final List<Integer> incomeCategoryLisItemIconId = new ArrayList<>();
    private final List<String> incomeCategoryListItemName = new ArrayList<>();
    private final String[] incomeCategoryItemNames = {
            "Salary",
            "Awards",
            "Grants",
            "Sale",
            "Rental",
            "Refunds",
            "Coupons",
            "Lottery",
            "Dividends",
            "Investments",
            "Others",
    };
    private IncomeFragmentListener incomeFragmentListener;

    public IncomeFragment() {}

    public interface IncomeFragmentListener {

        void selectItemFromIncomeCategory(String item_name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_income, container, false);
        RecyclerView incomeRecyclerView = view.findViewById(R.id.incomeRecyclerView);
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        incomeRecyclerView.setHasFixedSize(true);
        incomeCategoryLisItemIconId.clear();
        incomeCategoryListItemName.clear();

        for (int i = 0; i < incomeCategoryItemIconId.length; i++) {

            incomeCategoryLisItemIconId.add(incomeCategoryItemIconId[i]);
            incomeCategoryListItemName.add(incomeCategoryItemNames[i]);

        }

        IncomeItemRecyclerAdapter incomeItemRecyclerAdapter = new IncomeItemRecyclerAdapter(view.getContext(), incomeCategoryLisItemIconId, incomeCategoryListItemName);
        incomeRecyclerView.setAdapter(incomeItemRecyclerAdapter);
        incomeItemRecyclerAdapter.setOnClickListener(this);
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            incomeFragmentListener = (IncomeFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement IncomeFragmentListener!");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        incomeFragmentListener = null;
    }

    @Override
    public void selectItemFromIncomeCategory(int position, List<String> incomeCategoryListItemName) {

        incomeFragmentListener.selectItemFromIncomeCategory(incomeCategoryListItemName.get(position));
    }
}
