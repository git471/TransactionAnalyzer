package com.example.transactionanalyzer;

import java.io.Serializable;

public class Transaction implements Serializable {
    long id;
    double amount;
    String description;
    String timestamp;

    public Transaction(long id, double amount, String description, String timestamp) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

