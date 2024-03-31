package com.example.transactionanalyzer;


import static com.example.transactionanalyzer.DBHelper.KEY_AMOUNT;
import static com.example.transactionanalyzer.DBHelper.KEY_DESCRIPTION_CREDIT;
import static com.example.transactionanalyzer.DBHelper.KEY_DESCRIPTION_DEBIT;
import static com.example.transactionanalyzer.DBHelper.KEY_ID;
import static com.example.transactionanalyzer.DBHelper.KEY_TIMESTAMP;
import static com.example.transactionanalyzer.DBHelper.TABLE_CREDIT;
import static com.example.transactionanalyzer.DBHelper.TABLE_DEBIT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private DBHelper dbHelper;

    public TransactionManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void addDebitTransaction(double amount, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DESCRIPTION_DEBIT, description);
        db.insert(TABLE_DEBIT, null, values);
        db.close();
    }

    public void addCreditTransaction(double amount, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DESCRIPTION_CREDIT, description);
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

                transactions.add(new Transaction(id, amount, description, timestamp));
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

                transactions.add(new Transaction(id, amount, description, timestamp));
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
    public List<Transaction> getTransactions(String tableName,String time) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "amount","description", "timestamp"};
        String selection = "timestamp >= datetime('now', '"+time+"')";
        Cursor cursor = db.query(tableName, columns, selection, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                // Create Transaction object and add it to the list
                Transaction transaction = new Transaction((long) id, amount,description, timestamp);
                transactions.add(transaction);
            }
            cursor.close();
        }
        return transactions;
    }
    @SuppressLint("Range")
    public List<Transaction> getMonthlyTransactions(String tableName,int year, int month) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "amount","description", "timestamp"}; // Adjust the column names accordingly
        // Construct the query
        String selection = "strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ?";
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
                // Create Transaction object and add it to the list
                Transaction transaction = new Transaction(id, amount,description, timestamp);
                transactions.add(transaction);
            }
            cursor.close();
        }

        return transactions;
    }

}

