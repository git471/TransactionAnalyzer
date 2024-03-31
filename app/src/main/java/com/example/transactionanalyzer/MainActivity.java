package com.example.transactionanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener,OnDataSentListener {
    TransactionManager transactionManager;
    Toolbar toolbar;
    TransactionsFragment transactionsFragment;
    StatsFragment secondFragment = new StatsFragment();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsFragment = new TransactionsFragment(this);
        setContentView(R.layout.activity_main);
        setToolbar();
        setBottomNavigationView();
        setTransactionsFragment();
        requestPermissions();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        toolbar.setNavigationOnClickListener(view -> Log.d("TAG", "onClick: "));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.transactions) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, transactionsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.stats) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, secondFragment)
                    .commit();
            return true;
        }
        return false;
    }
    public void setTransactionsFragment(){
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionManager=new TransactionManager(this);
        transactionManager.addDebitTransaction(32,"viaks");
        transactionViewModel.setDebitTrasactions(transactionManager.getTransactions("debit","-12 hours"));
        transactionViewModel.setCreditTransactions(transactionManager.getTransactions("credit","-12 days"));
        transactionViewModel.setLast7daysDebitTransactions(transactionManager.getTransactions("debit","-7 days"));
        transactionViewModel.setLast7daysCreditTransactions(transactionManager.getTransactions("credit","-7 days"));
        transactionViewModel.setLast30daysCreditTransactions(transactionManager.getMonthlyTransactions("credit",2024,03));
        transactionViewModel.setLast30daysdebitTransactions(transactionManager.getMonthlyTransactions("debit",2024,03));

    }
    public void setBottomNavigationView(){
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);
        int selectedColor = ContextCompat.getColor(this,R.color.debit); // Change this to the color you want
        int defaultColor = ContextCompat.getColor(this,R.color.black); // Change this to the color you want
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        selectedColor,
                        defaultColor
                }
        );
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.transactions);
    }
    private void requestPermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.RECEIVE_SMS
            },100);
        }
    }

    @Override
    public void onDataSent(Transaction data,String tableName) {
        transactionManager.updateTransactionValue((int)data.getId(),data.getDescription(),tableName);
    }
}