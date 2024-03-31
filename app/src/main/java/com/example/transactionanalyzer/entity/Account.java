package com.example.transactionanalyzer.entity;

import java.io.Serializable;

public class Account implements Serializable {
    public int aid;
    public String name;
    public String messageString;

    public Account() {
    }

    public Account(int aid, String name, String messageString) {
        this.aid = aid;
        this.name = name;
        this.messageString = messageString;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }
}
