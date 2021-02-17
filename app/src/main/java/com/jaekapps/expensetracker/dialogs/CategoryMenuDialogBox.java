package com.jaekapps.expensetracker.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jaekapps.expensetracker.R;

public class CategoryMenuDialogBox extends BottomSheetDialogFragment implements View.OnClickListener {

    private AppCompatButton cancelButton, confirmButton;
    private CategoryPickerListener categoryPickerListener;
    private RadioGroup categoryRadioGroup;
    private String category, chosenCategory;

    public CategoryMenuDialogBox(String category) {

        chosenCategory = category;
    }

    private void initializeOnClickListener() {

        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        cancelButton = view.findViewById(R.id.cancelButton);
        categoryRadioGroup = view.findViewById(R.id.categoryRadioGroup);
        confirmButton = view.findViewById(R.id.confirmButton);

        if (chosenCategory.equals("Expense_Category")) {

            categoryRadioGroup.check(R.id.expenseRadioButton);

        } else if (chosenCategory.equals("Income_Category")) {

            categoryRadioGroup.check(R.id.incomeRadioButton);

        }

    }

    public interface CategoryPickerListener {

        void cancel_menu();
        void confirm(String category);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_category_menu, container, false);
        initializeViews(view);
        initializeOnClickListener();
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.expenseRadioButton) {

                    category = "Expense_Category";

                } else if (checkedId == R.id.incomeRadioButton) {

                    category = "Income_Category";

                }

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            categoryPickerListener = (CategoryPickerListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement CategoryPickerListener!");

        }

    }

    @Override
    public void onClick(View v) {

        int view_id = v.getId();

        if (view_id == R.id.cancelButton) {

            categoryPickerListener.cancel_menu();

        } else if (view_id == R.id.confirmButton) {

            categoryPickerListener.confirm(category);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        categoryPickerListener = null;
    }
}
