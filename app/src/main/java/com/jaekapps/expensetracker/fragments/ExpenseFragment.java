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
import com.jaekapps.expensetracker.adapters.ExpenseItemRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment implements ExpenseItemRecyclerAdapter.ExpenseItemClickListener {

    private final int[] expenseCategoryItemIconId = {
            R.drawable.food_dark,
            R.drawable.bills_dark,
            R.drawable.transportation_dark,
            R.drawable.home_dark,
            R.drawable.car_dark,
            R.drawable.entertainment_dark,
            R.drawable.shopping_dark,
            R.drawable.cloth_dark,
            R.drawable.insurance_dark,
            R.drawable.tax_dark,
            R.drawable.phone_dark,
            R.drawable.cigarette_dark,
            R.drawable.health_dark,
            R.drawable.sports_dark,
            R.drawable.baby_dark,
            R.drawable.pet_dark,
            R.drawable.beauty_dark,
            R.drawable.electronics_dark,
            R.drawable.wine_dark,
            R.drawable.vegetables_dark,
            R.drawable.gift_dark,
            R.drawable.social_dark,
            R.drawable.travel_dark,
            R.drawable.education_dark,
            R.drawable.book_dark,
            R.drawable.office_dark,
            R.drawable.others_dark,
    };
    private final List<Integer> expenseCategoryListItemIconId = new ArrayList<>();
    private final List<String> expenseCategoryListItemName = new ArrayList<>();
    private final String[] expenseCategoryItemNames = {
            "Food",
            "Bills",
            "Transportation",
            "Home",
            "Car",
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
            "Pet",
            "Beauty",
            "Electronics",
            "Wine",
            "Vegetables",
            "Gift",
            "Social",
            "Travel",
            "Education",
            "Book",
            "Office",
            "Others",
    };
    ExpenseFragmentListener expenseFragmentListener;

    public ExpenseFragment() {}

    public interface ExpenseFragmentListener {

        void selectItemFromExpenseCategory(String item_name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_expense, container, false);
        RecyclerView expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        expenseRecyclerView.setHasFixedSize(true);
        expenseCategoryListItemIconId.clear();
        expenseCategoryListItemName.clear();

        for (int i = 0; i < expenseCategoryItemIconId.length; i++) {

            expenseCategoryListItemIconId.add(expenseCategoryItemIconId[i]);
            expenseCategoryListItemName.add(expenseCategoryItemNames[i]);

        }

        ExpenseItemRecyclerAdapter expenseItemRecyclerAdapter = new ExpenseItemRecyclerAdapter(view.getContext(), expenseCategoryListItemIconId, expenseCategoryListItemName);
        expenseRecyclerView.setAdapter(expenseItemRecyclerAdapter);
        expenseItemRecyclerAdapter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try  {

            expenseFragmentListener = (ExpenseFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement ExpenseFragmentListener!");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        expenseFragmentListener = null;
    }

    @Override
    public void selectItemFromExpenseCategory(int position, List<String> expenseCategoryListItemName) {

        expenseFragmentListener.selectItemFromExpenseCategory(expenseCategoryListItemName.get(position));
    }
}
