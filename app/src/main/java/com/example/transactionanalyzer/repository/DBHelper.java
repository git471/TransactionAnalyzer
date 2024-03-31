package com.example.transactionanalyzer;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "transactions.db";
    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_DEBIT = "debit";
    public static final String TABLE_CREDIT = "credit";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_TIMESTAMP = "timestamp";

    // Debit table column names
    public static final String KEY_DESCRIPTION_DEBIT = "description";

    // Credit table column names
    public static final String KEY_DESCRIPTION_CREDIT = "description";

    // Create table statements
    private static final String CREATE_TABLE_DEBIT = "CREATE TABLE " + TABLE_DEBIT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_AMOUNT + " REAL,"
            + KEY_DESCRIPTION_DEBIT + " TEXT,"
            + KEY_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_CREDIT = "CREATE TABLE " + TABLE_CREDIT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_AMOUNT + " REAL,"
            + KEY_DESCRIPTION_CREDIT + " TEXT,"
            + KEY_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEBIT);
        db.execSQL(CREATE_TABLE_CREDIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDIT);
        onCreate(db);
    }
}
