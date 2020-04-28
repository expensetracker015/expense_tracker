package com.jaekapps.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment {

    private int[] incomeCategoryItemIconId = {

            R.drawable.salary,
            R.drawable.awards,
            R.drawable.gift,
            R.drawable.sale,
            R.drawable.house,
            R.drawable.refunds,
            R.drawable.coupons,
            R.drawable.lottery,
            R.drawable.dividends,
            R.drawable.investments,
            R.drawable.others

    };
    private List<Integer> incomeCategoryLisItemIconId = new ArrayList<>();
    private List<String> incomeCategoryListItemName = new ArrayList<>();
    private String[] incomeCategoryItemNames = {

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


    IncomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.income_fragment, container, false);
        RecyclerView incomeRecyclerView = view.findViewById(R.id.incomeRecyclerView);
        incomeRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        incomeRecyclerView.setHasFixedSize(true);
        incomeCategoryLisItemIconId.clear();
        incomeCategoryListItemName.clear();

        for (int i = 0; i < incomeCategoryItemIconId.length; i++) {

            incomeCategoryLisItemIconId.add(incomeCategoryItemIconId[i]);
            incomeCategoryListItemName.add(incomeCategoryItemNames[i]);

        }

        IncomeItemRecyclerAdapter incomeItemRecyclerAdapter = new IncomeItemRecyclerAdapter(incomeCategoryLisItemIconId, incomeCategoryListItemName);
        incomeRecyclerView.setAdapter(incomeItemRecyclerAdapter);
        incomeItemRecyclerAdapter.setOnClickListener(new IncomeItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<String> incomeCategoryListItemName) {

                Intent intent = new Intent();
                intent.putExtra("item_name", incomeCategoryListItemName.get(position));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

            }
        });
        return view;

    }
}
