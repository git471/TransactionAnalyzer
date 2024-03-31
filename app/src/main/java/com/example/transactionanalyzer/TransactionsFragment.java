package com.example.transactionanalyzer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TransactionsFragment extends Fragment {
    TransactionViewModel transactionViewModel;
    private OnDataSentListener mListener;

    public TransactionsFragment(OnDataSentListener listener) {
        this.mListener=listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);
        TableLayout transactionCreditTable = rootView.findViewById(R.id.transactionCreditTable);
        addHeaderRow(transactionCreditTable);
        TableLayout transactionDebitTable = rootView.findViewById(R.id.transactionDebitTable);
        addHeaderRow(transactionDebitTable);
        TableLayout transactionWeekDebitTable = rootView.findViewById(R.id.weektransactionDebitTable);
        addHeaderRow(transactionWeekDebitTable);
        TableLayout transactionWeekCreditTable = rootView.findViewById(R.id.weektransactionCreditTable);
        addHeaderRow(transactionWeekCreditTable);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        for (Transaction transaction : transactionViewModel.getDebitTrasactions()) {
            addTransactionRow(transaction, transactionDebitTable, "debit");
        }
        for (Transaction transaction : transactionViewModel.getCreditTransactions()) {
            addTransactionRow(transaction, transactionCreditTable, "credit");
        }
        for (Transaction transaction : transactionViewModel.getLast7daysDebitTransactions()) {
            addTransactionRow(transaction, transactionWeekDebitTable, "debit");
        }
        for (Transaction transaction : transactionViewModel.getLast7daysCreditTransactions()) {
            addTransactionRow(transaction, transactionWeekCreditTable, "credit");
        }
        ((TextView) rootView.findViewById(R.id.todayTotaldebit)).setText("Total Debit : " + calculateTotalAmount(transactionViewModel.getDebitTrasactions()));
        ((TextView) rootView.findViewById(R.id.todayTotalCredit)).setText("Total Credit : " + calculateTotalAmount(transactionViewModel.getCreditTransactions()));
        ((TextView) rootView.findViewById(R.id.last7daysCreditTotal)).setText("Total Credit : " + calculateTotalAmount(transactionViewModel.getLast7daysCreditTransactions()));
        ((TextView) rootView.findViewById(R.id.last7daysDebitTotal)).setText("Total Debit : " + calculateTotalAmount(transactionViewModel.getLast7daysDebitTransactions()));
        ((TextView) rootView.findViewById(R.id.thisMonthTotal)).setText("Total Credit : " + calculateTotalAmount(transactionViewModel.getLast30daysCreditTransactions()) + "    Total Debit : " + calculateTotalAmount(transactionViewModel.getLast30daysdebitTransactions()));
        ((TextView) rootView.findViewById(R.id.marchMonthTotal)).setText("Total Credit : " + calculateTotalAmount(transactionViewModel.getLast30daysCreditTransactions()) + "    Total Debit : " + calculateTotalAmount(transactionViewModel.getLast30daysdebitTransactions()));

        return rootView;
    }

    private void addTransactionRow(Transaction transaction, TableLayout transactionTable, String type) {
        // Create a new row
        TableRow row = new TableRow(requireContext());
        row.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.row_background)); // Set custom background

        // Create TextViews for each cell in the row
        TextView amountTextView = new TextView(requireContext());
        amountTextView.setText(String.valueOf(transaction.getAmount()));
        amountTextView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background

        TextView descriptionTextView = new TextView(requireContext());
        descriptionTextView.setText(transaction.getDescription());
        descriptionTextView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background
        int tableWidth = transactionTable.getWidth();
        int descriptionColumnWidth = (int) (0.3 * tableWidth);
        descriptionTextView.setWidth(descriptionColumnWidth);
        TextView timeTextView = new TextView(requireContext());
        timeTextView.setText(convertIntoIST(transaction.getTimestamp()));
        timeTextView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background
        Button button = new Button(requireContext());
        button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)); // Set width and height to wrap content
        button.setPadding(0, 0, 0, 0); // Set padding to zero
        button.setBackgroundResource(android.R.color.transparent); // Remove button background
        button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background));
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_note_24, 0, 0, 0); // Replace ic_edit with your icon drawable
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogWithSingleTransactionFragment(transaction,type);
            }
        });
        // Add the TextViews to the row
        row.addView(amountTextView);
        row.addView(descriptionTextView);
        row.addView(timeTextView);
        row.addView(button);
        // Add the row to the TableLayout
        transactionTable.addView(row);
    }

    @SuppressLint("SetTextI18n")
    private void addHeaderRow(TableLayout transactionTable) {

        TableRow headerRow = new TableRow(requireContext());
        headerRow.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.row_background)); // Set custom background

        TextView amountHeader = new TextView(requireContext());
        amountHeader.setText("Amount");
        amountHeader.setTypeface(null, Typeface.BOLD);
        amountHeader.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background

        TextView descriptionHeader = new TextView(requireContext());
        descriptionHeader.setText("Description");
        descriptionHeader.setTypeface(null, Typeface.BOLD);
        descriptionHeader.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background

        TextView timeHeader = new TextView(requireContext());
        timeHeader.setText("Time");
        timeHeader.setTypeface(null, Typeface.BOLD);
        timeHeader.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.cell_background)); // Set custom background
        TextView operationsHeader = new TextView(requireContext());
        operationsHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_dehaze_24, 0, 0, 0); // Replace ic_edit with your icon drawable

        headerRow.addView(amountHeader);
        headerRow.addView(descriptionHeader);
        headerRow.addView(timeHeader);
        headerRow.addView(operationsHeader);

        transactionTable.addView(headerRow);
    }

    @SuppressLint("SimpleDateFormat")
    public String convertIntoIST(String date) {
        if (date != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date inputDate = inputFormat.parse(date);
                outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                assert inputDate != null;
                return outputFormat.format(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private double calculateTotalAmount(List<Transaction> transactions) {
        double totalAmount = 0.0;
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }

    private void showAlertDialogWithSingleTransactionFragment(Transaction transaction,String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_single_transaction_activity, null);
        ((TextView)view.findViewById(R.id.textViewAmount)).setText(String.valueOf(transaction.getAmount()));
        ((TextView)view.findViewById(R.id.textViewDescription)).setText(String.valueOf(transaction.getDescription()));
        ((TextView)view.findViewById(R.id.textViewDate)).setText(String.valueOf(transaction.getTimestamp()));

        builder.setView(view);
        builder.setPositiveButton("OK", (dialog, which) -> {
            transaction.setDescription(((TextView)view.findViewById(R.id.textViewDescription)).getText().toString());
            if(mListener != null)
                mListener.onDataSent(transaction,type);
            startActivity(new Intent(requireActivity(), MainActivity.class));
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, i) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            // Set the custom border drawable as the background
            window.setBackgroundDrawableResource(R.drawable.border);
        }
        alertDialog.show();
    }
}