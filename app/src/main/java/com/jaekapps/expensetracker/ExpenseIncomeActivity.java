package com.jaekapps.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ExpenseIncomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    BEIAmount beiAmount;
    boolean beiAmountYear, beiAmountMonth, dateFound, itemFound, monthFound, yearFound;
    CardView expenseCardView, incomeCardView;
    CategorySharedPreferences categorySharedPreferences;
    char[] amount;
    DatabaseReference beiAmountDatabaseReference, beiAmountMonthDatabaseReference, beiAmountYearDatabaseReference, categoryDatabaseReference, dateDatabaseReference, monthDatabaseReference, userDatabaseReference, userIdDatabaseReference, yearDatabaseReference;
    DatePickerDialog datePickerDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    GridLayout calcGridLayout;
    HomeScreenFragment homeScreenFragment;
    int currentDay, currentMonth, currentYear, in, length, selectedDay, selectedMonth, selectedYear;
    LinearLayout subcategoryLinearLayout;
    SignInUsingEmailConfigActivity signInUsingEmailConfigActivity;
    SignInUsingGoogleConfigActivity signInUsingGoogleConfigActivity;
    String balance, category, currentItemAmount, date, expense, income, itemName, month, previousItemAmount, text, userId;
    TextView amountTextView, itemNameTextView, labelTextView, subcategoryTextView, todaysDateTextView;

    private void addAmountToTextView(String number) {

        if (!amountTextView.getText().equals("0")) {

            text = "";
            amount = amountTextView.getText().toString().toCharArray();
            int flag = 0, operatorPos = 0, pos = 0;

            for (in = 0; in < amount.length; in++) {

                if (amount[in] == '+' || amount[in] == '-') {

                    operatorPos = in;
                    break;

                }

            }

            if (amount[operatorPos] == '+' || amount[operatorPos] == '-') {

                for (in = operatorPos + 1; in < amount.length; in++) {

                    if (amount[in] == '.') {

                        pos = in;
                        break;

                    }

                }

                if (amount[pos] == '.') {

                    for (in = pos + 1; in < amount.length; in++) {

                        flag++;

                    }

                    if (flag != 3) {

                        text = amountTextView.getText().toString() + number;
                        amountTextView.setText(text);

                    }

                } else {

                    text = amountTextView.getText() + number;
                    amountTextView.setText(text);

                }

            } else {

                for (in = 0; in < amount.length; in++) {

                    if (amount[in] == '.') {

                        pos = in;
                        break;

                    }

                }

                if (amount[pos] == '.') {

                    for (in = pos + 1; in < amount.length; in++) {

                        flag++;

                    }

                    if (flag != 3) {

                        text = amountTextView.getText() + number;
                        amountTextView.setText(text);

                    }

                } else {

                    text = amountTextView.getText() + number;
                    amountTextView.setText(text);

                }

            }

        } else if (amountTextView.getText().toString().equals("0")) {

            text = number;
            amountTextView.setText(text);

        }

    }

    private String addCurAmAndPreAm(float currentItemAmount, float previousItemAmount) {

        return  String.valueOf(currentItemAmount + previousItemAmount);

    }

    private void addOperatorToTextView(String operatorSign) {

        if (!amountTextView.getText().equals("0")) {

            text = "";
            text = amountTextView.getText().toString();
            amount = text.toCharArray();
            int pos = amount.length;
            pos = pos - 1;
            StringBuilder stringBuilder = new StringBuilder();

            if (amount[pos] == '+' || amount[pos] == '-') {

                text = "";

                for (in = 0; in <= pos; in++) {

                    stringBuilder.append(amount[in]);

                }

                text = stringBuilder.toString();
                amountTextView.setText(text);

            } else {

                int operatorPos = 0;

                for (in = 0; in < amount.length; in++) {

                    if (amount[in] == '+' || amount[in] == '-') {

                        operatorPos = in;
                        break;

                    }

                }

                if (operatorPos != in) {

                    text = amountTextView.getText() + operatorSign;
                    amountTextView.setText(text);

                } else {

                    performTheOperation(operatorPos, operatorSign);

                }

            }

        }

    }

    private void calculateTheAmountsAndModifyIt() {

        if (selectedMonth == currentMonth) {

            if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                if (expense.equals("0")) {

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(Long.parseLong(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                            expense = String.valueOf(totalExpense);
                            balance = String.valueOf(Float.parseFloat(income) - Float.parseFloat(expense));

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(Long.parseLong(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                } else if (expense.contains(".")) {

                    float totalExpense;

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                } else {

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(Long.parseLong(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        float totalExpense;

                        if (amountTextView.getText().toString().contains(".")) {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        } else {

                            long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(Long.parseLong(income) - totalExpense);
                            expense = String.valueOf(totalExpense);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                }

            } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                if (expense.equals("0")) {

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            long totalIncome = Long.parseLong(income) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Long.parseLong(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        float totalIncome;

                        if (amountTextView.getText().toString().contains(".")) {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            long totalIncome = Long.parseLong(income) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Long.parseLong(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                } else if (expense.contains(".")) {

                    float totalIncome;

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                } else {

                    if (income.equals("0")) {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            long totalIncome = Long.parseLong(income) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Long.parseLong(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else if (income.contains(".")) {

                        float totalIncome;

                        if (amountTextView.getText().toString().contains(".")) {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    } else {

                        if (amountTextView.getText().toString().contains(".")) {

                            float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                            income = String.valueOf(totalIncome);

                        } else {

                            long totalIncome = Long.parseLong(income) + Long.parseLong(amountTextView.getText().toString());
                            balance = String.valueOf(totalIncome - Long.parseLong(expense));
                            income = String.valueOf(totalIncome);

                        }

                        updateTheBEIAmount(balance, expense, income);

                    }

                }

            }

        } else {

            switch (selectedMonth) {

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

            userIdDatabaseReference = userDatabaseReference.child(userId);
            beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
            beiAmountYearDatabaseReference = beiAmountDatabaseReference.child(String.valueOf(selectedYear));
            beiAmountMonthDatabaseReference = beiAmountYearDatabaseReference.child(month);
            beiAmountMonthDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        beiAmount = dataSnapshot.getValue(BEIAmount.class);
                        balance = beiAmount.getBalance();
                        expense = beiAmount.getExpense();
                        income = beiAmount.getIncome();

                        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                            if (expense.contains(".")) {

                                if (income.contains(".")) {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    } else {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                } else {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    } else {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    }

                                }

                            } else {

                                if (income.contains(".")) {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    } else {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                } else {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalExpense = Float.parseFloat(expense) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(Float.parseFloat(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    } else {

                                        long totalExpense = Long.parseLong(expense) + Long.parseLong(amountTextView.getText().toString());
                                        balance = String.valueOf(Long.parseLong(income) - totalExpense);
                                        expense = String.valueOf(totalExpense);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                }

                            }

                        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                            if (expense.contains(".")) {

                                if (income.contains(".")) {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    } else {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                } else {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    } else {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                }

                            } else {

                                if (income.contains(".")) {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    } else {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                } else {

                                    if (amountTextView.getText().toString().contains(".")) {

                                        float totalIncome = Float.parseFloat(income) + Float.parseFloat(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Float.parseFloat(expense));
                                        income = String.valueOf(totalIncome);

                                    } else {

                                        long totalIncome = Long.parseLong(income) + Long.parseLong(amountTextView.getText().toString());
                                        balance = String.valueOf(totalIncome - Long.parseLong(expense));
                                        income = String.valueOf(totalIncome);

                                    }

                                    updateTheBEIAmount(balance, expense, income);

                                }

                            }

                        }

                    } else {

                        if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                            if (amountTextView.getText().toString().contains(".")) {

                                float totalExpense = Float.parseFloat(amountTextView.getText().toString());
                                balance = String.valueOf(0 - totalExpense);
                                expense = String.valueOf(totalExpense);
                                income = "0.0";

                            } else {

                                long totalExpense = Long.parseLong(amountTextView.getText().toString());
                                balance = String.valueOf(0 - totalExpense);
                                expense = String.valueOf(totalExpense);
                                income = "0";

                            }

                            updateTheBEIAmount(balance, expense, income);

                        } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                            if (amountTextView.getText().toString().contains(".")) {

                                float totalIncome = Float.parseFloat(amountTextView.getText().toString());
                                balance = String.valueOf(totalIncome);
                                income = String.valueOf(totalIncome);
                                expense = "0.0";

                            } else {

                                long totalIncome = Long.parseLong(amountTextView.getText().toString());
                                balance = String.valueOf(totalIncome);
                                income = String.valueOf(totalIncome);
                                expense = "0";

                            }

                            updateTheBEIAmount(balance, expense, income);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void checkIfDotIsPresentBeforeOrAfterOfOperator(char[] amount, int operatorPos) {

        if (amount[operatorPos - 1] == '.') {

            amountTextView.setText("0");
            Toast.makeText(ExpenseIncomeActivity.this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

        } else if (operatorPos == amount.length - 1) {

            amountTextView.setText("0");
            Toast.makeText(ExpenseIncomeActivity.this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

        } else if (amount[amount.length - 1] == '.') {

            amountTextView.setText("0");
            Toast.makeText(ExpenseIncomeActivity.this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

        } else {

            performTheOperation(operatorPos, "");

        }

    }

    private void performTheOperation(int operatorPos, String operatorSign) {

        StringBuilder firstNoStringBuilder = new StringBuilder();
        StringBuilder secondNoStringBuilder = new StringBuilder();

        for (in = 0; in < operatorPos; in++) {

            firstNoStringBuilder = firstNoStringBuilder.append(amount[in]);

        }

        for (in = operatorPos + 1; in < amount.length; in++) {

            secondNoStringBuilder = secondNoStringBuilder.append(amount[in]);

        }

        String firstNoString = firstNoStringBuilder.toString();
        String secondNoString = secondNoStringBuilder.toString();
        String operator = String.valueOf(amount[operatorPos]);

        if (operator.equals("+")) {

            if (firstNoString.contains(".") || secondNoString.contains(".")) {

                float firstNo = Float.parseFloat(firstNoString);
                float secondNo = Float.parseFloat(secondNoString);
                float addition = firstNo + secondNo;
                text = String.valueOf(addition) + operatorSign;

            } else {

                long firstNo = Long.parseLong(firstNoString);
                long secondNo = Long.parseLong(secondNoString);
                long addition = firstNo + secondNo;
                text = String.valueOf(addition) + operatorSign;

            }

            amountTextView.setText(text);

        } else if (operator.equals("-")) {

            if (firstNoString.contains(".") || secondNoString.contains(".")) {

                float firstNo = Float.parseFloat(firstNoString);
                float secondNo = Float.parseFloat(secondNoString);

                if (firstNo < secondNo) {

                    text = "0";
                    Toast.makeText(ExpenseIncomeActivity.this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

                } else {

                    float subtraction = firstNo - secondNo;

                    if (subtraction == 0) {

                        text = "0";

                    } else {

                        text = String.valueOf(subtraction) + operatorSign;

                    }

                }

            } else {

                long firstNo = Long.parseLong(firstNoString);
                long secondNo = Long.parseLong(secondNoString);

                if (firstNo < secondNo) {

                    text = "0";
                    Toast.makeText(ExpenseIncomeActivity.this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

                } else {

                    long subtraction = firstNo - secondNo;

                    if (subtraction == 0) {

                        text = "0";

                    } else {

                        text = String.valueOf(subtraction) + operatorSign;

                    }

                }

            }

            amountTextView.setText(text);

        }

    }

    private void updateBEIAndCategoryAmount(String balance, String category, String currentItemAmount, String date, String expense, String income, String itemName, String month, String previousItemAmount, String year) {

        String totalAmount;

        if (currentItemAmount.contains(".")) {

            if (previousItemAmount.contains(".")) {

                totalAmount = addCurAmAndPreAm(Float.parseFloat(currentItemAmount), Float.parseFloat(previousItemAmount));

            } else {

                totalAmount = addCurAmAndPreAm(Float.parseFloat(currentItemAmount), Float.parseFloat(previousItemAmount));

            }

        } else {

            if (previousItemAmount.contains(".")) {

                totalAmount = addCurAmAndPreAm(Float.parseFloat(currentItemAmount), Float.parseFloat(previousItemAmount));

            } else {

                totalAmount = String.valueOf(Long.parseLong(currentItemAmount) + Long.parseLong(previousItemAmount));

            }

        }

        Intent intent = new Intent();
        intent.putExtra("balance", balance);
        intent.putExtra("category", category);
        intent.putExtra("date", date);
        intent.putExtra("day_no", selectedDay);
        intent.putExtra("expense", expense);
        intent.putExtra("income", income);
        intent.putExtra("item_name", itemName);
        intent.putExtra("month", month);
        intent.putExtra("total_amount", totalAmount);
        intent.putExtra("year", year);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void updateTheBEIAmount(String balance, String expense, String income) {

        beiAmount.setBalance(balance);
        beiAmount.setExpense(expense);
        beiAmount.setIncome(income);

        switch (selectedMonth) {

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

        userIdDatabaseReference = userDatabaseReference.child(userId);
        beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
        beiAmountDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot yearDataSnapshot : dataSnapshot.getChildren()) {

                        if (Integer.parseInt(yearDataSnapshot.getKey()) == selectedYear) {

                            beiAmountYear = true;
                            break;

                        }

                    }

                    if (beiAmountYear) {

                        DatabaseReference beiAmountYearDatabaseReference = beiAmountDatabaseReference.child(String.valueOf(selectedYear));
                        beiAmountYearDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot monthDataSnapShot : dataSnapshot.getChildren()) {

                                        if (monthDataSnapShot.getKey().equals(month)) {

                                            beiAmountMonth = true;
                                            break;

                                        }

                                    }

                                    if (beiAmountMonth) {

                                        updateTheCategoryAmount(beiAmount.getBalance(), beiAmount.getExpense(), beiAmount.getIncome(), month);

                                    } else {

                                        updateTheCategoryAmount(beiAmount.getBalance(), beiAmount.getExpense(), beiAmount.getIncome(), month);

                                    }

                                } else {

                                    updateTheCategoryAmount(beiAmount.getBalance(), beiAmount.getExpense(), beiAmount.getIncome(), month);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {

                        updateTheCategoryAmount(beiAmount.getBalance(), beiAmount.getExpense(), beiAmount.getIncome(), month);

                    }

                } else {

                    updateTheCategoryAmount(beiAmount.getBalance(), beiAmount.getExpense(), beiAmount.getIncome(), month);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateTheCategoryAmount(String balance, String expense, String income, String month) {

        currentItemAmount = amountTextView.getText().toString();
        String category = categorySharedPreferences.readCategoryName() + "_Category";
        itemName = itemNameTextView.getText().toString();
        String date;

        if (String.valueOf(selectedDay).length() == 1) {

            date = "0" + selectedDay + "_" + selectedMonth + "_" + selectedYear;

        } else {

            date = selectedDay + "_" + selectedMonth + "_" + selectedYear;

        }

        userIdDatabaseReference = userDatabaseReference.child(userId);
        categoryDatabaseReference = userIdDatabaseReference.child(category);

        try {

            categoryDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot yearDataSnapshot : dataSnapshot.getChildren()) {

                            if (Integer.parseInt(yearDataSnapshot.getKey()) == selectedYear) {

                                yearFound = true;
                                break;

                            }

                        }

                        if (yearFound) {

                            yearDatabaseReference = categoryDatabaseReference.child(String.valueOf(selectedYear));
                            yearDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot monthDataSnapshot : dataSnapshot.getChildren()) {

                                            if (monthDataSnapshot.getKey().equals(month)) {

                                                monthFound = true;
                                                break;

                                            }

                                        }

                                        if (monthFound) {

                                            monthDatabaseReference = yearDatabaseReference.child(month);
                                            monthDatabaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.exists()) {

                                                        for (DataSnapshot dateDataSnapshot : dataSnapshot.getChildren()) {

                                                            if (dateDataSnapshot.getKey().equals(date)) {

                                                                dateFound = true;
                                                                break;

                                                            }

                                                        }

                                                        if (dateFound) {

                                                            dateDatabaseReference = monthDatabaseReference.child(date);
                                                            dateDatabaseReference.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot.exists()) {

                                                                        for (DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()) {

                                                                            if (itemDataSnapshot.getKey().equals(itemNameTextView.getText().toString())) {

                                                                                itemFound = true;
                                                                                break;

                                                                            }

                                                                        }

                                                                        if (itemFound) {

                                                                            dateDatabaseReference.child(itemNameTextView.getText().toString()).addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    if (dataSnapshot.exists()) {

                                                                                        previousItemAmount = dataSnapshot.getValue(String.class);
                                                                                        updateBEIAndCategoryAmount(balance, category, currentItemAmount, date, expense, income, itemName, month, previousItemAmount, String.valueOf(selectedYear));

                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    Log.e("error", databaseError.getMessage());

                                                                                }
                                                                            });

                                                                        } else {

                                                                            updateBEIAndCategoryAmount(beiAmount.getBalance(), category, currentItemAmount, date, beiAmount.getExpense(), beiAmount.getIncome(), itemName, month, "0", String.valueOf(selectedYear));

                                                                        }

                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    Log.e("error", databaseError.getMessage());

                                                                }
                                                            });

                                                        } else {

                                                            updateBEIAndCategoryAmount(beiAmount.getBalance(), category, currentItemAmount, date, beiAmount.getExpense(), beiAmount.getIncome(), itemName, month, "0", String.valueOf(selectedYear));

                                                        }

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    Log.e("error", databaseError.getMessage());

                                                }
                                            });

                                        } else {

                                            updateBEIAndCategoryAmount(beiAmount.getBalance(), category, currentItemAmount, date, beiAmount.getExpense(), beiAmount.getIncome(), itemName, month, "0", String.valueOf(selectedYear));

                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Log.e("error", databaseError.getMessage());

                                }
                            });

                        } else {

                            updateBEIAndCategoryAmount(beiAmount.getBalance(), category, currentItemAmount, date, beiAmount.getExpense(), beiAmount.getIncome(), itemName, month, "0", String.valueOf(selectedYear));

                        }

                    } else {

                        updateBEIAndCategoryAmount(beiAmount.getBalance(), category, currentItemAmount, date, beiAmount.getExpense(), beiAmount.getIncome(), itemName, month, "0", String.valueOf(selectedYear));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.e("error", databaseError.getMessage());

                }
            });

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                try {

                    itemNameTextView.setText(data.getStringExtra("item_name"));

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_income);

        amountTextView = findViewById(R.id.amountTextView);
        beiAmount = new BEIAmount();
        calcGridLayout = findViewById(R.id.calcGridLayout);
        Calendar calendar = Calendar.getInstance();
        categorySharedPreferences = new CategorySharedPreferences(this);
        categorySharedPreferences.writeCategoryName("");
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = DatePickerDialog.newInstance(ExpenseIncomeActivity.this, currentYear, currentMonth, currentDay);
        expenseCardView = findViewById(R.id.expenseCardView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userDatabaseReference = firebaseDatabase.getReference("User");
        homeScreenFragment = new HomeScreenFragment();
        incomeCardView = findViewById(R.id.incomeCardView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        labelTextView = findViewById(R.id.labelTextView);
        signInUsingEmailConfigActivity = new SignInUsingEmailConfigActivity(this);
        signInUsingGoogleConfigActivity = new SignInUsingGoogleConfigActivity(this);
        subcategoryLinearLayout = findViewById(R.id.subcategoryLinearLayout);
        subcategoryTextView = findViewById(R.id.subcategoryTextView);
        currentMonth = currentMonth + 1;
        date = String.valueOf(currentMonth) + "/" + String.valueOf(currentDay);
        selectedDay = currentDay;
        selectedMonth = currentMonth;
        selectedYear = currentYear;

        switch (selectedMonth) {

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

        todaysDateTextView = findViewById(R.id.todaysDateTextView);
        todaysDateTextView.setText(date);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Expense-Income");
            userId = firebaseUser.getUid();

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

        if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus() || signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

            userIdDatabaseReference = userDatabaseReference.child(userId);
            beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
            beiAmountYearDatabaseReference = beiAmountDatabaseReference.child(String.valueOf(selectedYear));
            beiAmountMonthDatabaseReference = beiAmountYearDatabaseReference.child(month);
            beiAmountMonthDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        beiAmount = dataSnapshot.getValue(BEIAmount.class);
                        balance = beiAmount.getBalance();
                        expense = beiAmount.getExpense();
                        income = beiAmount.getIncome();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        expenseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categorySharedPreferences.readCategoryName().equals("Income")) {

                    itemNameTextView.setText(getResources().getString(R.string.add_item));

                    if (!amountTextView.getText().toString().equals("0")) {

                        amountTextView.setText("0");

                    }

                }

                categorySharedPreferences.writeCategoryName("Expense");
                category = "Category - " + categorySharedPreferences.readCategoryName();
                subcategoryTextView.setText(category);

            }
        });
        incomeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                    itemNameTextView.setText(getResources().getString(R.string.add_item));

                    if (!amountTextView.getText().toString().equals("0")) {

                        amountTextView.setText("0");

                    }

                }

                categorySharedPreferences.writeCategoryName("Income");
                category = "Category - " + categorySharedPreferences.readCategoryName();
                subcategoryTextView.setText(category);

            }
        });
        subcategoryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categorySharedPreferences.readCategoryName().equals("")) {

                    Toast.makeText(ExpenseIncomeActivity.this, "Please, select a category. Expense or income", Toast.LENGTH_SHORT).show();

                } else if (categorySharedPreferences.readCategoryName().equals("Expense") || categorySharedPreferences.readCategoryName().equals("Income")) {

                    Intent intent = new Intent(ExpenseIncomeActivity.this, AddItemCategoryActivity.class);
                    startActivityForResult(intent, 1);

                }

            }
        });

        for (int i = 0; i < calcGridLayout.getChildCount(); i++) {

            CardView cardView = (CardView) calcGridLayout.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (view.getId() == R.id.todayCardView) {

                        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorDatePickerDialog));
                        datePickerDialog.setTitle("Calendar");
                        datePickerDialog.show(ExpenseIncomeActivity.this.getSupportFragmentManager(), "Date Picker Dialog");

                    } else {

                        if (categorySharedPreferences.readCategoryName().equals("")) {

                            Toast.makeText(ExpenseIncomeActivity.this, "Please, select a category. Expense or income", Toast.LENGTH_SHORT).show();

                        } else if (categorySharedPreferences.readCategoryName().equals("Expense") || categorySharedPreferences.readCategoryName().equals("Income")) {

                            if (itemNameTextView.getText().equals("ADD ITEM")) {

                                Toast.makeText(ExpenseIncomeActivity.this, "Please, select an item", Toast.LENGTH_SHORT).show();

                            } else {

                                switch (view.getId()) {

                                    case R.id.dotCardView:

                                        text = "";

                                        if (amountTextView.getText().equals("0")) {

                                            text = "0.";
                                            amountTextView.setText(text);

                                        } else {

                                            int operatorPos = 0, pos = 0;
                                            amount = amountTextView.getText().toString().toCharArray();

                                            for (in = 0; in < amount.length; in++) {

                                                if (amount[in] == '+' || amount[in] == '-') {

                                                    operatorPos = in;
                                                    break;

                                                }

                                            }

                                            if ((amount[operatorPos] == '+' || amount[operatorPos] == '-') && operatorPos == amount.length - 1) {

                                                text = amountTextView.getText().toString() + "0.";
                                                amountTextView.setText(text);

                                            } else if ((amount[operatorPos] == '+' || amount[operatorPos] == '-') && operatorPos < amount.length - 1) {

                                                StringBuilder secondNoStringBuilder = new StringBuilder();

                                                for (in = operatorPos + 1; in < amount.length; in++) {

                                                    secondNoStringBuilder = secondNoStringBuilder.append(amount[in]);

                                                }

                                                String secondNo = secondNoStringBuilder.toString();
                                                char[] secondNoArr = secondNo.toCharArray();

                                                for (in = 0; in < secondNoArr.length; in++) {

                                                    if (secondNoArr[in] == '.') {

                                                        pos = in;
                                                        break;

                                                    }

                                                }

                                                if (secondNoArr[pos] != '.') {

                                                    text = amountTextView.getText() + ".";
                                                    amountTextView.setText(text);

                                                }

                                            } else {

                                                for (in = 0; in < amount.length; in++) {

                                                    if (amount[in] == '.') {

                                                        pos = in;
                                                        break;

                                                    }

                                                }

                                                if (amount[pos] != '.') {

                                                    text = amountTextView.getText() + ".";
                                                    amountTextView.setText(text);

                                                }

                                            }

                                        }

                                        break;

                                    case R.id.zeroCardView:
                                        addAmountToTextView("0");
                                        break;

                                    case R.id.cancelCardView:

                                        if (!amountTextView.getText().equals("0")) {

                                            length = amountTextView.getText().length();

                                            if (length == 1) {

                                                text = "0";
                                                amountTextView.setText(text);

                                            } else {

                                                text = "";
                                                StringBuilder stringBuilderText = new StringBuilder();
                                                amount = amountTextView.getText().toString().toCharArray();

                                                for (in = 0; in < length - 1; in++) {

                                                    stringBuilderText = stringBuilderText.append(amount[in]);

                                                }

                                                text = stringBuilderText.toString();
                                                amountTextView.setText(text);

                                            }

                                        }

                                        break;

                                    case R.id.equalOrOkCardView:

                                        if (amountTextView.getText().toString().equals("0") || amountTextView.getText().toString().equals("0.") || amountTextView.getText().toString().equals("0.0") || amountTextView.getText().toString().equals("0.00") || amountTextView.getText().toString().equals("0.000")) {

                                            amountTextView.setText("0");

                                        } else {

                                            int operatorPos = 0;
                                            text = amountTextView.getText().toString();
                                            amount = text.toCharArray();

                                            for (in = 0; in < amount.length; in++) {

                                                if (amount[in] == '+' || amount[in] == '-') {

                                                    operatorPos = in;
                                                    break;

                                                }

                                            }

                                            if (amount[operatorPos] == '+' || amount[operatorPos] == '-') {

                                                checkIfDotIsPresentBeforeOrAfterOfOperator(amount, operatorPos);

                                            }

                                        }

                                        break;

                                    case R.id.oneCardView:
                                        addAmountToTextView("1");
                                        break;

                                    case R.id.twoCardView:
                                        addAmountToTextView("2");
                                        break;

                                    case R.id.threeCardView:
                                        addAmountToTextView("3");
                                        break;

                                    case R.id.subtractCardView:
                                        addOperatorToTextView("-");
                                        break;

                                    case R.id.fourCardView:
                                        addAmountToTextView("4");
                                        break;

                                    case R.id.fiveCardView:
                                        addAmountToTextView("5");
                                        break;

                                    case R.id.sixCardView:
                                        addAmountToTextView("6");
                                        break;

                                    case R.id.addCardView:
                                        addOperatorToTextView("+");
                                        break;

                                    case R.id.sevenCardView:
                                        addAmountToTextView("7");
                                        break;

                                    case R.id.eightCardView:
                                        addAmountToTextView("8");
                                        break;

                                    case R.id.nineCardView:
                                        addAmountToTextView("9");
                                        break;

                                }

                            }

                        }

                    }

                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_new_user_action_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear = monthOfYear + 1;

        if (dayOfMonth != currentDay) {

            date = String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth);
            labelTextView.setText(date);
            todaysDateTextView.setText(String.valueOf(year));
            selectedDay = dayOfMonth;
            selectedMonth = monthOfYear;
            selectedYear = year;

        } else {

            if (monthOfYear != currentMonth) {

                date = String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth);
                labelTextView.setText(date);
                todaysDateTextView.setText(String.valueOf(year));
                selectedDay = dayOfMonth;
                selectedMonth = monthOfYear;
                selectedYear = year;

            } else {

                date = String.valueOf(currentMonth) + "/" + String.valueOf(currentDay);
                labelTextView.setText(getResources().getString(R.string.today));
                todaysDateTextView.setText(date);

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        } else if (item.getItemId() == R.id.saveInformation) {

            if (categorySharedPreferences.readCategoryName().equals("")) {

                Toast.makeText(this, "Please, select a category. Expense or income", Toast.LENGTH_SHORT).show();

            } else if (categorySharedPreferences.readCategoryName().equals("Expense") || categorySharedPreferences.readCategoryName().equals("Income")){

                if (itemNameTextView.getText().equals("ADD ITEM")) {

                    Toast.makeText(this, "Please, select item", Toast.LENGTH_SHORT).show();

                } else {

                    if (amountTextView.getText().equals("0")) {

                        Toast.makeText(this, "Please, enter amount", Toast.LENGTH_SHORT).show();

                    } else if (amountTextView.getText().equals("0.0") || amountTextView.getText().equals("0.00") || amountTextView.getText().equals("0.000")) {

                        amountTextView.setText("0");
                        Toast.makeText(this, "Please, enter amount", Toast.LENGTH_SHORT).show();

                    } else {

                        int operatorPos = 0;
                        text = amountTextView.getText().toString();
                        amount = text.toCharArray();

                        for (in = 0; in < amount.length; in++) {

                            if (amount[in] == '+' || amount[in] == '-') {

                                operatorPos = in;
                                break;

                            }

                        }

                        if (amount[operatorPos] == '+' || amount[operatorPos] == '-') {

                            checkIfDotIsPresentBeforeOrAfterOfOperator(amount, operatorPos);

                        } else {

                            int pos = 0;
                            text = amountTextView.getText().toString();
                            amount = text.toCharArray();

                            for (in = 0; in < amount.length; in++) {

                                if (amount[in] == '.') {

                                    pos = in;
                                    break;

                                }

                            }

                            if (amount[pos] == '.' && pos == amount.length - 1) {

                                amountTextView.setText("0");
                                Toast.makeText(this, "Please, enter valid amount", Toast.LENGTH_SHORT).show();

                            } else {

                                if (signInUsingGoogleConfigActivity.readSignInUsingGoogleStatus()) {

                                    userIdDatabaseReference = userDatabaseReference.child(userId);
                                    beiAmountDatabaseReference = userIdDatabaseReference.child("BEIAmount");
                                    beiAmountDatabaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {

                                                calculateTheAmountsAndModifyIt();

                                            } else {

                                                if (categorySharedPreferences.readCategoryName().equals("Expense")) {

                                                    if (amountTextView.getText().toString().contains(".")) {

                                                        float totalExpense = Float.parseFloat(amountTextView.getText().toString());
                                                        balance = String.valueOf(0 - totalExpense);
                                                        expense = String.valueOf(totalExpense);
                                                        income = "0.0";

                                                    } else {

                                                        long totalExpense = Long.parseLong(amountTextView.getText().toString());
                                                        balance = String.valueOf(0 - totalExpense);
                                                        expense = String.valueOf(totalExpense);
                                                        income = "0";

                                                    }

                                                    updateTheBEIAmount(balance, expense, income);

                                                } else if (categorySharedPreferences.readCategoryName().equals("Income")) {

                                                    if (amountTextView.getText().toString().contains(".")) {

                                                        float totalIncome = Float.parseFloat(amountTextView.getText().toString());
                                                        balance = String.valueOf(totalIncome);
                                                        income = String.valueOf(totalIncome);
                                                        expense = "0.0";

                                                    } else {

                                                        long totalIncome = Long.parseLong(amountTextView.getText().toString());
                                                        balance = String.valueOf(totalIncome);
                                                        income = String.valueOf(totalIncome);
                                                        expense = "0";

                                                    }

                                                    updateTheBEIAmount(balance, expense, income);

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else if (signInUsingEmailConfigActivity.readSignInUsingEmailStatus()) {

                                    if (selectedYear == currentYear) {

                                        calculateTheAmountsAndModifyIt();

                                    } else {

                                        calculateTheAmountsAndModifyIt();

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return true;

    }
}
