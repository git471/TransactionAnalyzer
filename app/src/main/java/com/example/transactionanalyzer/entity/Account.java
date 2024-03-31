package com.example.transactionanalyzer;

import java.io.Serializable;

public class Account implements Serializable {
    public int aid;
    public String name;
    public String messageString;
    

    public Account(int aid, String name, String messageString) {
        this.aid = aid;
        this.name = name;
        this.messageString = messageString;
    }
}
