package com.jaekapps.expensetracker.model;

public class PeriodicBudgets {

    private int percentage;
    private String budgetAmount, budgetName, expenseAmount, period, status;

    public PeriodicBudgets() {}

    public int getPercentage() {

        return percentage;
    }

    public String getBudgetAmount() {

        return budgetAmount;
    }

    public String getBudgetName() {

        return budgetName;
    }

    public String getExpenseAmount() {

        return expenseAmount;
    }

    public String getPeriod() {

        return period;
    }

    public String getStatus() {

        return status;
    }

    public void setBudgetAmount(String budgetAmount) {

        this.budgetAmount = budgetAmount;
    }

    public void setBudgetName(String budgetName) {

        this.budgetName = budgetName;
    }

    public void setExpenseAmount(String expenseAmount) {

        this.expenseAmount = expenseAmount;
    }

    public void setPercentage(int percentage) {

        this.percentage = percentage;
    }

    public void setPeriod(String period) {

        this.period = period;
    }

    public void setStatus(String status) {

        this.status = status;
    }
}
