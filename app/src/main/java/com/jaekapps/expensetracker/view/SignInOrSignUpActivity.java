package com.jaekapps.expensetracker.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaekapps.expensetracker.model.BEIAmount;
import com.jaekapps.expensetracker.model.CreateNewUser;
import com.jaekapps.expensetracker.dialogs.ForgotPasswordDialogBox;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.fragments.SignInUsingEmailFragment;
import com.jaekapps.expensetracker.dialogs.SignInUsingEmailOrGoogleDialogBox;
import com.jaekapps.expensetracker.dialogs.SignUpUsingEmailDialogBox;
import com.jaekapps.expensetracker.fragments.SignUpUsingEmailFragment;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingEmailPreferences;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingGooglePreferences;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;

import java.util.Calendar;
import java.util.Objects;

public class SignInOrSignUpActivity extends AppCompatActivity implements ForgotPasswordDialogBox.ForgotPasswordListener,
        SignInUsingEmailFragment.SignInListener, SignUpUsingEmailFragment.SignUpListener {

    private BEIAmount beiAmount;
    private CardView backButtonCardView;
    private CreateNewUser createNewUser;
    private DatabaseReference userDBReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ForgotPasswordDialogBox forgotPasswordDialogBox;
    private FrameLayout fragmentContainer;
    private int currentYear;
    private SignInUsingEmailPreferences signInUsingEmailPreferences;
    private SignInUsingEmailFragment signInUsingEmailFragment;
    private SignInUsingEmailOrGoogleDialogBox signInUsingEmailOrGoogleDialogBox;
    private SignInUsingGooglePreferences signInUsingGooglePreferences;
    private SignUpUsingEmailDialogBox signUpUsingEmailDialogBox;
    private SignUpUsingEmailFragment signUpUsingEmailFragment;
    private String mode_of_sign_in, month, userId;
    private TextView titleTextView;
    private UserIdPreferences userIdPreferences;

    private boolean checkIfUserIdContainsSpecChar(String userId) {

        boolean specialCharFound = false;

        for (Character character : userId.toCharArray()) {

            if ((character >= 33 && character <= 47) || (character >= 58 && character <= 64) || (character >= 91 && character <= 96) || (character >= 123 && character <= 126)) {

                specialCharFound = true;
                break;

            }

        }

        return specialCharFound;
    }

    private boolean checkInternetConnection() {

        boolean internetConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                    internetConnected = true;

                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                    internetConnected = true;

                }

            }

        }

        return internetConnected;
    }

    private String convertToUserId(char[] userId) {

        int i;
        String userID;
        StringBuilder stringBuilder = new StringBuilder();

        for (i = 0; i < userId.length; i++) {

            if (!(userId[i] >= 33 && userId[i] <= 47) && !(userId[i] >= 58 && userId[i] <= 64) && !(userId[i] >= 91 && userId[i] <= 96) && !(userId[i] >= 123 && userId[i] <= 126)) {

                stringBuilder.append(userId[i]);

            }

        }

        userID = stringBuilder.toString();
        return userID;
    }

    private String extractUserId(char[] email_id) {

        int i, pos = 0;
        String userId;
        StringBuilder stringBuilder = new StringBuilder();

        for (i = 0; i < email_id.length; i++) {

            if (email_id[i] == '@') {

                pos = i;

            }

        }

        for (i = 0; i < pos; i++) {

            stringBuilder.append(email_id[i]);

        }

        userId = stringBuilder.toString();
        return userId;
    }

    private void findMonth(int currentMonth) {

        switch (currentMonth) {

            case 1:
                month = "Jan";
                break;

            case 2:
                month = "Feb";
                break;

            case 3:
                month = "Mar";
                break;

            case 4:
                month = "Apr";
                break;

            case 5:
                month = "May";
                break;

            case 6:
                month = "June";
                break;

            case 7:
                month = "July";
                break;

            case 8:
                month = "Aug";
                break;

            case 9:
                month = "Sep";
                break;

            case 10:
                month = "Oct";
                break;

            case 11:
                month = "Nov";
                break;

            case 12:
                month = "Dec";
                break;

        }

    }

    private void changeTheToolbarTitle(String title) {

        titleTextView.setText(title);
    }

    private void goToHomeScreenActivity() {

        signInUsingEmailPreferences.setSignInUsingEmailStatus(true);
        signInUsingGooglePreferences.setSignInUsingGoogleStatus(false);
        userIdPreferences.setUserID(userId);
        startActivity(new Intent(SignInOrSignUpActivity.this, HomeScreenActivity.class));
        finishAffinity();
        finish();
    }

    private void initialization() {

        backButtonCardView = findViewById(R.id.backButtonCardView);
        beiAmount = new BEIAmount();
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        createNewUser = new CreateNewUser();
        forgotPasswordDialogBox = new ForgotPasswordDialogBox();
        firebaseAuth = FirebaseAuth.getInstance();
        fragmentContainer = findViewById(R.id.fragmentContainer);
        int currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        findMonth(currentMonth);
        Intent intent = getIntent();
        mode_of_sign_in = intent.getStringExtra("mode_of_sign_in");
        signInUsingEmailPreferences = new SignInUsingEmailPreferences(this);
        signInUsingGooglePreferences = new SignInUsingGooglePreferences(this);
        signInUsingEmailFragment = new SignInUsingEmailFragment();
        signInUsingEmailOrGoogleDialogBox = new SignInUsingEmailOrGoogleDialogBox();
        signUpUsingEmailDialogBox = new SignUpUsingEmailDialogBox();
        signUpUsingEmailFragment = new SignUpUsingEmailFragment();
        titleTextView = findViewById(R.id.titleTextView);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        userIdPreferences = new UserIdPreferences(this);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void loadTheFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void loadTheFragmentWithSlideLeftCustomAnimation(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void showToast(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_sign_up);

        initialization();
        backButtonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (fragmentContainer != null) {

            if (savedInstanceState != null) {

                return;

            }

            if (mode_of_sign_in.equals("Sign In")) {

                changeTheToolbarTitle("Sign In Using Email");
                loadTheFragment(signInUsingEmailFragment);

            } else if (mode_of_sign_in.equals("Sign Up")) {

                changeTheToolbarTitle("Sign Up Using Email");
                loadTheFragment(signUpUsingEmailFragment);

            }

        }

    }

    @Override
    public void closeTheDialogBox() {

        forgotPasswordDialogBox.dismiss();
    }

    @Override
    public void forgotPassword() {

        forgotPasswordDialogBox.show(getSupportFragmentManager(), "forgot_password");
    }

    @Override
    public void loadSignInUsingEmailFragment() {

        changeTheToolbarTitle("Sign In Using Email");
        loadTheFragmentWithSlideLeftCustomAnimation(signInUsingEmailFragment);
    }

    @Override
    public void loadSignUpUsingEmailFragment() {

        changeTheToolbarTitle("Sign Up Using Email");
        loadTheFragmentWithSlideLeftCustomAnimation(signUpUsingEmailFragment);
    }

    @Override
    public void resetPassword() {

        if (forgotPasswordDialogBox.checkEmailAddress()) {

            String email_address = forgotPasswordDialogBox.getEmailAddress();
            firebaseAuth.sendPasswordResetEmail(email_address)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                showToast("Please, check your email and follow the instructions to reset the password. Sign in with the new password to continue.");

                            } else {

                                showToast("Failed to send email - " + Objects.requireNonNull(task.getException()).getMessage());

                            }

                        }
                    });

        }

    }

    @Override
    public void signIn() {

        if (checkInternetConnection()) {

            if (signInUsingEmailFragment.checkEmailAddress()) {

                signInUsingEmailOrGoogleDialogBox.show(getSupportFragmentManager(), "sign_in");
                String email_address = signInUsingEmailFragment.getEmailId();
                String password = signInUsingEmailFragment.getPassword();
                userId = extractUserId(email_address.toCharArray());

                if (checkIfUserIdContainsSpecChar(userId)) {

                    userId = convertToUserId(userId.toCharArray());

                }

                firebaseAuth.signInWithEmailAndPassword(email_address, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    goToHomeScreenActivity();
                                    showToast("Successfully signed in.");

                                } else {

                                    showToast("Failed to sign in - " + Objects.requireNonNull(task.getException()).getMessage());

                                }

                                signInUsingEmailOrGoogleDialogBox.dismiss();

                            }
                        });

            }

        } else {

            showToast("Please, check your internet connection.");
            signInUsingEmailOrGoogleDialogBox.dismiss();

        }

    }

    @Override
    public void signUp() {

        if (checkInternetConnection()) {

            if (signUpUsingEmailFragment.checkUsername()) {

                signUpUsingEmailDialogBox.show(getSupportFragmentManager(), "sign_up");
                String email_address = signUpUsingEmailFragment.getEmailId();
                String password = signUpUsingEmailFragment.getPassword();
                String username = signUpUsingEmailFragment.getUsername();
                createNewUser.setEmail_address(email_address);
                createNewUser.setUsername(username);
                beiAmount.setBalance("0");
                beiAmount.setExpense("0");
                beiAmount.setIncome("0");
                userId = extractUserId(email_address.toCharArray());

                if (checkIfUserIdContainsSpecChar(userId)) {

                    userId = convertToUserId(userId.toCharArray());

                }

                firebaseAuth.createUserWithEmailAndPassword(email_address, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    firebaseUser = firebaseAuth.getCurrentUser();

                                    if (firebaseUser != null) {

                                        userDBReference.child(userId)
                                                .child("Account_Details")
                                                .setValue(createNewUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            userDBReference.child(userId)
                                                                    .child("BEIAmount")
                                                                    .child(String.valueOf(currentYear))
                                                                    .child(month)
                                                                    .setValue(beiAmount)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()) {

                                                                                goToHomeScreenActivity();
                                                                                showToast("Account created successfully.");

                                                                            } else {

                                                                                showToast("Failed to sign up - " + Objects.requireNonNull(task.getException()).getMessage());

                                                                            }

                                                                            signUpUsingEmailDialogBox.dismiss();

                                                                        }
                                                                    });

                                                        } else {

                                                            showToast("Failed to sign up - " + Objects.requireNonNull(task.getException()).getMessage());
                                                            signUpUsingEmailDialogBox.dismiss();

                                                        }

                                                    }
                                                });

                                    }

                                } else {

                                    showToast("Failed to sign up - " + Objects.requireNonNull(task.getException()).getMessage());

                                }

                            }
                        });

            }

        } else {

            showToast("Please, check your internet connection.");

        }

    }
}
