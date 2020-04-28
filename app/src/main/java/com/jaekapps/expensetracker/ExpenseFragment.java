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

public class ExpenseFragment extends Fragment {

    private int[] expenseCategoryItemIconId = {

            R.drawable.food,
            R.drawable.bills,
            R.drawable.transportation,
            R.drawable.house,
            R.drawable.car,
            R.drawable.education,
            R.drawable.book,
            R.drawable.office,
            R.drawable.entertainment,
            R.drawable.shopping,
            R.drawable.cloth,
            R.drawable.insurance,
            R.drawable.tax,
            R.drawable.telephone,
            R.drawable.cigarette,
            R.drawable.health,
            R.drawable.sports,
            R.drawable.baby,
            R.drawable.pet,
            R.drawable.beauty,
            R.drawable.electronics,
            R.drawable.wine,
            R.drawable.vegetables,
            R.drawable.gift,
            R.drawable.social,
            R.drawable.travel,
            R.drawable.others,

    };
    private List<Integer> expenseCategoryListItemIconId = new ArrayList<>();
    private List<String> expenseCategoryListItemName = new ArrayList<>();
    private String[] expenseCategoryItemNames = {

            "Food",
            "Bills",
            "Transportation",
            "Home",
            "Car",
            "Education",
            "Book",
            "Office",
            "Entertainment",
            "Shopping",
            "Clothing",
            "Insurance",
            "Tax",
            "Telephone",
            "Cigarette",
            "Health",
            "Sports",
            "Baby",
            "Pets",
            "Beauty",
            "Electronics",
            "Wine",
            "Vegetables",
            "Gift",
            "Social",
            "Travel",
            "Others",

    };

    ExpenseFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.expense_fragment, container, false);
        RecyclerView expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        expenseRecyclerView.setHasFixedSize(true);
        expenseCategoryListItemIconId.clear();
        expenseCategoryListItemName.clear();

        for (int i = 0; i < expenseCategoryItemIconId.length; i++) {

            expenseCategoryListItemIconId.add(expenseCategoryItemIconId[i]);
            expenseCategoryListItemName.add(expenseCategoryItemNames[i]);

        }

        ExpenseItemRecyclerAdapter expenseItemRecyclerAdapter = new ExpenseItemRecyclerAdapter(expenseCategoryListItemIconId, expenseCategoryListItemName);
        expenseRecyclerView.setAdapter(expenseItemRecyclerAdapter);
        expenseItemRecyclerAdapter.setOnClickListener(new ExpenseItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<String> expenseCategoryListItemName) {

                Intent intent = new Intent();
                intent.putExtra("item_name", expenseCategoryListItemName.get(position));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();

            }
        });
        return view;

    }
}
