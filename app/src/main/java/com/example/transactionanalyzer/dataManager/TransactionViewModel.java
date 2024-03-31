package com.example.transactionanalyzer.dataManager;

import androidx.lifecycle.ViewModel;

import com.example.transactionanalyzer.R;
import com.example.transactionanalyzer.entity.Account;
import com.example.transactionanalyzer.entity.Transaction;
import com.example.transactionanalyzer.repository.AccountManager;
import com.example.transactionanalyzer.repository.TransactionManager;

import java.util.List;

public class TransactionViewModel extends ViewModel {
    TransactionManager transactionManager;
    AccountManager accountManager;
    int id= R.id.home;

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
