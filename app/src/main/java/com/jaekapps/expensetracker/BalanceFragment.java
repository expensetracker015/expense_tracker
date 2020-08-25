package com.jaekapps.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class BalanceFragment extends Fragment implements View.OnClickListener {

    private BalanceFragmentListener balanceFragmentListener;
    private BEIAmount beiAmount;
    private CardView monthListCardView, yearListCardView;
    private DatabaseReference userDBReference;
    private RoundedHorizontalProgressBar balanceByCurrenciesProgressBar, balanceProgressBar;
    private String balance, month, year, userId;
    private TextView balanceAmountTextView, balanceByCurrencyTextView, balanceProgressTextView, balanceStatusTextView;
    TextView monthTitleTextView, yearTitleTextView;

    BalanceFragment(String month, String year) {

        this.month = month;
        this.year = year;
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

                }

            }

            for (i = pos - 1; i >= 0; i--) {

                flag++;

                if (flag <= 3) {

                    amountBuilder.append(amt[i]);

                } else {

                    amountBuilder.append(",").append(amt[i]);
                    flag = 0;

                }

            }

            amountBuilder.reverse();

            for (i = pos; i < amt.length; i++) {

                amountBuilder.append(amt[i]);

            }

            new_amount = amountBuilder.toString();

        } else {

            amt = amount.toCharArray();

            for (i = amt.length - 1; i >= 0; i--) {

                flag++;

                if (flag <= 3) {

                    amountBuilder.append(amt[i]);

                } else {

                    amountBuilder.append(",").append(amt[i]);
                    flag = 0;

                }

            }

            amountBuilder.reverse();
            new_amount = amountBuilder.toString();

        }

        return new_amount;
    }

    private void initializeOnClickListener() {

        monthListCardView.setOnClickListener(this);
        yearListCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        balanceAmountTextView = view.findViewById(R.id.balanceAmountTextView);
        balanceByCurrenciesProgressBar = view.findViewById(R.id.balanceByCurrenciesProgressBar);
        balanceByCurrencyTextView = view.findViewById(R.id.balanceByCurrencyTextView);
        balanceProgressBar = view.findViewById(R.id.balanceProgressBar);
        balanceProgressTextView = view.findViewById(R.id.balanceProgressTextView);
        balanceStatusTextView = view.findViewById(R.id.balanceStatusTextView);
        balanceStatusTextView.setVisibility(View.GONE);
        beiAmount = new BEIAmount();
        monthListCardView = view.findViewById(R.id.monthListCardView);
        monthTitleTextView = view.findViewById(R.id.monthTitleTextView);
        monthTitleTextView.setText(month);
        userDBReference = FirebaseDatabase.getInstance().getReference("User");
        UserIdConfigActivity userIdConfigActivity = new UserIdConfigActivity(view.getContext());
        userId = userIdConfigActivity.getUserID();
        yearListCardView = view.findViewById(R.id.yearListCardView);
        yearTitleTextView = view.findViewById(R.id.yearTitleTextView);
        yearTitleTextView.setText(year);
    }

    public interface BalanceFragmentListener {

        void showMonthListForBalance(String current_month, String current_year);
        void showYearListForBalance(String current_month, String current_year);
    }

    public void updateTheViews(final Context context, String month, String year) {

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
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            beiAmount = snapshot.getValue(BEIAmount.class);

                            if (beiAmount != null) {

                                if (beiAmount.getBalance().contains("-")) {

                                    char[] bal = beiAmount.getBalance().toCharArray();
                                    StringBuilder builder = new StringBuilder();

                                    for (int i = 1; i < bal.length; i++) {

                                        builder.append(bal[i]);

                                    }

                                    beiAmount.setBalance(builder.toString());
                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    balance = "-" + context
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();
                                    balanceStatusTextView.setVisibility(View.VISIBLE);
                                    String status = "NEGATIVE BALANCE";
                                    balanceStatusTextView.setText(status);

                                } else {

                                    beiAmount.setBalance(putComma(beiAmount.getBalance()));
                                    balance = context
                                            .getResources()
                                            .getString(R.string.rupees) + beiAmount.getBalance();

                                }

                                balanceAmountTextView.setText(balance);
                                balanceByCurrenciesProgressBar.animateProgress(1000, 0, 100);
                                balanceByCurrencyTextView.setText(balance);
                                balanceProgressBar.animateProgress(1000, 0, 100);
                                balanceProgressTextView.setText(balance);

                            }

                        } else {

                            balance = context
                                    .getResources()
                                    .getString(R.string.rupees) + "0";
                            balanceAmountTextView.setText(balance);
                            balanceByCurrenciesProgressBar.setProgress(0);
                            balanceByCurrencyTextView.setText(balance);
                            balanceProgressBar.setProgress(0);
                            balanceProgressTextView.setText(balance);

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

        final View view = inflater.inflate(R.layout.balance_fragment, container, false);
        initializeViews(view);
        initializeOnClickListener();
        updateTheViews(
                view.getContext(),
                month,
                year
        );
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            balanceFragmentListener = (BalanceFragmentListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement BalanceFragmentListener!");

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.monthListCardView:
                balanceFragmentListener.showMonthListForBalance(month, year);
                break;

            case R.id.yearListCardView:
                balanceFragmentListener.showYearListForBalance(month, year);
                break;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        balanceFragmentListener = null;
    }
}
