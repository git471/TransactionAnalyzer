package com.example.transactionanalyzer.repository;


import static com.example.transactionanalyzer.repository.DBHelper.ACCOUNT_ID;
import static com.example.transactionanalyzer.repository.DBHelper.KEY_AMOUNT;
import static com.example.transactionanalyzer.repository.DBHelper.KEY_DESCRIPTION_CREDIT;
import static com.example.transactionanalyzer.repository.DBHelper.KEY_DESCRIPTION_DEBIT;
import static com.example.transactionanalyzer.repository.DBHelper.KEY_ID;
import static com.example.transactionanalyzer.repository.DBHelper.KEY_TIMESTAMP;
import static com.example.transactionanalyzer.repository.DBHelper.TABLE_CREDIT;
import static com.example.transactionanalyzer.repository.DBHelper.TABLE_DEBIT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.transactionanalyzer.entity.Transaction;
import com.example.transactionanalyzer.repository.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private DBHelper dbHelper;

    public TransactionManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void addDebitTransaction(double amount, String description, int aid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DESCRIPTION_DEBIT, description);
        values.put(ACCOUNT_ID, aid);
        db.insert(TABLE_DEBIT, null, values);
        db.close();
    }

    public void addCreditTransaction(double amount, String description, int aid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DESCRIPTION_CREDIT, description);
        values.put(ACCOUNT_ID, aid);
        db.insert(TABLE_CREDIT, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public List<Transaction> getAllDebitTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEBIT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
                double amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT));
                String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION_DEBIT));
                String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                int aid = cursor.getInt(cursor.getColumnIndex(ACCOUNT_ID));
                transactions.add(new Transaction(id, amount, description, timestamp, aid));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;
    }
    @SuppressLint("Range")
    public List<Transaction> getAllCreditTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CREDIT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
                double amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT));
                String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION_CREDIT));
                String timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                int aid = cursor.getInt(cursor.getColumnIndex(ACCOUNT_ID));
                transactions.add(new Transaction(id, amount, description, timestamp, aid));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;
    }
    public void updateTransactionValue(int transactionId, String newValue,String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", newValue);
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(transactionId) };
        int count = db.update(table, values, selection, selectionArgs);
        db.close();
        if (count > 0) {
            // Transaction value updated successfully
        } else {
            // Failed to update transaction value
        }
    }
    @SuppressLint("Range")
    public List<Transaction> getTransactions(String tableName,String time,int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "amount","description", "timestamp","aid"};
        String selection = "timestamp >= datetime('now', '" + time + "') AND aid = "+accountId;
        Cursor cursor = db.query(tableName, columns, selection, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                int aid = cursor.getInt(cursor.getColumnIndex(ACCOUNT_ID));
                transactions.add(new Transaction(id, amount, description, timestamp, aid));
            }
            cursor.close();
        }
        return transactions;
    }
    @SuppressLint("Range")
    public List<Transaction> getMonthlyTransactions(String tableName,int year, int month,int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "amount","description", "timestamp", "aid"}; // Adjust the column names accordingly
        // Construct the query
        String selection = "strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ? AND aid = "+accountId;
        String[] selectionArgs = {String.valueOf(year), String.format("%02d", month)}; // Ensure month is in two digits format
        // Execute the query
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        // Iterate through the cursor and populate the list of transactions
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                int aid = cursor.getInt(cursor.getColumnIndex(ACCOUNT_ID));
                transactions.add(new Transaction(id, amount, description, timestamp, aid));
            }
            cursor.close();
        }

        return transactions;
    }

}

