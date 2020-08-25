package com.jaekapps.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SpendingFragment extends Fragment {

    PieChart pieChart;

    SpendingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spending_fragment, container, false);
        pieChart = view.findViewById(R.id.spendingCategoryPieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        ArrayList<PieEntry> arrayList = new ArrayList<>();
        arrayList.add(new PieEntry(34f, "USA"));
        arrayList.add(new PieEntry(29f, "India"));
        arrayList.add(new PieEntry(14f, "UK"));
        arrayList.add(new PieEntry(20, "Russia"));
        arrayList.add(new PieEntry(23, "Australia"));
        arrayList.add(new PieEntry(15, "China"));
        PieDataSet pieDataSet = new PieDataSet(arrayList, "Countries");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        return view;
    }
}
