package com.jaekapps.expensetracker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import java.util.Objects;

public class ForgotPasswordDialogBox extends AppCompatDialogFragment implements View.OnClickListener {

    private AppCompatButton resetPasswordButton;
    private AppCompatEditText emailEditText;
    private CardView closeDialogBox;
    private ForgotPasswordListener forgotPasswordListener;

    ForgotPasswordDialogBox() {}

    private boolean checkIfEmailAddressIsValid(String email_address) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches();
    }

    private void initializeOnClickListener() {

        closeDialogBox.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        closeDialogBox = view.findViewById(R.id.closeDialogBox);
        emailEditText = view.findViewById(R.id.emailEditText);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
    }

    public boolean checkEmailAddress() {

        boolean emailAddressStatus = false;

        if (Objects.requireNonNull(emailEditText.getText()).toString().isEmpty()) {

            emailEditText.setError("Email address is required.");

        } else {

            if (!checkIfEmailAddressIsValid(emailEditText.getText().toString())) {

                emailEditText.setError("Please, provide a valid email address.");

            } else if (checkIfEmailAddressIsValid(emailEditText.getText().toString())) {

                emailAddressStatus = true;

            }

        }

        return emailAddressStatus;
    }

    public interface ForgotPasswordListener {

        void closeTheDialogBox();
        void resetPassword();
    }

    public String getEmailAddress() {

        return Objects.requireNonNull(emailEditText.getText()).toString();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.forgot_password_dialog_box, null);
        initializeViews(view);
        initializeOnClickListener();
        Dialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ForgotPasswordDialogAnimation;
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            forgotPasswordListener = (ForgotPasswordListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString());

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.closeDialogBox:
                forgotPasswordListener.closeTheDialogBox();
                break;

            case R.id.resetPasswordButton:
                forgotPasswordListener.resetPassword();
                break;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        forgotPasswordListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();

        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);

        }

    }
}
