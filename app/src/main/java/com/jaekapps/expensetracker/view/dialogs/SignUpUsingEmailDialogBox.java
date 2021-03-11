package com.jaekapps.expensetracker.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.jaekapps.expensetracker.R;

import java.util.Objects;

public class SignUpUsingEmailDialogBox extends DialogFragment {

    public SignUpUsingEmailDialogBox() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_sign_up_using_email, null);
        builder.setView(view);
        return builder.create();
    }
}
