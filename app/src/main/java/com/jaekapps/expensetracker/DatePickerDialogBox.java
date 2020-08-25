package com.jaekapps.expensetracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class DatePickerDialogBox extends BottomSheetDialogFragment implements View.OnClickListener {

    private AppCompatButton cancelButton, okButton;
    private CardView aprCardView, augCardView, decCardView, febCardView, janCardView, julyCardView, juneCardView, leftArrowLayout,
            marCardView, mayCardView, novCardView, octCardView, rightArrowLayout, sepCardView;
    private DatePickerListener datePickerListener;
    private String chosenYear, date, month, year;
    private TextView aprTextView, augTextView, chosenYearTextView, dayAndDateTextView, decTextView, febTextView, janTextView,
            julyTextView, juneTextView, marTextView, mayTextView, novTextView, octTextView, sepTextView, yearTextView;

    DatePickerDialogBox(String chosenYear, String date, String month, String year) {

        this.chosenYear = chosenYear;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    private void initializeOnClickListener() {

        aprCardView.setOnClickListener(this);
        augCardView.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        decCardView.setOnClickListener(this);
        febCardView.setOnClickListener(this);
        janCardView.setOnClickListener(this);
        julyCardView.setOnClickListener(this);
        juneCardView.setOnClickListener(this);
        leftArrowLayout.setOnClickListener(this);
        marCardView.setOnClickListener(this);
        mayCardView.setOnClickListener(this);
        novCardView.setOnClickListener(this);
        octCardView.setOnClickListener(this);
        okButton.setOnClickListener(this);
        rightArrowLayout.setOnClickListener(this);
        sepCardView.setOnClickListener(this);
    }

    private void initializeViews(View view) {

        aprCardView = view.findViewById(R.id.aprCardView);
        aprTextView = view.findViewById(R.id.aprTextView);
        augCardView = view.findViewById(R.id.augCardView);
        augTextView = view.findViewById(R.id.augTexView);
        cancelButton = view.findViewById(R.id.cancelButton);
        chosenYearTextView = view.findViewById(R.id.chosenYearTextView);
        dayAndDateTextView = view.findViewById(R.id.dayAndDateTextView);
        decCardView = view.findViewById(R.id.decCardView);
        decTextView = view.findViewById(R.id.decTextView);
        febCardView = view.findViewById(R.id.febCardView);
        febTextView = view.findViewById(R.id.febTextView);
        janCardView = view.findViewById(R.id.janCardView);
        janTextView = view.findViewById(R.id.janTextView);
        julyCardView = view.findViewById(R.id.julyCardView);
        juneCardView = view.findViewById(R.id.juneCardView);
        julyTextView = view.findViewById(R.id.julyTextView);
        juneTextView = view.findViewById(R.id.juneTextView);
        leftArrowLayout = view.findViewById(R.id.leftArrowLayout);
        marCardView = view.findViewById(R.id.marCardView);
        marTextView = view.findViewById(R.id.marTextView);
        mayCardView = view.findViewById(R.id.mayCardView);
        mayTextView = view.findViewById(R.id.mayTextView);
        novCardView = view.findViewById(R.id.novCardView);
        novTextView = view.findViewById(R.id.novTextView);
        octCardView = view.findViewById(R.id.octCardView);
        octTextView = view.findViewById(R.id.octTextView);
        okButton = view.findViewById(R.id.okButton);
        rightArrowLayout = view.findViewById(R.id.rightArrowLayout);
        sepCardView = view.findViewById(R.id.sepCardView);
        sepTextView = view.findViewById(R.id.sepTextView);
        yearTextView = view.findViewById(R.id.yearTextView);
    }

    public interface DatePickerListener {

        void cancel();
        void chooseMonth(String month);
        void chooseNextYear();
        void choosePreviousYear();
        void ok();
    }

    public String getYear() {

        return chosenYearTextView.getText().toString();
    }

    private void setTheCardAndTextColorForMonth(String month) {

        aprCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        aprTextView.setTextColor(Color.parseColor("#000000"));
        augCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        augTextView.setTextColor(Color.parseColor("#000000"));
        decCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        decTextView.setTextColor(Color.parseColor("#000000"));
        febCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        febTextView.setTextColor(Color.parseColor("#000000"));
        janCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        janTextView.setTextColor(Color.parseColor("#000000"));
        julyCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        julyTextView.setTextColor(Color.parseColor("#000000"));
        juneCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        juneTextView.setTextColor(Color.parseColor("#000000"));
        marCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        marTextView.setTextColor(Color.parseColor("#000000"));
        mayCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        mayTextView.setTextColor(Color.parseColor("#000000"));
        novCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        novTextView.setTextColor(Color.parseColor("#000000"));
        octCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        octTextView.setTextColor(Color.parseColor("#000000"));
        sepCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        sepTextView.setTextColor(Color.parseColor("#000000"));
        highlightTheMonth(month);
    }

    public void highlightTheMonth(String month) {

        switch (month) {

            case "Apr":
                aprCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                aprTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Aug":
                augCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                augTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Dec":
                decCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                 decTextView.setTextColor(Color.parseColor("#FFFFFF"));
                 break;

            case "Feb":
                febCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                febTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Jan":
                janCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                janTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "July":
                julyCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                julyTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "June":
                juneCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                juneTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Mar":
                marCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                marTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "May":
                mayCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                mayTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Nov":
                novCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                novTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Oct":
                octCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                octTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case "Sep":
                sepCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getContext()).getTheme()));
                sepTextView.setTextColor(Color.parseColor("#FFFFFF"));
                break;

        }

    }

    public void setYear(String year) {

        chosenYearTextView.setText(year);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.date_picker_dialog_box, container, false);
        initializeViews(view);
        initializeOnClickListener();
        chosenYearTextView.setText(chosenYear);
        dayAndDateTextView.setText(date);
        yearTextView.setText(year);
        highlightTheMonth(month);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            datePickerListener = (DatePickerListener) context;

        } catch (Exception e) {

            throw new ClassCastException(context.toString() + " must implement DatePickerListener!");

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.aprCardView:
                datePickerListener.chooseMonth("Apr");
                setTheCardAndTextColorForMonth("Apr");
                break;

            case R.id.augCardView:
                datePickerListener.chooseMonth("Aug");
                setTheCardAndTextColorForMonth("Aug");
                break;

            case R.id.cancelButton:
                datePickerListener.cancel();
                break;

            case R.id.decCardView:
                datePickerListener.chooseMonth("Dec");
                setTheCardAndTextColorForMonth("Dec");
                break;

            case R.id.febCardView:
                datePickerListener.chooseMonth("Feb");
                setTheCardAndTextColorForMonth("Feb");
                break;

            case R.id.janCardView:
                datePickerListener.chooseMonth("Jan");
                setTheCardAndTextColorForMonth("Jan");
                break;

            case R.id.julyCardView:
                datePickerListener.chooseMonth("July");
                setTheCardAndTextColorForMonth("July");
                break;

            case R.id.juneCardView:
                datePickerListener.chooseMonth("June");
                setTheCardAndTextColorForMonth("June");
                break;

            case R.id.leftArrowLayout:
                datePickerListener.choosePreviousYear();
                break;

            case R.id.marCardView:
                datePickerListener.chooseMonth("Mar");
                setTheCardAndTextColorForMonth("Mar");
                break;

            case R.id.mayCardView:
                datePickerListener.chooseMonth("May");
                setTheCardAndTextColorForMonth("May");
                break;

            case R.id.novCardView:
                datePickerListener.chooseMonth("Nov");
                setTheCardAndTextColorForMonth("Nov");
                break;

            case R.id.octCardView:
                datePickerListener.chooseMonth("Oct");
                setTheCardAndTextColorForMonth("Oct");
                break;

            case R.id.okButton:
                datePickerListener.ok();
                break;

            case R.id.rightArrowLayout:
                datePickerListener.chooseNextYear();
                break;

            case R.id.sepCardView:
                datePickerListener.chooseMonth("Sep");
                setTheCardAndTextColorForMonth("Sep");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        datePickerListener = null;
    }
}
