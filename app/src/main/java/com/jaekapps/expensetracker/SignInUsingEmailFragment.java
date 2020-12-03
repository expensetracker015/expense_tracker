package com.jaekapps.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignInUsingEmailFragment extends Fragment implements View.OnClickListener {

    private boolean passwordStatus;
    private CardView createAccountCardView, forgotPasswordButton, signInButton;
    private SignInListener signInListener;
    private TextInputEditText emailAddressTextInputEditText, passwordTextInputEditText;
    private TextInputLayout passwordTextInputLayout;

    SignInUsingEmailFragment() {}

    private boolean checkIfEmailAddressIsValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkPassword() {

        if (Objects.requireNonNull(passwordTextInputEditText.getText()).toString().isEmpty()) {

            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
            passwordTextInputEditText.setError("Password is required.");
            passwordStatus = false;

        } else {

            if (passwordTextInputEditText.getText().toString().contains(" ")) {

                passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                passwordTextInputEditText.setError("Password can not contain space.");
                passwordStatus = false;

            } else {

                if (passwordTextInputEditText.getText().toString().length() < 5) {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password is not strong. Password length should be more than 5.");
                    passwordStatus = false;

                } else if (passwordTextInputEditText.getText().toString().length() > 15) {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    passwordTextInputEditText.setError("Password length can not be more than 15.");
                    passwordStatus = false;

                } else {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);
                    passwordStatus = true;

                }

            }

        }

    }

    private void initializeOnClickListener() {

        createAccountCardView.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    private void initializeView(View view) {

        createAccountCardView = view.findViewById(R.id.createAccountCardView);
        emailAddressTextInputEditText = view.findViewById(R.id.emailAddressTextInputEditText);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);
        passwordTextInputEditText = view.findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signInButton = view.findViewById(R.id.signInButton);
    }

    public boolean checkEmailAddress() {

        boolean emailAddressStatus = false;
        checkPassword();

        if (Objects.requireNonNull(emailAddressTextInputEditText.getText()).toString().isEmpty()) {

            emailAddressTextInputEditText.setError("Email address is required.");

        } else {

            if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                emailAddressTextInputEditText.setError("Please, provide a valid email address.");

            } else if (checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                emailAddressStatus = passwordStatus;

            }

        }

        return emailAddressStatus;
    }

    public interface SignInListener {

        void forgotPassword();
        void loadSignUpUsingEmailFragment();
        void signIn();
    }

    String getEmailId() {

        return Objects.requireNonNull(emailAddressTextInputEditText.getText()).toString();
    }

    String getPassword() {

        return Objects.requireNonNull(passwordTextInputEditText.getText()).toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.signinusingemailfragment, container, false);
        initializeView(view);
        initializeOnClickListener();
        passwordTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            signInListener = (SignInListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement SignInListener");

        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.createAccountCardView) {

            signInListener.loadSignUpUsingEmailFragment();

        } else if (id == R.id.forgotPasswordButton) {

            signInListener.forgotPassword();

        } else if (id == R.id.signInButton) {

            signInListener.signIn();

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        signInListener = null;
    }
}
