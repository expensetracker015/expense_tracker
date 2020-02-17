package com.jaekapps.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity {

    private LoginStateConfigActivity loginStateConfigActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FloatingActionButton addFab = findViewById(R.id.addFab);
        loginStateConfigActivity = new LoginStateConfigActivity(this);
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

                FirebaseAuth.getInstance().signOut();
                loginStateConfigActivity.writeLogInStatus(false);
                Toast.makeText(HomeScreenActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
                finish();

            }
        });

    }
}
