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

public class SignUpUsingEmailFragment extends Fragment {

    private boolean emailAddressStatus = false;
    private boolean passwordStatus = false;
    private CardView signUpButton;
    private SignUpListener signUpListener;
    private TextInputEditText emailAddressTextInputEditText, passwordTextInputEditText, usernameTextInputEditText;
    private TextInputLayout passwordTextInputLayout;

    SignUpUsingEmailFragment() {}

    private boolean checkIfEmailAddressIsValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkEmailAddress() {

        if (Objects.requireNonNull(emailAddressTextInputEditText.getText()).toString().isEmpty()) {

            checkPassword();
            emailAddressTextInputEditText.setError("Email address is required.");
            emailAddressStatus = false;

        } else {

            checkPassword();

            if (!checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                emailAddressTextInputEditText.setError("Please, provide a valid email address.");
                emailAddressStatus = false;

            } else if (checkIfEmailAddressIsValid(emailAddressTextInputEditText.getText().toString())) {

                emailAddressStatus = true;

            }

        }

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
                    passwordTextInputEditText.setError("Password can not be more than 15 characters.");
                    passwordStatus = false;

                } else {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);
                    passwordStatus = true;

                }

            }

        }

    }

    private void initializeViews(View view) {

        emailAddressTextInputEditText = view.findViewById(R.id.emailAddressTextInputEditText);
        passwordTextInputEditText = view.findViewById(R.id.passwordTextInputEditText);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(15);
        signUpButton = view.findViewById(R.id.signUpButton);
        usernameTextInputEditText = view.findViewById(R.id.usernameTextInputEditText);
    }

    public boolean checkUsername() {

        boolean usernameStatus;
        checkEmailAddress();

        if (Objects.requireNonNull(usernameTextInputEditText.getText()).toString().isEmpty()) {

            usernameTextInputEditText.setError("Username is required.");

            usernameStatus = false;

        } else {

            boolean specialCharFound = false;

            for (Character character : usernameTextInputEditText.getText().toString().toCharArray()) {

                if ((character >= 33 && character <= 47) || (character >= 58 && character <= 64) ||
                        (character >= 91 && character <= 96) || (character >= 123 && character <= 126)) {

                    specialCharFound = true;
                    break;

                }

            }

            if (specialCharFound) {

                usernameTextInputEditText.setError("Username can not contain special characters.");
                usernameStatus = false;

            } else {

                usernameStatus = emailAddressStatus && passwordStatus;

            }

        }

        return usernameStatus;
    }

    public interface SignUpListener {

        void signUp();
    }

    String getEmailId() {

        return Objects.requireNonNull(emailAddressTextInputEditText.getText()).toString();
    }

    String getPassword() {

        return Objects.requireNonNull(passwordTextInputEditText.getText()).toString();
    }

    String getUsername() {

        return Objects.requireNonNull(usernameTextInputEditText.getText()).toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.signupusingemailfragment, container, false);
        initializeViews(view);
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
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpListener.signUp();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            signUpListener = (SignUpListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement SignUpListener.");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        signUpListener = null;
    }
}
