package com.jaekapps.expensetracker.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaekapps.expensetracker.model.BEIAmount;
import com.jaekapps.expensetracker.R;
import com.jaekapps.expensetracker.model.SubItem;
import com.jaekapps.expensetracker.sharedpreferences.UserIdPreferences;
import com.jaekapps.expensetracker.view.adapters.Top5IncomeRecyclerAdapter;
import com.jaekapps.expensetracker.view.adapters.TopIncomeCategoriesRecyclerAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EarningFragment extends Fragment implements View.OnClickListener {

    int currentMonth;
    private AppCompatButton showMoreButton;
    private ArrayList<Float> itemAmountPercentageList;
    private ArrayList<Integer> indexList, itemIconList, itemIconList2, topCategoriesItemIndexList, topCategoriesItemPercentageList;
    private ArrayList<PieEntry> incomeItemList;
    private ArrayList<String> amountList, itemList, modifiedAmountList, modifiedItemList, newAmountList, newItemList,
            topCategoriesItemAmountList, topCategoriesItemList;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private DatabaseReference userDBReference;
    private EarningFragmentListener earningFragmentListener;
    private int currentYear;
    private int[] colors, topCategoriesItemColors;
    private PieChart earningCategoryPieChart;
    private PieData incomePieData;
    private PieDataSet incomePieDataSet;
    private RecyclerView incomeItemRecyclerView, topIncomeCategoriesRecyclerView;
    private String current_month, date, income, month, userId, year;
    private TextView monthTitleTextView, natureOfEarningAmountTextView, natureOfEarningMonthTextView, earningAmountTextView,
            earningCurrentMonthTextView, yearTitleTextView;
    private Top5IncomeRecyclerAdapter top5IncomeRecyclerAdapter;
    private TopIncomeCategoriesRecyclerAdapter topIncomeCategoriesRecyclerAdapter;

    EarningFragment(String month, String year) {

        this.month = month;
        this.year = year;
    }

    private ArrayList<Integer> addIconsToIconList(int listSize) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0 ; i < listSize; i++) {

            itemIconList.add(R.drawable.app_logo_white);

        }

        return itemIconList;
    }

    private ArrayList<Integer> addIconsToIconList(ArrayList<String> itemCategoryList, HashMap<String, SubItem[]> subItemHashMap) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0; i < itemCategoryList.size(); i++) {

            SubItem[] subItems = subItemHashMap.get(itemCategoryList.get(i));

            if (subItems != null) {

                for (int j = 0; j < subItems.length; j++) {

                    itemIconList.add(findTheIcon(itemCategoryList.get(i)));

                }

            }

        }

        return itemIconList;
    }

    private ArrayList<Integer> addIconsToIconList(ArrayList<String> itemList, ArrayList<String> itemCategoryList, HashMap<String, SubItem[]> subItemHashMap) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {

            String sub_item = itemList.get(i);

            for (int j = 0; j < itemCategoryList.size(); j++) {

                SubItem[] subItems = subItemHashMap.get(itemCategoryList.get(j));

                if (subItems != null) {

                    for (SubItem subItem : subItems) {

                        if (sub_item.equals(subItem.getItem_name())) {

                            itemIconList.add(findTheIcon(itemCategoryList.get(j)));

                        }

                    }

                }

            }

        }

        return itemIconList;
    }

    private ArrayList<Integer> addIconsToIconList2(ArrayList<String> itemList, ArrayList<String> itemCategoryList, HashMap<String, SubItem[]> subItemHashMap) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {

            String sub_item = itemList.get(i);

            for (int j = 0; j < itemCategoryList.size(); j++) {

                SubItem[] subItems = subItemHashMap.get(itemCategoryList.get(j));

                if (subItems != null) {

                    for (SubItem subItem : subItems) {

                        if (sub_item.equals(subItem.getItem_name())) {

                            itemIconList.add(findTheIcon(itemCategoryList.get(j)));

                        }

                    }

                }

            }

        }

        return itemIconList;
    }

    private ArrayList<String> makeAListOfTop5Categories(ArrayList<String> amountList) {

        ArrayList<String> tempList = new ArrayList<>();
        BigDecimal temp;
        BigDecimal[] amount = new BigDecimal[amountList.size()];

        for (int i = 0; i < amountList.size(); i++) {

            amount[i] = new BigDecimal(amountList.get(i));

        }

        for (int i = 0; i < amount.length; i++) {

            for (int j = i + 1; j < amount.length; j++) {

                if (amount[i].compareTo(amount[j]) < 0) {

                    temp = amount[j];
                    amount[j] = amount[i];
                    amount[i] = temp;

                }

            }

        }

        tempList.clear();

        for (int i = 0; i < 5; i++) {

            tempList.add(amount[i].toString());

        }

        return tempList;
    }

    private ArrayList<String> sortTheTopCategoriesItemAmountList(ArrayList<String> topCategoriesItemAmountList) {

        ArrayList<String> tempList = new ArrayList<>();
        BigDecimal temp;
        BigDecimal[] amount = new BigDecimal[topCategoriesItemAmountList.size()];

        for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

            amount[i] = new BigDecimal(topCategoriesItemAmountList.get(i));

        }

        for (int i = 0; i < amount.length; i++) {

            for (int j = i + 1; j < amount.length; j++) {

                if (amount[i].compareTo(amount[j]) < 0) {

                    temp = amount[j];
                    amount[j] = amount[i];
                    amount[i] = temp;

                }

            }

        }

        tempList.clear();

        for (BigDecimal bigDecimal : amount) {

            tempList.add(bigDecimal.toString());

        }

        return tempList;
    }

    private int findTheIcon(String item_name) {

        int icon_id = 0;

        switch (item_name) {

            case "Salary":
                icon_id = R.drawable.salary_light;
                break;
            case "Awards":
                icon_id = R.drawable.awards_light;
                break;
            case "Grants":
                icon_id = R.drawable.gift_light;
                break;
            case "Sale":
                icon_id = R.drawable.sale_light;
                break;
            case "Rental":
                icon_id = R.drawable.home_light;
                break;
            case "Refunds":
                icon_id = R.drawable.refunds_light;
                break;
            case "Coupons":
                icon_id = R.drawable.coupons_light;
                break;
            case "Lottery":
                icon_id = R.drawable.lottery_light;
                break;
            case "Dividends":
                icon_id = R.drawable.dividends_light;
                break;
            case "Investments":
                icon_id = R.drawable.investments_light;
                break;
            case "Others":
                icon_id = R.drawable.others_light;
                break;

        }

        return icon_id;
    }

    private String convertToFullName(String month) {

        switch (month) {

            case "Jan":
                month = "January";
                break;

            case "Feb":
                month = "February";
                break;

            case "Mar":
                month = "March";
                break;

            case "Apr":
                month = "April";
                break;

            case "May":
                month = "May";
                break;

            case "June":
                month = "June";
                break;

            case "July":
                month = "July";
                break;

            case "Aug":
                month = "August";
                break;

            case "Sep":
                month = "September";
                break;

            case "Oct":
                month = "October";
                break;

            case "Nov":
                month = "November";
                break;

            case "Dec":
                month = "December";
                break;

        }

        return month;
    }

    private String findMonth(int currentMonth) {

        String month = "";

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

        return month;
    }

    private String putComma(String amount) {

        char[] amt;
        int flag = 0, i, pos = 0;
        String new_amount = "";
        StringBuilder amountBuilder = new StringBuilder(new_amount);

        if (amount.contains(".")) {

            amt = amount.toCharArray();

            for (i = 0; i < amt.length; i++) {

                if (amt[i] == '.') {

                    pos = i;
                    break;

                } else {

                    amountBuilder.append(amt[i]);

                }

            }

            new_amount = amountBuilder.toString();
            amountBuilder = new StringBuilder();

            if (new_amount.length() >= 4) {

                amt = new_amount.toCharArray();
                char[] new_amt = amount.toCharArray();

                for (i = amt.length - 1; i >= 0; i--) {

                    if (flag < 3) {

                        amountBuilder.append(amt[i]);
                        flag++;

                    } else {

                        amountBuilder.append(',');
                        amountBuilder.append(amt[i]);
                        flag = 1;

                    }

                }

                amountBuilder.reverse();

                for (i = pos; i < new_amt.length; i++) {

                    amountBuilder.append(new_amt[i]);

                }

                new_amount = amountBuilder.toString();

            } else {

                new_amount = amount;

            }

        } else {

            amt = amount.toCharArray();

            for (i = amt.length - 1; i >= 0; i--) {

                if (flag < 3) {

                    amountBuilder.append(amt[i]);
                    flag++;

                } else {

                    amountBuilder.append(",");
                    amountBuilder.append(amt[i]);
                    flag = 1;

                }

            }

            amountBuilder.reverse();
            new_amount = amountBuilder.toString();

        }

        return new_amount;
    }

    private void changeMonthNameIfMonthOrYearIsNotSame(String month_info) {

        natureOfEarningMonthTextView.setText(month_info);
        earningCurrentMonthTextView.setText(month_info);
    }

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        showMoreButton.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        amountList = new ArrayList<>();
        beiAmount = new BEIAmount();
        Calendar calendar = Calendar.getInstance();
        colors = new int[] {
                getResources().getColor(R.color.indigo),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.light_blue),
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.purple)
        };
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        current_month = findMonth(currentMonth);
        currentYear = calendar.get(Calendar.YEAR);
        earningAmountTextView = view.findViewById(R.id.earningAmountTextView);
        earningCategoryPieChart = view.findViewById(R.id.earningCategoryPieChart);
        earningCurrentMonthTextView = view.findViewById(R.id.earningCurrentMonthTextView);
        incomeItemList = new ArrayList<>();
        incomeItemRecyclerView = view.findViewById(R.id.incomeItemRecyclerView);
        incomeItemRecyclerView.setHasFixedSize(true);
        incomeItemRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        indexList = new ArrayList<>();
        itemAmountPercentageList = new ArrayList<>();
        itemIconList = new ArrayList<>();
        itemIconList2 = new ArrayList<>();
        itemList = new ArrayList<>();
        modifiedAmountList = new ArrayList<>();
        modifiedItemList = new ArrayList<>();
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        natureOfEarningAmountTextView = view.findViewById(R.id.natureOfEarningAmountTextView);
        natureOfEarningMonthTextView = view.findViewById(R.id.natureOfEarningMonthTextView);
        newAmountList = new ArrayList<>();
        newItemList = new ArrayList<>();
        showMoreButton = view.findViewById(R.id.showMoreButton);
        topCategoriesItemColors = new int[] {
                getResources().getColor(R.color.amber),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.blue_gray),
                getResources().getColor(R.color.brown),
                getResources().getColor(R.color.cyan),
                getResources().getColor(R.color.deep_orange),
                getResources().getColor(R.color.deep_purple),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.indigo),
                getResources().getColor(R.color.light_blue),
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.lime),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.pink),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.teal),
                getResources().getColor(R.color.yellow)
        };
        topCategoriesItemAmountList = new ArrayList<>();
        topCategoriesItemIndexList = new ArrayList<>();
        topCategoriesItemList = new ArrayList<>();
        topCategoriesItemPercentageList = new ArrayList<>();
        topIncomeCategoriesRecyclerView = view.findViewById(R.id.topIncomeCategoriesRecyclerView);
        topIncomeCategoriesRecyclerView.setHasFixedSize(true);
        topIncomeCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdPreferences userIdPreferences = new UserIdPreferences(view.getContext());
        userId = userIdPreferences.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
    }

    private void sortTheIndexList(ArrayList<Integer> indexList) {

        int temp;

        for (int i = 0; i < indexList.size(); i++) {

            for (int j = i + 1; j < indexList.size(); j++) {

                if (indexList.get(i) > indexList.get(j)) {

                    temp = indexList.get(i);
                    indexList.set(i, indexList.get(j));
                    indexList.set(j, temp);

                }

            }

        }
    }

    public interface EarningFragmentListener {

        void showMonthListForEarning(String current_month, String current_year);
        void showMoreIncomeForEarning(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList);
        void showYearListForEarning(String current_month, String current_year);
    }

    public void updateTheViews(final Context context, final String month, final String year) {

        String convertedMonth;
        this.month = month;
        this.year = year;
        monthTitleTextView.setText(month);
        yearTitleTextView.setText(year);

        if (currentYear == Integer.parseInt(year)) {

            if (current_month.equals(month)) {

                changeMonthNameIfMonthOrYearIsNotSame("THIS MONTH");

            } else {

                convertedMonth = convertToFullName(month);
                changeMonthNameIfMonthOrYearIsNotSame(convertedMonth + " " + year);

            }

        } else {

            convertedMonth = convertToFullName(month);
            changeMonthNameIfMonthOrYearIsNotSame(convertedMonth + " " + year);

        }

        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);

                            if (beiAmount != null) {

                                beiAmount.setIncome(putComma(beiAmount.getIncome()));
                                income = context.getResources()
                                        .getString(R.string.rupees) + " " + beiAmount.getIncome();
                                earningAmountTextView.setText(income);
                                natureOfEarningAmountTextView.setText(income);

                            }

                        } else {

                            income = context.getResources()
                                    .getString(R.string.rupees) + " 0";
                            earningAmountTextView.setText(income);
                            natureOfEarningAmountTextView.setText(income);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        userDBReference.child(userId)
                .child("Income_Category")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            if (snapshot.getChildrenCount() == 1) {

                                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                    date  = dateSnapshot.getKey();

                                }

                                if (date != null) {

                                    amountList.clear();
                                    itemList.clear();
                                    ArrayList<String> itemCategoryList = new ArrayList<>();
                                    HashMap<String, SubItem[]> subItemHashMap = new HashMap<>();

                                    for (DataSnapshot itemSnapshot : snapshot.child(date).getChildren()) {

                                        int j = 0;
                                        String item = itemSnapshot.getKey();
                                        itemCategoryList.add(item);
                                        SubItem[] subItems = new SubItem[(int) itemSnapshot.getChildrenCount()];

                                        for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                            amountList.add(subItemSnapshot.getValue(String.class));
                                            itemList.add(subItemSnapshot.getKey());
                                            subItems[j] = new SubItem();
                                            subItems[j].setAmount(subItemSnapshot.getValue(String.class));
                                            subItems[j].setItem_name(subItemSnapshot.getKey());
                                            j++;

                                        }

                                        subItemHashMap.put(item, subItems);

                                    }

                                    if (amountList.size() == 1 && itemList.size() == 1) {

                                        amountList.set(0, context.getResources().getString(R.string.rupees) + " " + putComma(amountList.get(0)));
                                        incomeItemList.clear();
                                        itemIconList.clear();
                                        //itemIconList = addIconsToIconList(itemList);
                                        itemIconList = addIconsToIconList(itemCategoryList, subItemHashMap);
                                        earningCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                        earningCategoryPieChart.getDescription().setEnabled(false);
                                        earningCategoryPieChart.setCenterText(itemList.get(0) + "\n" + amountList.get(0) + "\n" + "100%");
                                        earningCategoryPieChart.setDrawEntryLabels(false);
                                        earningCategoryPieChart.setDrawHoleEnabled(true);
                                        earningCategoryPieChart.setHoleRadius(65);
                                        earningCategoryPieChart.setRotationEnabled(false);
                                        incomeItemList.add(new PieEntry(100f, itemList.get(0)));
                                        incomePieDataSet = new PieDataSet(incomeItemList, "");
                                        incomePieDataSet.setColors(colors);
                                        incomePieDataSet.setDrawValues(false);
                                        incomePieData = new PieData(incomePieDataSet);
                                        earningCategoryPieChart.setData(incomePieData);
                                        incomePieData.setValueTextColor(Color.WHITE);
                                        incomePieData.setValueTextSize(10f);
                                        showMoreButton.setVisibility(View.GONE);
                                        top5IncomeRecyclerAdapter = new Top5IncomeRecyclerAdapter(
                                                itemIconList,
                                                amountList,
                                                itemList,
                                                context,
                                                colors
                                        );
                                        topCategoriesItemAmountList.clear();
                                        topCategoriesItemAmountList.add(amountList.get(0));
                                        topCategoriesItemList.clear();
                                        topCategoriesItemList.add(itemList.get(0));
                                        topCategoriesItemPercentageList.clear();
                                        topCategoriesItemPercentageList.add(100);

                                    } else {

                                        modifiedAmountList.clear();
                                        modifiedItemList.clear();
                                        newAmountList = amountList;
                                        newItemList = itemList;
                                        topCategoriesItemAmountList.clear();
                                        topCategoriesItemAmountList = sortTheTopCategoriesItemAmountList(newAmountList);
                                        topCategoriesItemList.clear();
                                        topCategoriesItemPercentageList.clear();

                                        for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                            String amount = topCategoriesItemAmountList.get(i);

                                            for (int j = 0; j < newAmountList.size(); j++) {

                                                if (amount.equals(newAmountList.get(j))) {

                                                    if (topCategoriesItemIndexList.size() != 0) {

                                                        if (!topCategoriesItemIndexList.contains(j)) {

                                                            topCategoriesItemIndexList.add(j);

                                                        }

                                                    } else {

                                                        topCategoriesItemIndexList.add(j);

                                                    }

                                                }

                                            }

                                        }

                                        topCategoriesItemAmountList.clear();

                                        for (int i = 0; i < topCategoriesItemIndexList.size(); i++) {

                                            topCategoriesItemAmountList.add(newAmountList.get(topCategoriesItemIndexList.get(i)));
                                            topCategoriesItemList.add(newItemList.get(topCategoriesItemIndexList.get(i)));

                                        }

                                        BigDecimal percentage;
                                        BigDecimal total_amount = new BigDecimal(topCategoriesItemAmountList.get(0));
                                        float[] item_percentage = new float[topCategoriesItemAmountList.size()];

                                        for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                            percentage = new BigDecimal(topCategoriesItemAmountList.get(i)).divide(total_amount, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                            item_percentage[i] = Float.parseFloat(new DecimalFormat("##.##").format(percentage));

                                        }

                                        for (float f : item_percentage) {

                                            topCategoriesItemPercentageList.add((int) f);

                                        }

                                        for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                            topCategoriesItemAmountList.set(i, context.getResources().getString(R.string.rupees) + " " + putComma(topCategoriesItemAmountList.get(i)));

                                        }

                                        if (amountList.size() > 5 && itemList.size() > 5) {

                                            modifiedAmountList = makeAListOfTop5Categories(newAmountList);

                                            for (int i = 0; i < modifiedAmountList.size(); i++) {

                                                String amount = modifiedAmountList.get(i);

                                                for (int j = 0; j < newAmountList.size(); j++) {

                                                    if (amount.equals(newAmountList.get(j))) {

                                                        if (indexList.size() != 0) {

                                                            if (indexList.size() == 5) {

                                                                break;

                                                            } else {

                                                                if (!indexList.contains(j)) {

                                                                    indexList.add(j);

                                                                }

                                                            }

                                                        } else {

                                                            indexList.add(j);

                                                        }

                                                    }

                                                }

                                            }

                                            modifiedAmountList.clear();

                                            for (int i = 0; i < indexList.size(); i++) {

                                                modifiedAmountList.add(newAmountList.get(indexList.get(i)));
                                                modifiedItemList.add(newItemList.get(indexList.get(i)));

                                            }

                                        } else {

                                            modifiedAmountList = newAmountList;
                                            modifiedItemList = newItemList;
                                            showMoreButton.setVisibility(View.GONE);

                                        }

                                        BigDecimal amount_percentage;
                                        BigDecimal total_expense_amount = new BigDecimal(0);
                                        final float[] item_amount_percentage = new float[modifiedAmountList.size()];

                                        for (int i = 0; i < amountList.size(); i++) {

                                            total_expense_amount = total_expense_amount.add(new BigDecimal(amountList.get(i)));

                                        }

                                        for (int i = 0; i < modifiedAmountList.size(); i++) {

                                            amount_percentage = new BigDecimal(modifiedAmountList.get(i)).divide(total_expense_amount, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                            item_amount_percentage[i] = Float.parseFloat(new DecimalFormat("##.##").format(amount_percentage));

                                        }

                                        itemAmountPercentageList.clear();

                                        for (float p : item_amount_percentage) {

                                            itemAmountPercentageList.add(p);

                                        }

                                        for (int i = 0; i < modifiedAmountList.size(); i++) {

                                            modifiedAmountList.set(i, context.getResources().getString(R.string.rupees) + " " + putComma(modifiedAmountList.get(i)));

                                        }

                                        earningCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                        earningCategoryPieChart.getDescription().setEnabled(false);
                                        earningCategoryPieChart.getLegend().setWordWrapEnabled(true);
                                        earningCategoryPieChart.setCenterText("All\n" + month + "\n" + context.getResources().getString(R.string.rupees) + putComma(String.valueOf(total_expense_amount)));
                                        earningCategoryPieChart.setDrawEntryLabels(false);
                                        earningCategoryPieChart.setDrawHoleEnabled(true);
                                        earningCategoryPieChart.setHoleRadius(65);
                                        earningCategoryPieChart.setRotationEnabled(false);
                                        incomeItemList.clear();

                                        for (int i = 0; i < modifiedItemList.size(); i++) {

                                            incomeItemList.add(new PieEntry(item_amount_percentage[i], modifiedItemList.get(i)));

                                        }

                                        incomePieDataSet = new PieDataSet(incomeItemList, "");
                                        incomePieDataSet.setColors(colors);
                                        incomePieDataSet.setDrawValues(false);
                                        incomePieData = new PieData(incomePieDataSet);
                                        earningCategoryPieChart.setData(incomePieData);
                                        incomePieData.setValueTextColor(Color.WHITE);
                                        incomePieData.setValueTextSize(10f);
                                        final BigDecimal finalTotal_expense_amount = total_expense_amount;
                                        earningCategoryPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                            @Override
                                            public void onValueSelected(Entry e, Highlight h) {

                                                int index = (int) h.getX();
                                                earningCategoryPieChart.setCenterText(modifiedItemList.get(index) + "\n" + modifiedAmountList.get(index) + "\n" + itemAmountPercentageList.get(index) + "%");
                                            }

                                            @Override
                                            public void onNothingSelected() {

                                                earningCategoryPieChart.setCenterText("All\n" + month + "\n" + context.getResources().getString(R.string.rupees) + " " + putComma(finalTotal_expense_amount.toString()));
                                            }
                                        });
                                        itemIconList.clear();
                                        itemIconList2.clear();
                                        itemIconList = addIconsToIconList(
                                                modifiedItemList,
                                                itemCategoryList,
                                                subItemHashMap
                                        );
                                        itemIconList2 = addIconsToIconList2(
                                                newItemList,
                                                itemCategoryList,
                                                subItemHashMap
                                        );
                                        top5IncomeRecyclerAdapter = new Top5IncomeRecyclerAdapter(
                                                itemIconList,
                                                modifiedAmountList,
                                                modifiedItemList,
                                                context,
                                                colors
                                        );

                                    }

                                    incomeItemRecyclerView.setAdapter(top5IncomeRecyclerAdapter);
                                    topIncomeCategoriesRecyclerAdapter = new TopIncomeCategoriesRecyclerAdapter(
                                            topCategoriesItemPercentageList,
                                            topCategoriesItemAmountList,
                                            topCategoriesItemList,
                                            context,
                                            topCategoriesItemColors
                                    );
                                    topIncomeCategoriesRecyclerView.setAdapter(topIncomeCategoriesRecyclerAdapter);

                                }


                            } else {

                                amountList.clear();
                                boolean alreadyAdded;
                                final ArrayList<String> dateList = new ArrayList<>();
                                dateList.clear();
                                HashMap<String, String> itemHashMap = new HashMap<>();
                                itemHashMap.clear();
                                itemList.clear();
                                newAmountList.clear();
                                newItemList.clear();
                                String item;
                                topCategoriesItemAmountList.clear();
                                topCategoriesItemList.clear();
                                topCategoriesItemPercentageList.clear();

                                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {

                                    dateList.add(dateSnapshot.getKey());

                                }

                                for (int i = 0; i < dateList.size(); i++) {

                                    for (DataSnapshot itemSnapshot : snapshot.child(dateList.get(i)).getChildren()) {

                                        for (DataSnapshot subItemSnapshot : itemSnapshot.getChildren()) {

                                            amountList.add(subItemSnapshot.getValue(String.class));
                                            itemList.add(subItemSnapshot.getKey());

                                        }

                                    }

                                }

                                for (int i = 0; i < itemList.size(); i++) {

                                    alreadyAdded = false;
                                    BigDecimal amount = new BigDecimal(0);
                                    item = itemList.get(i);

                                    for (int j = 0; j < itemList.size(); j++) {

                                        if (!itemHashMap.isEmpty()) {

                                            if (itemHashMap.containsKey(item)) {

                                                alreadyAdded = true;
                                                break;

                                            } else {

                                                if (item.equals(itemList.get(j))) {

                                                    amount = amount.add(new BigDecimal(amountList.get(j)));

                                                }

                                            }

                                        } else {

                                            if (item.equals(itemList.get(j))) {

                                                amount = amount.add(new BigDecimal(amountList.get(j)));

                                            }

                                        }

                                    }

                                    if (!alreadyAdded) {

                                        itemHashMap.put(item, "Added");
                                        newAmountList.add(amount.toString());
                                        newItemList.add(item);

                                    }

                                }

                                indexList.clear();
                                topCategoriesItemAmountList = sortTheTopCategoriesItemAmountList(newAmountList);

                                for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                    String amount = topCategoriesItemAmountList.get(i);

                                    for (int j = 0; j < newAmountList.size(); j++) {

                                        if (amount.equals(newAmountList.get(j))) {

                                            if (topCategoriesItemIndexList.size() != 0) {

                                                if (!topCategoriesItemIndexList.contains(j)) {

                                                    topCategoriesItemIndexList.add(j);

                                                }

                                            } else {

                                                topCategoriesItemIndexList.add(j);

                                            }

                                        }

                                    }

                                }

                                topCategoriesItemAmountList.clear();

                                for (int i = 0; i < topCategoriesItemIndexList.size(); i++) {

                                    topCategoriesItemAmountList.add(newAmountList.get(topCategoriesItemIndexList.get(i)));
                                    topCategoriesItemList.add(newItemList.get(topCategoriesItemIndexList.get(i)));

                                }

                                BigDecimal percentage;
                                BigDecimal total_amount = new BigDecimal(topCategoriesItemAmountList.get(0));
                                float[] item_percentage = new float[topCategoriesItemAmountList.size()];

                                for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                    percentage = new BigDecimal(topCategoriesItemAmountList.get(i)).divide(total_amount, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                    item_percentage[i] = Float.parseFloat(new DecimalFormat("##.##").format(percentage));

                                }

                                for (float f : item_percentage) {

                                    topCategoriesItemPercentageList.add((int) f);

                                }

                                for (int i = 0; i < topCategoriesItemAmountList.size(); i++) {

                                    topCategoriesItemAmountList.set(i, context.getResources().getString(R.string.rupees) + " " + putComma(topCategoriesItemAmountList.get(i)));

                                }

                                if (newAmountList.size() > 5 && newItemList.size() > 5) {

                                    modifiedAmountList = makeAListOfTop5Categories(newAmountList);

                                    for (int i = 0; i < modifiedAmountList.size(); i++) {

                                        String amount = modifiedAmountList.get(i);

                                        for (int j = 0; j < newAmountList.size(); j++) {

                                            if (amount.equals(newAmountList.get(j))) {

                                                if (indexList.size() != 0) {

                                                    if (indexList.size() == 5) {

                                                        break;

                                                    } else {

                                                        if (!indexList.contains(j)) {

                                                            indexList.add(j);

                                                        }

                                                    }

                                                } else {

                                                    indexList.add(j);

                                                }

                                            }

                                        }

                                    }

                                    modifiedAmountList.clear();
                                    modifiedItemList.clear();
                                    sortTheIndexList(indexList);

                                    for (int i = 0; i < indexList.size(); i++) {

                                        modifiedAmountList.add(newAmountList.get(indexList.get(i)));
                                        modifiedItemList.add(newItemList.get(indexList.get(i)));

                                    }

                                } else {

                                    modifiedAmountList = newAmountList;
                                    modifiedItemList = newItemList;
                                    showMoreButton.setVisibility(View.GONE);

                                }

                                BigDecimal amount_percentage;
                                BigDecimal total_expense_amount = new BigDecimal(0);
                                final float[] item_amount_percentage = new float[modifiedAmountList.size()];

                                for (int i = 0; i < newAmountList.size(); i++) {

                                    total_expense_amount = total_expense_amount.add(new BigDecimal(newAmountList.get(i)));

                                }

                                for (int i = 0; i < modifiedAmountList.size(); i++) {

                                    amount_percentage = new BigDecimal(modifiedAmountList.get(i)).divide(total_expense_amount, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
                                    item_amount_percentage[i] = Float.parseFloat(new DecimalFormat("##.##").format(amount_percentage));

                                }

                                itemAmountPercentageList.clear();

                                for (float p : item_amount_percentage) {

                                    itemAmountPercentageList.add(p);

                                }

                                for (int i = 0; i < modifiedAmountList.size(); i++) {

                                    modifiedAmountList.set(i, context.getResources().getString(R.string.rupees) + " " + putComma(modifiedAmountList.get(i)));

                                }

                                earningCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                earningCategoryPieChart.getDescription().setEnabled(false);
                                earningCategoryPieChart.getLegend().setWordWrapEnabled(true);
                                earningCategoryPieChart.setCenterText("All\n" + month + "\n"  + context.getResources().getString(R.string.rupees) + putComma(String.valueOf(total_expense_amount)));
                                earningCategoryPieChart.setDrawEntryLabels(false);
                                earningCategoryPieChart.setDrawHoleEnabled(true);
                                earningCategoryPieChart.setHoleRadius(65);
                                earningCategoryPieChart.setRotationEnabled(false);
                                incomeItemList.clear();

                                for (int i = 0; i < modifiedItemList.size(); i++) {

                                    incomeItemList.add(new PieEntry(item_amount_percentage[i], modifiedItemList.get(i)));

                                }

                                incomePieDataSet = new PieDataSet(incomeItemList, "");
                                incomePieDataSet.setColors(colors);
                                incomePieDataSet.setDrawValues(false);
                                incomePieData = new PieData(incomePieDataSet);
                                earningCategoryPieChart.setData(incomePieData);
                                incomePieData.setValueTextColor(Color.WHITE);
                                incomePieData.setValueTextSize(10f);
                                final BigDecimal finalTotal_expense_amount = total_expense_amount;
                                earningCategoryPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {

                                        int index = (int) h.getX();
                                        earningCategoryPieChart.setCenterText(modifiedItemList.get(index) + "\n" + modifiedAmountList.get(index) + "\n" + itemAmountPercentageList.get(index) + "%");
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                        earningCategoryPieChart.setCenterText("All\n" + month + "\n"  + context.getResources().getString(R.string.rupees) + " " + putComma(finalTotal_expense_amount.toString()));
                                    }
                                });
                                itemIconList.clear();
                                itemIconList2.clear();
                                itemIconList = addIconsToIconList(modifiedItemList.size());
                                itemIconList2 = addIconsToIconList(newItemList.size());
                                top5IncomeRecyclerAdapter = new Top5IncomeRecyclerAdapter(
                                        itemIconList,
                                        modifiedAmountList,
                                        modifiedItemList,
                                        context,
                                        colors
                                );
                                incomeItemRecyclerView.setAdapter(top5IncomeRecyclerAdapter);
                                topIncomeCategoriesRecyclerAdapter = new TopIncomeCategoriesRecyclerAdapter(
                                        topCategoriesItemPercentageList,
                                        topCategoriesItemAmountList,
                                        topCategoriesItemList,
                                        context,
                                        topCategoriesItemColors
                                );
                                topIncomeCategoriesRecyclerView.setAdapter(topIncomeCategoriesRecyclerAdapter);

                            }

                        } else {

                            earningCategoryPieChart.setData(null);
                            earningCategoryPieChart.invalidate();
                            itemIconList.clear();
                            modifiedAmountList.clear();
                            modifiedItemList.clear();
                            showMoreButton.setVisibility(View.GONE);
                            top5IncomeRecyclerAdapter = new Top5IncomeRecyclerAdapter(
                                    itemIconList,
                                    modifiedAmountList,
                                    modifiedItemList,
                                    context,
                                    colors
                            );
                            incomeItemRecyclerView.setAdapter(top5IncomeRecyclerAdapter);
                            topCategoriesItemAmountList.clear();
                            topCategoriesItemList.clear();
                            topCategoriesItemPercentageList.clear();
                            topIncomeCategoriesRecyclerAdapter = new TopIncomeCategoriesRecyclerAdapter(
                                    topCategoriesItemPercentageList,
                                    topCategoriesItemAmountList,
                                    topCategoriesItemList,
                                    context,
                                    topCategoriesItemColors
                            );
                            topIncomeCategoriesRecyclerView.setAdapter(topIncomeCategoriesRecyclerAdapter);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earning, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(view.getContext(), month, year);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            earningFragmentListener = (EarningFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement EarningFragmentListener!");

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.monthListCardView) {

            earningFragmentListener.showMonthListForEarning(month, year);

        } else if (id == R.id.showMoreButton) {

            earningFragmentListener.showMoreIncomeForEarning(itemIconList2, newAmountList, newItemList);

        } else if (id == R.id.yearListCardView) {

            earningFragmentListener.showYearListForEarning(month, year);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        earningFragmentListener = null;
    }
}
