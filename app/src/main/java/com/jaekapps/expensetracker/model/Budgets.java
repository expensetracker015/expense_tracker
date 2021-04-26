package com.jaekapps.expensetracker.model;

public class Budgets {

    private String amount, endDate, period, startDate;

    public Budgets() {}

    public String getAmount() {

        return amount;
    }

    public String getEndDate() {

        return endDate;
    }

    public String getPeriod() {

        return period;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setAmount(String amount) {

        this.amount = amount;
    }

    public void setEndDate(String endDate) {

        this.endDate = endDate;
    }

    public void setPeriod(String period) {

        this.period = period;
    }

    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }
}
