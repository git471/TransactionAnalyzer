package com.example.transactionanalyzer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.transactionanalyzer.MainActivity;
import com.example.transactionanalyzer.R;
import com.example.transactionanalyzer.dataManager.OnDataSentListener;
import com.example.transactionanalyzer.dataManager.TransactionViewModel;
import com.example.transactionanalyzer.entity.Account;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {
    OnDataSentListener mListner;
    TransactionViewModel transactionViewModel;
    LinearLayout tableParent;

    public HomeFragment(OnDataSentListener listener) {
        this.mListner = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tableParent = rootView.findViewById(R.id.parent);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        Button button = rootView.findViewById(R.id.addAccount);
        button.setOnClickListener(view -> {
            showAccountDialog();
        });
        for (Account account : transactionViewModel.getAccountManager().getAccounts()) {
            int i = 0;
            addAccountRow(account, rootView, i++);
        }
        return rootView;
    }

    @SuppressLint("MissingInflatedId")
    public void showAccountDialog() {
        View alertView = LayoutInflater.from(requireContext()).inflate(R.layout.account, null);
        EditText nameView = alertView.findViewById(R.id.accountName);
        EditText messageView = alertView.findViewById(R.id.accountMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertView);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            if (mListner != null)
                mListner.createAccount(new Account(0, nameView.getText().toString(), messageView.getText().toString()));
            requireActivity().startActivity(new Intent(requireActivity(),MainActivity.class));
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.border);
        }
        alertDialog.show();

    }

    private void addAccountRow(Account account, View rootView, int i) {
        TableLayout tableLayout = new TableLayout(requireContext());

        // Set TableLayout attributes
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(4, 4, 4, 10); // Add margin between TableLayouts
        tableLayout.setLayoutParams(layoutParams);
        tableLayout.setStretchAllColumns(true);
        // Create a new row
        TableRow idRow = new TableRow(requireContext());

        // Create TextViews for each cell in the row
        TextView id = new TextView(requireContext());
        id.setText("Account id : ");
        idRow.addView(id);

        TextView idValue = new TextView(requireContext());
        idValue.setText(String.valueOf(account.getAid()));
        idValue.setTag("id");
        idRow.addView(idValue);
        idRow.setPadding(20, 20, 20, 20);
        tableLayout.addView(idRow);

        TableRow nameRow = new TableRow(requireContext());

        // Create TextViews for each cell in the row
        TextView name = new TextView(requireContext());
        name.setText("Account name : ");
        nameRow.addView(name);

        TextView nameValue = new TextView(requireContext());
        nameValue.setText(account.getName());
        nameRow.addView(nameValue);
        nameRow.setPadding(20, 20, 20, 20);
        tableLayout.addView(nameRow);

        TableRow messageRow = new TableRow(requireContext());

        // Create TextViews for each cell in the row
        TextView message = new TextView(requireContext());
        message.setText("Message String : ");
        messageRow.addView(message);

        TextView messageValue = new TextView(requireContext());
        messageValue.setText(String.valueOf(account.getMessageString()));
        messageRow.addView(messageValue);
        messageRow.setPadding(20, 20, 20, 20);
        tableLayout.addView(messageRow);

        TableRow operationsRow = new TableRow(requireContext());
        Button editButton = new Button(requireContext());
        editButton.setOnClickListener(view -> {
            handleAccountUpdate(account);
        });
        editButton.setText("edit");
        editButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24, 0, 0, 0);
        operationsRow.addView(editButton);
        Button deleteButton = new Button(requireContext());
        deleteButton.setOnClickListener(view -> {
            if(mListner != null)
                mListner.deleteAccount(account);
            requireActivity().startActivity(new Intent(requireActivity(),MainActivity.class));
        });
        deleteButton.setText("delete");
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_delete_24, 0, 0, 0);
        operationsRow.addView(deleteButton);
        tableLayout.addView(operationsRow);
        tableLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border));
        tableLayout.setPadding(20, 20, 20, 20);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        tableLayout.setOnClickListener(v -> {
            removePreviousTableStyles(rootView);
            editor.putInt("aid", account.getAid());
            editor.apply();
        });
        if(account.getAid() == sharedPreferences.getInt("aid", -1)){
            addSelectionLayout(tableLayout);
        }
        removePreviousTableStyles(rootView);
        tableParent.addView(tableLayout);
    }
    public void addSelectionLayout(TableLayout tableLayout){
        tableLayout.setBackgroundResource(R.drawable.selected); // Add border

        // Add primary label
        TextView primaryLabel = new TextView(requireContext());
        primaryLabel.setText("Primary");
        primaryLabel.setTag("primary_label"); // Set tag for identification
        primaryLabel.setTextColor(Color.WHITE); // Set text color
        primaryLabel.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent)); // Set background color
        primaryLabel.setPadding(16, 8, 16, 8); // Set padding
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL; // Center horizontally
        tableLayout.addView(primaryLabel, 0, layoutParams); // Add label above the table
        TextView tableNameTextView = (TextView) tableLayout.findViewWithTag("id");
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("aid", tableNameTextView.getText()!=null?Integer.parseInt((String) tableNameTextView.getText()):-1);
        editor.apply();
    }
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    public void handleAccountUpdate(Account account){
        View alertView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_account, null);
        TextView idView = alertView.findViewById(R.id.accountId);
        EditText nameView = alertView.findViewById(R.id.accountName);
        EditText messageView = alertView.findViewById(R.id.accountMessage);
        idView.setText(String.valueOf(account.getAid()));
        nameView.setText(account.getName());
        messageView.setText(account.getMessageString());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertView);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            if (mListner != null)
                mListner.updateAccount(new Account(account.getAid(), nameView.getText().toString(), messageView.getText().toString()));
            requireActivity().startActivity(new Intent(requireActivity(),MainActivity.class));
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.border);
        }
        alertDialog.show();    }

    public void removePreviousTableStyles(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.parent); // Replace with your actual parent layout ID
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View child = parentLayout.getChildAt(i);
            if (child instanceof TableLayout) {
                TableLayout tableLayout = (TableLayout) child;
                tableLayout.setOnClickListener(v -> {
                    // Deselect all tables and remove primary label
                    for (int j = 0; j < parentLayout.getChildCount(); j++) {
                        View innerChild = parentLayout.getChildAt(j);
                        if (innerChild instanceof TableLayout) {
                            TableLayout tl = (TableLayout) innerChild;
                            tl.setBackgroundResource(R.drawable.border); // Remove border

                            // Remove primary label if exists
                            TextView label = (TextView) tl.findViewWithTag("primary_label");
                            if (label != null) {
                                tl.removeView(label);
                            }
                        }
                    }
                    addSelectionLayout(tableLayout);
                });

            }
        }

    }
}