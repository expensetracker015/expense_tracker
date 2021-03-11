package com.jaekapps.expensetracker.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaekapps.expensetracker.model.BEIAmount;
import com.jaekapps.expensetracker.model.CreateNewUser;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.view.dialogs.SignInUsingEmailOrGoogleDialogBox;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingEmailPreferences;
import com.jaekapps.expensetracker.sharedpreferences.SignInUsingGooglePreferences;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BEIAmount beiAmount;
    private CardView signInUsingEmailButton, signUpUsingEmailButton;
    private CreateNewUser createNewUser;
    private DatabaseReference userDBReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;
    private int currentYear;
    private final int GOOGLE_SIGN_IN = 1;
    private SignInUsingEmailPreferences signInUsingEmailPreferences;
    private SignInUsingGooglePreferences signInUsingGooglePreferences;
    private SignInUsingEmailOrGoogleDialogBox signInUsingEmailOrGoogleDialogBox;
    private String month, userId;
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

    private void goToHomeScreenActivity() {

        showToast("Successfully signed in.");
        signInUsingEmailOrGoogleDialogBox.dismiss();
        signInUsingEmailPreferences.setSignInUsingEmailStatus(false);
        signInUsingGooglePreferences.setSignInUsingGoogleStatus(true);
        userIdPreferences.setUserID(userId);
        startActivity(new Intent(this, HomeScreenActivity.class));
        finish();
    }

    private void goToSignInOrSignUpActivity(String modeOfSignIn) {

        Intent intent = new Intent(this, SignInOrSignUpActivity.class);
        intent.putExtra("mode_of_sign_in", modeOfSignIn);
        startActivity(intent);
    }

    private void initialization() {

        beiAmount = new BEIAmount();
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        createNewUser = new CreateNewUser();
        firebaseAuth = FirebaseAuth.getInstance();
        googleSignInButton = findViewById(R.id.googleSignInButton);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        int currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        findMonth(currentMonth);
        signInUsingEmailButton = findViewById(R.id.signInUsingEmailButton);
        signUpUsingEmailButton = findViewById(R.id.signUpUsingEmailButton);
        signInUsingEmailPreferences = new SignInUsingEmailPreferences(this);
        signInUsingGooglePreferences = new SignInUsingGooglePreferences(this);
        signInUsingEmailOrGoogleDialogBox = new SignInUsingEmailOrGoogleDialogBox();
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        userIdPreferences = new UserIdPreferences(this);
    }

    private void initializeOnClickListener() {

        googleSignInButton.setOnClickListener(this);
        signInUsingEmailButton.setOnClickListener(this);
        signUpUsingEmailButton.setOnClickListener(this);
    }

    private void showTheGoogleAccounts() {

        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN);
    }

    private void showToast(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void signInUsingGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        signInUsingEmailOrGoogleDialogBox.show(getSupportFragmentManager(), "dialog box");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult())
                                    .getAdditionalUserInfo())
                                    .isNewUser()) {

                                firebaseUser = firebaseAuth.getCurrentUser();

                                if (firebaseUser != null) {

                                    beiAmount.setBalance("0");
                                    beiAmount.setExpense("0");
                                    beiAmount.setIncome("0");
                                    createNewUser.setEmail_address(firebaseUser.getEmail());
                                    createNewUser.setUsername(firebaseUser.getDisplayName());
                                    userId = extractUserId(Objects.requireNonNull(firebaseUser.getEmail()).toCharArray());

                                    if (checkIfUserIdContainsSpecChar(userId)) {

                                        userId = convertToUserId(userId.toCharArray());

                                    }

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

                                                                        } else {

                                                                            showToast("Failed to sign in - " + Objects.requireNonNull(task.getException()).getMessage());
                                                                            signInUsingEmailOrGoogleDialogBox.dismiss();

                                                                        }

                                                                    }
                                                                });

                                                    } else {

                                                        showToast("Failed to sign in - " + Objects.requireNonNull(task.getException()).getMessage());
                                                        signInUsingEmailOrGoogleDialogBox.dismiss();

                                                    }

                                                }
                                            });

                                }

                            } else {

                                firebaseUser = firebaseAuth.getCurrentUser();

                                if (firebaseUser != null) {

                                    userId = extractUserId(Objects.requireNonNull(firebaseUser.getEmail()).toCharArray());

                                    if (checkIfUserIdContainsSpecChar(userId)) {

                                        userId = convertToUserId(userId.toCharArray());

                                    }

                                    goToHomeScreenActivity();

                                }

                            }

                        } else {

                            showToast("Failed to sign in - " + Objects.requireNonNull(task.getException()).getMessage());
                            signInUsingEmailOrGoogleDialogBox.dismiss();

                        }

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {

                    if (task.isSuccessful()) {

                        try {

                            GoogleSignInAccount account = task.getResult(ApiException.class);

                            if (account != null) {

                                signInUsingGoogle(account);

                            }

                        } catch (ApiException e) {

                            e.printStackTrace();

                        }

                    }

                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        initializeOnClickListener();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.googleSignInButton) {

            if (checkInternetConnection()) {

                showTheGoogleAccounts();

            } else {

                showToast("Please, check your internet connection.");

            }

        } else if (id == R.id.signInUsingEmailButton) {

            goToSignInOrSignUpActivity("Sign In");

        } else if (id == R.id.signUpUsingEmailButton) {

            goToSignInOrSignUpActivity("Sign Up");

        }

    }
}
