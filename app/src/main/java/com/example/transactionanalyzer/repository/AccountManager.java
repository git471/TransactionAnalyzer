package com.example.transactionanalyzer.repository;

import static com.example.transactionanalyzer.repository.DBHelper.ACCOUNT_MESSAGE_STRING;
import static com.example.transactionanalyzer.repository.DBHelper.ACCOUNT_NAME;
import static com.example.transactionanalyzer.repository.DBHelper.TABLE_ACCOUNT;
import static com.example.transactionanalyzer.repository.DBHelper.TABLE_DEBIT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.transactionanalyzer.entity.Account;
import com.example.transactionanalyzer.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private DBHelper dbHelper;
    public AccountManager(Context context){
            dbHelper = new DBHelper(context);
    }
    public void addAccount(String name, String messageString) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME, name);
        values.put(ACCOUNT_MESSAGE_STRING,messageString);
        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    public void updateAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME, account.getName());
        values.put(ACCOUNT_MESSAGE_STRING, account.getMessageString());
        String selection = "aid=?";
        String[] selectionArgs = { String.valueOf(account.getAid()) };
        int count = db.update(TABLE_ACCOUNT, values, selection, selectionArgs);
        db.close();
        if (count > 0) {
            // Transaction value updated successfully
        } else {
            // Failed to update transaction value
        }
    }
    public void deleteAccount(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "aid = "+id;

        int rowsAffected = db.delete(TABLE_ACCOUNT, whereClause, null);

        if (rowsAffected > 0) {
            // Deletion was successful
            Log.d("DeleteEntry", "Deleted entry with id = 1 successfully");
        } else {
            // No rows were deleted (probably because no row with id = 1 exists)
            Log.d("DeleteEntry", "No entry found with id = 1");
        }
    }
    @SuppressLint("Range")
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("aid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String messageString = cursor.getString(cursor.getColumnIndex("messageString"));
                accounts.add(new Account(id,name,messageString));
            }
            cursor.close();
        }
        return accounts;
    }
}
