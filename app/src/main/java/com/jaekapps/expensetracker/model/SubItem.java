package com.jaekapps.expensetracker.model;

public class SubItem {

    String amount, item_name;

    public SubItem() {}

    public String getAmount() {
        return amount;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
