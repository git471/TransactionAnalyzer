package com.example.transactionanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.transactionanalyzer.dataManager.OnDataSentListener;
import com.example.transactionanalyzer.dataManager.TransactionViewModel;
import com.example.transactionanalyzer.entity.Account;
import com.example.transactionanalyzer.entity.Transaction;
import com.example.transactionanalyzer.fragment.HomeFragment;
import com.example.transactionanalyzer.fragment.StatsFragment;
import com.example.transactionanalyzer.fragment.TransactionsFragment;
import com.example.transactionanalyzer.repository.AccountManager;
import com.example.transactionanalyzer.repository.TransactionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener, OnDataSentListener {
    TransactionManager transactionManager;
    Toolbar toolbar;
    TransactionsFragment transactionsFragment;
    StatsFragment secondFragment = new StatsFragment();
    HomeFragment homeFragment;
    BottomNavigationView bottomNavigationView;
    TransactionViewModel transactionViewModel;

    AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsFragment = new TransactionsFragment(this);
        homeFragment = new HomeFragment(this);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        setContentView(R.layout.activity_main);
        setToolbar();
        setHomeFragment();
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
        }else if (item.getItemId() == R.id.home) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        }
        return false;
    }
    public void setTransactionsFragment(){
        transactionManager=new TransactionManager(this);
        transactionViewModel.setTransactionManager(transactionManager);
    }
    public void setHomeFragment(){
        accountManager = new AccountManager(this);
        transactionViewModel.setAccountManager(accountManager);
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
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    private void requestPermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.RECEIVE_SMS
            },100);
        }
    }

    @Override
    public void onDataSent(Transaction data, String tableName) {
        transactionManager.updateTransactionValue((int)data.getId(),data.getDescription(),tableName);
    }

    @Override
    public void createAccount(Account account) {
        accountManager.addAccount(account.getName(),account.getMessageString());
    }

    @Override
    public void updateAccount(Account account) {
        accountManager.updateAccount(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountManager.deleteAccount(account.getAid());
    }

}