package com.example.transactionanalyzer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.transactionanalyzer.R;
import com.example.transactionanalyzer.dataManager.TransactionViewModel;
import com.example.transactionanalyzer.entity.Transaction;
import com.example.transactionanalyzer.repository.TransactionManager;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

public class StatsFragment extends Fragment {
    TextView tvR, tvPython, tvR7, tvPython7, tvR30, tvPython30;
    PieChart pieChart, pieChart7, pieChart30;
    TransactionViewModel transactionViewModel ;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        // Initialize UI elements
        tvR = rootView.findViewById(R.id.tvR);
        tvPython = rootView.findViewById(R.id.tvPython);
        pieChart = rootView.findViewById(R.id.piechart);
        tvR7 = rootView.findViewById(R.id.tvR7);
        tvPython7 = rootView.findViewById(R.id.tvPython7);
        pieChart7 = rootView.findViewById(R.id.piechart7);
        tvR30 = rootView.findViewById(R.id.tvR30);
        tvPython30 = rootView.findViewById(R.id.tvPython30);
        pieChart30 = rootView.findViewById(R.id.piechart30);

        // Set data for UI elements
        setData();

        return rootView;
    }

    private void setData() {
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        TransactionManager transactionManager=transactionViewModel.getTransactionManager();
        int aid = sharedPreferences.getInt("aid", -1);
        // Set the percentage of language used
        tvR.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionManager.getTransactions("debit","-12 hours",aid)),(float) calculateTotalAmount(transactionManager.getTransactions("credit","-12 days",aid)))));
        tvPython.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionManager.getTransactions("credit","-12 days",aid)),(float) calculateTotalAmount(transactionManager.getTransactions("debit","-12 hours",aid)))));
        tvR7.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionManager.getTransactions("debit","-7 days",aid)),(float) calculateTotalAmount(transactionManager.getTransactions("credit","-7 days",aid)))));
        tvPython7.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionManager.getTransactions("credit","-7 days",aid)),(float) calculateTotalAmount(transactionManager.getTransactions("debit","-7 days",aid)))));
        tvR30.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionManager.getMonthlyTransactions("debit",2024,03,aid)),(float) calculateTotalAmount(transactionManager.getMonthlyTransactions("credit",2024,03,aid)))));
        tvPython30.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionManager.getMonthlyTransactions("credit",2024,03,aid)),(float) calculateTotalAmount(transactionManager.getMonthlyTransactions("debit",2024,03,aid)))));

        // Set the data and color to the pie chart
        setPieChart(pieChart, (int) Math.round(calculateTotalAmount(transactionManager.getTransactions("debit","-12 hours",aid))), (int) Math.round(calculateTotalAmount(transactionManager.getTransactions("credit","-12 days",aid))));
        setPieChart(pieChart7, (int) Math.round(calculateTotalAmount(transactionManager.getTransactions("debit","-7 days",aid))), (int) Math.round(calculateTotalAmount(transactionManager.getTransactions("credit","-7 days",aid))));
        setPieChart(pieChart30, (int) Math.round(calculateTotalAmount(transactionManager.getMonthlyTransactions("debit",2024,03,aid))), (int) Math.round(calculateTotalAmount(transactionManager.getMonthlyTransactions("credit",2024,03,aid))));
    }

    private void setPieChart(PieChart chart, Integer debit, Integer credit) {
        chart.addPieSlice(
                new PieModel(
                        "debit",
                        calculatePercentage(debit,credit),
                        Color.parseColor("#FFA726")));
        chart.addPieSlice(
                new PieModel(
                        "credit",
                        calculatePercentage(credit,debit),
                        Color.parseColor("#66BB6A")));
        chart.startAnimation();
    }
    private double calculateTotalAmount(List<Transaction> transactions) {
        double totalAmount = 0.0;
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }
    private float calculatePercentage(float a ,float b){
        return ((a/(a+b))*100);
    }
}
