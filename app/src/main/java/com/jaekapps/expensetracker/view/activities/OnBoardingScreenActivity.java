package com.jaekapps.expensetracker.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.view.adapters.OnBoardingScreenAdapter;

import me.relex.circleindicator.CircleIndicator;

public class OnBoardingScreenActivity extends AppCompatActivity {

    private CardView nextButtonFab;
    private final int[] on_boarding_screens = {
            R.drawable.on_boarding_screen_1,
            R.drawable.on_boarding_screen_2,
            R.drawable.on_boarding_screen_3,
            R.drawable.on_boarding_screen_4
    };
    private int current_page;
    private RelativeLayout onBoardingScreenLayout;
    private ViewPager onBoardingScreenViewPager;

    private void initializeViews() {

        CircleIndicator circleIndicator = findViewById(R.id.circleIndicator);
        nextButtonFab = findViewById(R.id.nextButtonFab);
        OnBoardingScreenAdapter onBoardingScreenAdapter = new OnBoardingScreenAdapter(this);
        onBoardingScreenLayout = findViewById(R.id.onBoardingScreenLayout);
        onBoardingScreenViewPager = findViewById(R.id.onBoardingScreenViewPager);
        onBoardingScreenViewPager.setAdapter(onBoardingScreenAdapter);
        onBoardingScreenLayout.setBackgroundResource(on_boarding_screens[0]);
        circleIndicator.setViewPager(onBoardingScreenViewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);

        initializeViews();
        nextButtonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_page < 3) {

                    onBoardingScreenViewPager.setCurrentItem(current_page + 1);

                } else if (current_page == 3) {

                    startActivity(new Intent(OnBoardingScreenActivity.this, MainActivity.class));
                    finishAffinity();

                }

            }
        });
        onBoardingScreenViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                current_page = position;
                onBoardingScreenLayout.setBackgroundResource(on_boarding_screens[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}