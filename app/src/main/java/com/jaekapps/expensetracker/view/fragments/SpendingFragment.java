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
import com.jaekapps.expensetracker.view.adapters.Top5ExpensesRecyclerAdapter;
import com.jaekapps.expensetracker.view.adapters.TopExpenseCategoriesRecyclerAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SpendingFragment extends Fragment implements View.OnClickListener {

    int currentMonth;
    private AppCompatButton showMoreButton;
    private ArrayList<Float> itemAmountPercentageList;
    private ArrayList<Integer> indexList, itemIconList, itemIconList2, topCategoriesItemIndexList, topCategoriesItemPercentageList;
    private ArrayList<PieEntry> expenseItemList;
    private ArrayList<String> amountList, itemList, modifiedAmountList, modifiedItemList, newAmountList, newItemList,
            topCategoriesItemAmountList, topCategoriesItemList;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private DatabaseReference userDBReference;
    private int currentYear;
    private int[] colors, topCategoriesItemColors;
    private PieChart spendingCategoryPieChart;
    private PieData expensesPieData;
    private PieDataSet expensesPieDataSet;
    private RecyclerView expenseItemRecyclerView, topExpenseCategoriesRecyclerView;
    private SpendingFragmentListener spendingFragmentListener;
    private String current_month, date, expense, month, year, userId;
    private TextView monthTitleTextView, natureOfSpendingAmountTextView, natureOfSpendingMonthTextView, spendingAmountTextView,
            spendingCurrentMonthTextView, yearTitleTextView;
    private Top5ExpensesRecyclerAdapter top5ExpensesRecyclerAdapter;
    private TopExpenseCategoriesRecyclerAdapter topExpenseCategoriesRecyclerAdapter;

    SpendingFragment(String month, String year) {

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

            case "Food":
                icon_id = R.drawable.food_light;
                break;
            case "Bills":
                icon_id = R.drawable.bills_light;
                break;
            case "Transportation":
                icon_id = R.drawable.transportation_light;
                break;
            case "Home":
                icon_id = R.drawable.home_light;
                break;
            case "Car":
                icon_id = R.drawable.car_light;
                break;
            case "Entertainment":
                icon_id = R.drawable.entertainment_light;
                break;
            case "Shopping":
                icon_id = R.drawable.shopping_light;
                break;
            case "Clothing":
                icon_id = R.drawable.cloth_light;
                break;
            case "Insurance":
                icon_id = R.drawable.insurance_light;
                break;
            case "Tax":
                icon_id = R.drawable.tax_light;
                break;
            case "Telephone":
                icon_id = R.drawable.phone_light;
                break;
            case "Cigarette":
                icon_id = R.drawable.cigarette_light;
                break;
            case "Health":
                icon_id = R.drawable.health_light;
                break;
            case "Sports":
                icon_id = R.drawable.sports_light;
                break;
            case "Baby":
                icon_id = R.drawable.baby_light;
                break;
            case "Pet":
                icon_id = R.drawable.pet_light;
                break;
            case "Beauty":
                icon_id = R.drawable.beauty_light;
                break;
            case "Electronics":
                icon_id = R.drawable.electronics_light;
                break;
            case "Wine":
                icon_id = R.drawable.wine_light;
                break;
            case "Vegetables":
                icon_id = R.drawable.vegetables_light;
                break;
            case "Gift":
                icon_id = R.drawable.gift_light;
                break;
            case "Social":
                icon_id = R.drawable.social_light;
                break;
            case "Travel":
                icon_id = R.drawable.travel_light;
                break;
            case "Education":
                icon_id = R.drawable.education_light;
                break;
            case "Book":
                icon_id = R.drawable.book_light;
                break;
            case "Office":
                icon_id = R.drawable.office_light;
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

        natureOfSpendingMonthTextView.setText(month_info);
        spendingCurrentMonthTextView.setText(month_info);
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
                getResources().getColor(R.color.amber),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.deep_orange),
                getResources().getColor(R.color.deep_purple),
                getResources().getColor(R.color.green),
        };
        currentMonth = calendar.get(Calendar.MONTH);
        currentMonth = currentMonth + 1;
        current_month = findMonth(currentMonth);
        currentYear = calendar.get(Calendar.YEAR);
        expenseItemList = new ArrayList<>();
        expenseItemRecyclerView = view.findViewById(R.id.expenseItemRecyclerView);
        expenseItemRecyclerView.setHasFixedSize(true);
        expenseItemRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        indexList = new ArrayList<>();
        itemAmountPercentageList = new ArrayList<>();
        itemIconList = new ArrayList<>();
        itemIconList2 = new ArrayList<>();
        itemList = new ArrayList<>();
        modifiedAmountList = new ArrayList<>();
        modifiedItemList = new ArrayList<>();
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        natureOfSpendingAmountTextView = view.findViewById(R.id.natureOfSpendingAmountTextView);
        natureOfSpendingMonthTextView = view.findViewById(R.id.natureOfSpendingMonthTextView);
        newAmountList = new ArrayList<>();
        newItemList = new ArrayList<>();
        showMoreButton = view.findViewById(R.id.showMoreButton);
        spendingAmountTextView = view.findViewById(R.id.spendingAmountTextView);
        spendingCategoryPieChart = view.findViewById(R.id.spendingCategoryPieChart);
        spendingCurrentMonthTextView = view.findViewById(R.id.spendingCurrentMonthTextView);
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
        topExpenseCategoriesRecyclerView = view.findViewById(R.id.topExpenseCategoriesRecyclerView);
        topExpenseCategoriesRecyclerView.setHasFixedSize(true);
        topExpenseCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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

    public interface SpendingFragmentListener {

        void showMonthListForSpending(String current_month, String current_year);
        void showMoreExpensesForSpending(ArrayList<Integer> itemIconList, ArrayList<String> amountList, ArrayList<String> itemList);
        void showYearListForSpending(String current_month, String current_year);
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

                                beiAmount.setExpense(putComma(beiAmount.getExpense()));
                                expense = context.getResources()
                                        .getString(R.string.rupees) + " " + beiAmount.getExpense();
                                natureOfSpendingAmountTextView.setText(expense);
                                spendingAmountTextView.setText(expense);

                            }

                        } else {

                            expense = context.getResources()
                                    .getString(R.string.rupees) + " 0";
                            natureOfSpendingAmountTextView.setText(expense);
                            spendingAmountTextView.setText(expense);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        userDBReference.child(userId)
                .child("Expense_Category")
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
                                        expenseItemList.clear();
                                        itemIconList.clear();
                                        itemIconList = addIconsToIconList(itemCategoryList, subItemHashMap);
                                        showMoreButton.setVisibility(View.GONE);
                                        spendingCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                        spendingCategoryPieChart.getDescription().setEnabled(false);
                                        spendingCategoryPieChart.setCenterText(itemList.get(0) + "\n" + amountList.get(0) + "\n" + "100%");
                                        spendingCategoryPieChart.setDrawEntryLabels(false);
                                        spendingCategoryPieChart.setDrawHoleEnabled(true);
                                        spendingCategoryPieChart.setHoleRadius(65);
                                        spendingCategoryPieChart.setRotationEnabled(false);
                                        expenseItemList.add(new PieEntry(100f, itemList.get(0)));
                                        expensesPieDataSet = new PieDataSet(expenseItemList, "");
                                        expensesPieDataSet.setColors(colors);
                                        expensesPieDataSet.setDrawValues(false);
                                        expensesPieData = new PieData(expensesPieDataSet);
                                        spendingCategoryPieChart.setData(expensesPieData);
                                        expensesPieData.setValueTextColor(Color.WHITE);
                                        expensesPieData.setValueTextSize(10f);
                                        top5ExpensesRecyclerAdapter = new Top5ExpensesRecyclerAdapter(
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

                                        spendingCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                        spendingCategoryPieChart.getDescription().setEnabled(false);
                                        spendingCategoryPieChart.getLegend().setWordWrapEnabled(true);
                                        spendingCategoryPieChart.setCenterText("All\n" + month + "\n" + context.getResources().getString(R.string.rupees) + putComma(String.valueOf(total_expense_amount)));
                                        spendingCategoryPieChart.setDrawEntryLabels(false);
                                        spendingCategoryPieChart.setDrawHoleEnabled(true);
                                        spendingCategoryPieChart.setHoleRadius(65);
                                        spendingCategoryPieChart.setRotationEnabled(false);
                                        expenseItemList.clear();

                                        for (int i = 0; i < modifiedItemList.size(); i++) {

                                            expenseItemList.add(new PieEntry(item_amount_percentage[i], modifiedItemList.get(i)));

                                        }

                                        expensesPieDataSet = new PieDataSet(expenseItemList, "");
                                        expensesPieDataSet.setColors(colors);
                                        expensesPieDataSet.setDrawValues(false);
                                        expensesPieData = new PieData(expensesPieDataSet);
                                        spendingCategoryPieChart.setData(expensesPieData);
                                        expensesPieData.setValueTextColor(Color.WHITE);
                                        expensesPieData.setValueTextSize(10f);
                                        final BigDecimal finalTotal_expense_amount = total_expense_amount;
                                        spendingCategoryPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                            @Override
                                            public void onValueSelected(Entry e, Highlight h) {

                                                int index = (int) h.getX();
                                                spendingCategoryPieChart.setCenterText(modifiedItemList.get(index) + "\n" + modifiedAmountList.get(index) + "\n" + itemAmountPercentageList.get(index) + "%");
                                            }

                                            @Override
                                            public void onNothingSelected() {

                                                spendingCategoryPieChart.setCenterText("All\n" + month + "\n" + context.getResources().getString(R.string.rupees) + " " + putComma(finalTotal_expense_amount.toString()));
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
                                        top5ExpensesRecyclerAdapter = new Top5ExpensesRecyclerAdapter(
                                                itemIconList,
                                                modifiedAmountList,
                                                modifiedItemList,
                                                context,
                                                colors
                                        );

                                    }

                                    expenseItemRecyclerView.setAdapter(top5ExpensesRecyclerAdapter);
                                    topExpenseCategoriesRecyclerAdapter = new TopExpenseCategoriesRecyclerAdapter(
                                            topCategoriesItemPercentageList,
                                            topCategoriesItemAmountList,
                                            topCategoriesItemList,
                                            context,
                                            topCategoriesItemColors
                                    );
                                    topExpenseCategoriesRecyclerView.setAdapter(topExpenseCategoriesRecyclerAdapter);

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
                                final float[] item_amount_percentage = new float[newAmountList.size()];

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

                                spendingCategoryPieChart.animateY(1000, Easing.EaseInOutCubic);
                                spendingCategoryPieChart.getDescription().setEnabled(false);
                                spendingCategoryPieChart.getLegend().setWordWrapEnabled(true);
                                spendingCategoryPieChart.setCenterText("All\n" + month + "\n"  + context.getResources().getString(R.string.rupees) + " " + putComma(String.valueOf(total_expense_amount)));
                                spendingCategoryPieChart.setDrawEntryLabels(false);
                                spendingCategoryPieChart.setDrawHoleEnabled(true);
                                spendingCategoryPieChart.setHoleRadius(65);
                                spendingCategoryPieChart.setRotationEnabled(false);
                                expenseItemList.clear();

                                for (int i = 0; i < modifiedItemList.size(); i++) {

                                    expenseItemList.add(new PieEntry(item_amount_percentage[i], modifiedItemList.get(i)));

                                }

                                expensesPieDataSet = new PieDataSet(expenseItemList, "");
                                expensesPieDataSet.setColors(colors);
                                expensesPieDataSet.setDrawValues(false);
                                expensesPieData = new PieData(expensesPieDataSet);
                                spendingCategoryPieChart.setData(expensesPieData);
                                expensesPieData.setValueTextColor(Color.WHITE);
                                expensesPieData.setValueTextSize(10f);
                                final BigDecimal finalTotal_expense_amount = total_expense_amount;
                                spendingCategoryPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {

                                        int index = (int) h.getX();
                                        spendingCategoryPieChart.setCenterText(modifiedItemList.get(index) + "\n" + modifiedAmountList.get(index) + "\n" + itemAmountPercentageList.get(index) + "%");
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                        spendingCategoryPieChart.setCenterText("All\n" + month + "\n"  + context.getResources().getString(R.string.rupees) + " " + putComma(finalTotal_expense_amount.toString()));
                                    }
                                });
                                itemIconList.clear();
                                itemIconList2.clear();
                                itemIconList = addIconsToIconList(modifiedItemList.size());
                                itemIconList2 = addIconsToIconList(newItemList.size());
                                top5ExpensesRecyclerAdapter = new Top5ExpensesRecyclerAdapter(
                                        itemIconList,
                                        modifiedAmountList,
                                        modifiedItemList,
                                        context,
                                        colors
                                );
                                expenseItemRecyclerView.setAdapter(top5ExpensesRecyclerAdapter);
                                topExpenseCategoriesRecyclerAdapter = new TopExpenseCategoriesRecyclerAdapter(
                                        topCategoriesItemPercentageList,
                                        topCategoriesItemAmountList,
                                        topCategoriesItemList,
                                        context,
                                        topCategoriesItemColors
                                );
                                topExpenseCategoriesRecyclerView.setAdapter(topExpenseCategoriesRecyclerAdapter);

                            }

                        } else {

                            itemIconList.clear();
                            modifiedAmountList.clear();
                            modifiedItemList.clear();
                            showMoreButton.setVisibility(View.GONE);
                            spendingCategoryPieChart.setData(null);
                            spendingCategoryPieChart.invalidate();
                            top5ExpensesRecyclerAdapter = new Top5ExpensesRecyclerAdapter(
                                    itemIconList,
                                    modifiedAmountList,
                                    modifiedItemList,
                                    context,
                                    colors
                            );
                            expenseItemRecyclerView.setAdapter(top5ExpensesRecyclerAdapter);
                            topCategoriesItemAmountList.clear();
                            topCategoriesItemList.clear();
                            topCategoriesItemPercentageList.clear();
                            topExpenseCategoriesRecyclerAdapter = new TopExpenseCategoriesRecyclerAdapter(
                                    topCategoriesItemPercentageList,
                                    topCategoriesItemAmountList,
                                    topCategoriesItemList,
                                    context,
                                    topCategoriesItemColors
                            );
                            topExpenseCategoriesRecyclerView.setAdapter(topExpenseCategoriesRecyclerAdapter);

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

        View view = inflater.inflate(R.layout.fragment_spending, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(view.getContext(), month, year);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            spendingFragmentListener = (SpendingFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement SpendingFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.monthListCardView) {

            spendingFragmentListener.showMonthListForSpending(month, year);

        } else if (id == R.id.showMoreButton) {

            spendingFragmentListener.showMoreExpensesForSpending(itemIconList2, newAmountList, newItemList);

        } else if (id == R.id.yearListCardView) {

            spendingFragmentListener.showYearListForSpending(month, year);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        spendingFragmentListener = null;
    }
}
