package com.jaekapps.expensetracker.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jaekapps.expensetracker.R;

public class OnBoardingScreenAdapter extends PagerAdapter {

    private final Context context;
    private final int[] on_boarding_images = {
            R.drawable.on_boarding_image_1,
            R.drawable.on_boarding_image_2,
            R.drawable.on_boarding_image_3,
            R.drawable.on_boarding_image_4
    };
    private final String[] desc = {
            "All your finances in one place.",
            "Track you spending.",
            "Know where your money goes.",
            "Learn how to manage your finances."
    };

    public OnBoardingScreenAdapter(Context context) {

        this.context = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { return view == object; }

    @Override
    public int getCount() { return desc.length; }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_on_boarding_screen, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(on_boarding_images[position]);
        TextView onBoardingScreenTextView = view.findViewById(R.id.onBoardingScreenTextView);
        onBoardingScreenTextView.setText(desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { container.removeView((RelativeLayout) object); }
}
