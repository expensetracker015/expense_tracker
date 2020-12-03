package com.jaekapps.expensetracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ExpenseItemsListDialogBox extends DialogFragment {

    private final ArrayList<Integer> itemIconList;
    private final ArrayList<String> amountList, itemList;
    private final int[] colors;

    ExpenseItemsListDialogBox(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList, int[] colors) {

        this.amountList = amountList;
        this.colors = colors;
        this.itemIconList = itemIconList;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.expense_items_list_dialog_box, null);
        RecyclerView expenseItemsListRecyclerView = view.findViewById(R.id.expenseItemsListRecyclerView);
        expenseItemsListRecyclerView.setHasFixedSize(true);
        expenseItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        MoreExpensesRecyclerAdapter moreExpensesRecyclerAdapter = new MoreExpensesRecyclerAdapter(
                itemIconList,
                amountList,
                itemList,
                view.getContext(),
                colors
        );
        expenseItemsListRecyclerView.setAdapter(moreExpensesRecyclerAdapter);
        builder.setView(view);
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
