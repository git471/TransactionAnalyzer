package com.example.transactionanalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;
import java.util.Objects;

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
        // Set the percentage of language used
        tvR.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionViewModel.getDebitTrasactions()),(float) calculateTotalAmount(transactionViewModel.getCreditTransactions()))));
        tvPython.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionViewModel.getCreditTransactions()),(float) calculateTotalAmount(transactionViewModel.getDebitTrasactions()))));
        tvR7.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionViewModel.getLast7daysDebitTransactions()),(float) calculateTotalAmount(transactionViewModel.getLast7daysCreditTransactions()))));
        tvPython7.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionViewModel.getLast7daysCreditTransactions()),(float) calculateTotalAmount(transactionViewModel.getLast7daysDebitTransactions()))));
        tvR30.setText(String.valueOf(calculatePercentage((float) calculateTotalAmount(transactionViewModel.getLast30daysdebitTransactions()),(float) calculateTotalAmount(transactionViewModel.getLast30daysCreditTransactions()))));
        tvPython30.setText(String.valueOf(calculatePercentage((float)calculateTotalAmount(transactionViewModel.getLast30daysCreditTransactions()),(float) calculateTotalAmount(transactionViewModel.getLast30daysdebitTransactions()))));

        // Set the data and color to the pie chart
        setPieChart(pieChart, (int) Math.round(calculateTotalAmount(transactionViewModel.getDebitTrasactions())), (int) Math.round(calculateTotalAmount(transactionViewModel.getCreditTransactions())));
        setPieChart(pieChart7, (int) Math.round(calculateTotalAmount(transactionViewModel.getLast7daysDebitTransactions())), (int) Math.round(calculateTotalAmount(transactionViewModel.getLast7daysCreditTransactions())));
        setPieChart(pieChart30, (int) Math.round(calculateTotalAmount(transactionViewModel.getLast30daysdebitTransactions())), (int) Math.round(calculateTotalAmount(transactionViewModel.getLast30daysCreditTransactions())));
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
