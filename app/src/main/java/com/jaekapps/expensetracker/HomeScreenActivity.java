package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    private SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    private SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FloatingActionButton addFab = findViewById(R.id.addFab);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

                    googleSignInClient.signOut().addOnCompleteListener(HomeScreenActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(false);
                                signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(false);
                                Toast.makeText(HomeScreenActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                                finish();

                            } else {

                                Toast.makeText(HomeScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                } else if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

                    FirebaseAuth.getInstance().signOut();
                    signInUsingEmailConfigActivity.writeSignInUsingEmailStatus(false);
                    signInUsingGoogleConfigActivity.writeSignInUsingGoogleStatus(false);
                    Toast.makeText(HomeScreenActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                    finish();

                }

            }
        });

    }
}
