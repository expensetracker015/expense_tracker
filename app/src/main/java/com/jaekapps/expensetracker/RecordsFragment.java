package com.jaekapps.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RecordsFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Integer> itemIconList;
    private ArrayList<String> itemAmountList, itemDateList, itemNameList;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private DatabaseReference userDBReference;
    private final String category;
    private int[] colors;
    private LinearLayout noTransactionLayout;
    private ProgressBar loadingProgressbar;
    private RecordsFragmentListener recordsFragmentListener;
    private RecordsItemRecyclerAdapter recordsItemRecyclerAdapter;
    private RecyclerView recordsRecyclerView;
    private String month, userId, year;
    private TextView monthTitleTextView, recordAmountTextView, recordMonthYearTextView, yearTitleTextView;

    RecordsFragment(String category, String month, String year) {

        this.category = category;
        this.month = month;
        this.year = year;
    }

    private ArrayList<Integer> addIconsToIconList(ArrayList<String> itemList) {

        ArrayList<Integer> itemIconList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {

            itemIconList.add(findTheIcon(itemList.get(i)));

        }

        return itemIconList;
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

    private String replaceUnderscoreWithSlash(String date) {

        char[] dateArray = date.toCharArray();

        for (int i = 0; i < dateArray.length; i++) {

            if (dateArray[i] == '_') {

                dateArray[i] = '/';

            }

        }

        date = String.valueOf(dateArray);
        return date;
    }

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        beiAmount = new BEIAmount();
        colors = new int[] {
                getResources().getColor(R.color.amber, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.blue, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.blue_gray, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.brown, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.cyan, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.deep_orange, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.deep_purple, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.green, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.indigo, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.light_blue, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.light_green, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.lime, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.orange, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.pink, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.purple, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.red, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.teal, Objects.requireNonNull(getActivity()).getTheme()),
                getResources().getColor(R.color.yellow, Objects.requireNonNull(getActivity()).getTheme())
        };
        itemAmountList = new ArrayList<>();
        itemDateList = new ArrayList<>();
        itemIconList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        loadingProgressbar = view.findViewById(R.id.loadingProgressbar);
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        noTransactionLayout = view.findViewById(R.id.noTransactionLayout);
        noTransactionLayout.setVisibility(View.GONE);
        recordAmountTextView = view.findViewById(R.id.recordAmountTextView);
        recordMonthYearTextView = view.findViewById(R.id.recordMonthYearTextView);
        recordsRecyclerView = view.findViewById(R.id.recordsRecyclerView);
        recordsRecyclerView.setHasFixedSize(true);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(view.getContext());
        userId = userIdConfigActivity.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
    }

    public interface RecordsFragmentListener {

        void showMonthListForRecords(String current_month, String current_year);
        void showYearListForRecords(String current_month, String current_year);
    }

    public void updateViews(final Context context, final String month, final String year) {

        itemAmountList.clear();
        itemDateList.clear();
        itemIconList.clear();
        itemNameList.clear();
        this.month = month;
        this.year = year;
        monthTitleTextView.setText(month);
        yearTitleTextView.setText(year);
        userDBReference.child(userId)
                .child("BEIAmount")
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    @SuppressLint("SetTextI18n")
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);

                            if (beiAmount != null) {

                                if (beiAmount.getExpense().equals("0")) {

                                    beiAmount.setExpense(context.getResources().getString(R.string.rupees) + " " + putComma(beiAmount.getExpense()));

                                } else {

                                    beiAmount.setExpense("-" + context.getResources().getString(R.string.rupees) + " " + putComma(beiAmount.getExpense()));

                                }

                                recordAmountTextView.setText(beiAmount.getExpense());
                                recordMonthYearTextView.setText(month + " " + year);

                            }

                        } else {

                            recordAmountTextView.setText("");
                            recordMonthYearTextView.setText("");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("database_error", error.getMessage());
                    }
                });
        userDBReference.child(userId)
                .child(category)
                .child(year)
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            loadingProgressbar.setVisibility(View.GONE);

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                    itemAmountList.add(childSnapshot.getValue(String.class));
                                    itemDateList.add(dataSnapshot.getKey());
                                    itemNameList.add(childSnapshot.getKey());

                                }

                            }


                            itemIconList = addIconsToIconList(itemNameList);

                            for (int i = 0; i < itemAmountList.size(); i++) {

                                itemAmountList.set(i, "-" + context.getResources().getString(R.string.rupees) + " " + putComma(itemAmountList.get(i)));

                            }

                            for (int i = 0; i < itemDateList.size(); i++) {

                                itemDateList.set(i, replaceUnderscoreWithSlash(itemDateList.get(i)));

                            }

                        } else {

                            itemAmountList.clear();
                            itemDateList.clear();
                            itemIconList.clear();
                            itemNameList.clear();
                            noTransactionLayout.setVisibility(View.VISIBLE);

                        }

                        recordsItemRecyclerAdapter = new RecordsItemRecyclerAdapter(
                                itemIconList,
                                itemAmountList,
                                itemDateList,
                                itemNameList,
                                colors
                        );
                        recordsRecyclerView.setAdapter(recordsItemRecyclerAdapter);

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

        final View view = inflater.inflate(R.layout.records_fragment, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateViews(view.getContext(), month, year);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            recordsFragmentListener = (RecordsFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement RecordsFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.monthListCardView) {

            recordsFragmentListener.showMonthListForRecords(month, year);

        } else if (id == R.id.yearListCardView) {

            recordsFragmentListener.showYearListForRecords(month, year);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        recordsFragmentListener = null;
    }
}
