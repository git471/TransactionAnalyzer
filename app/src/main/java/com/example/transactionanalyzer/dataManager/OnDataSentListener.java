package com.example.transactionanalyzer.dataManager;

import com.example.transactionanalyzer.entity.Account;
import com.example.transactionanalyzer.entity.Transaction;

public interface OnDataSentListener {
    void onDataSent(Transaction data, String tableName);
    void createAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
