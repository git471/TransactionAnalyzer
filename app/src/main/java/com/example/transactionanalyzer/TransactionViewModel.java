package com.example.transactionanalyzer;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class TransactionViewModel extends ViewModel {
    List<Transaction> debitTrasactions;
    List<Transaction> creditTransactions;
    List<Transaction> last7daysDebitTransactions;
    List<Transaction> last7daysCreditTransactions;
    List<Transaction> last30daysCreditTransactions;
    List<Transaction> last30daysdebitTransactions;
    public List<Transaction> getDebitTrasactions() {
        return debitTrasactions;
    }

    public void setDebitTrasactions(List<Transaction> debitTrasactions) {
        this.debitTrasactions = debitTrasactions;
    }

    public List<Transaction> getCreditTransactions() {
        return creditTransactions;
    }

    public void setCreditTransactions(List<Transaction> creditTransactions) {
        this.creditTransactions = creditTransactions;
    }

    public List<Transaction> getLast7daysDebitTransactions() {
        return last7daysDebitTransactions;
    }

    public void setLast7daysDebitTransactions(List<Transaction> last7daysDebitTransactions) {
        this.last7daysDebitTransactions = last7daysDebitTransactions;
    }

    public List<Transaction> getLast7daysCreditTransactions() {
        return last7daysCreditTransactions;
    }

    public void setLast7daysCreditTransactions(List<Transaction> last7daysCreditTransactions) {
        this.last7daysCreditTransactions = last7daysCreditTransactions;
    }

    public List<Transaction> getLast30daysCreditTransactions() {
        return last30daysCreditTransactions;
    }

    public void setLast30daysCreditTransactions(List<Transaction> last30daysCreditTransactions) {
        this.last30daysCreditTransactions = last30daysCreditTransactions;
    }

    public List<Transaction> getLast30daysdebitTransactions() {
        return last30daysdebitTransactions;
    }

    public void setLast30daysdebitTransactions(List<Transaction> last30daysdebitTransactions) {
        this.last30daysdebitTransactions = last30daysdebitTransactions;
    }

}
