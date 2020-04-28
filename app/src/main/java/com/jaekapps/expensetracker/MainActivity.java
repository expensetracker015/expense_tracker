package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
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
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CardView signInUsingEmailButton, signUpUsingEmailButton;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInButton googleSignInButton;
    int currentMonth, currentYear;
    String month;
    private CreateNewUser createNewUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 1;
    private SignInDialogBox signInDialogBox;
    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    public static MainActivity mainActivity;

    private boolean checkInternetConnection() {

        boolean internetConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                internetConnected = true;

            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                internetConnected = true;

            }

        }

        return internetConnected;

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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        signInDialogBox = new SignInDialogBox();
        signInDialogBox.show(getSupportFragmentManager(), "progress_dialog");
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    firebaseUser = firebaseAuth.getCurrentUser();
                    createNewUser.setEmail_address(firebaseUser.getEmail());
                    createNewUser.setUsername(firebaseUser.getDisplayName());
                    databaseReference.child(firebaseUser.getUid()).child("Account_Details").setValue(createNewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                signInDialogBox.dismiss();
                                signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(false);
                                signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(true);
                                Toast.makeText(MainActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                finish();

                            }

                        }
                    });

                } else {

                    signInDialogBox.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void signInUsingGoogle() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                e.printStackTrace();

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        createNewUser = new CreateNewUser();
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        currentYear = calendar.get(Calendar.YEAR);
        findMonth(currentMonth);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        googleSignInButton = findViewById(R.id.googleSignInButton);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mainActivity = this;
        signInDialogBox = new SignInDialogBox();
        signInUsingEmailButton = findViewById(R.id.signInUsingEmailButton);
        signUpUsingEmailButton = findViewById(R.id.signUpUsingEmailButton);
        signInDialogBox = new SignInDialogBox();
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);

        if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus() || signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
            finish();

        }

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternetConnection()) {

                    signInUsingGoogle();

                } else {

                    Toast.makeText(MainActivity.this, "Please, check your internet connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

        signInUsingEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignInUsingEmailActivity.class));

            }
        });
        signUpUsingEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, CreateAnAccountActivity.class));

            }
        });

    }
}
