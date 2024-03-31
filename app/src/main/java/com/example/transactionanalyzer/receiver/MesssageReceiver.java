package com.example.transactionanalyzer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.transactionanalyzer.entity.Account;
import com.example.transactionanalyzer.repository.AccountManager;
import com.example.transactionanalyzer.repository.TransactionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MesssageReceiver extends BroadcastReceiver {
    public TransactionManager transactionManager;
    public AccountManager accountManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        transactionManager=new TransactionManager(context);
        accountManager = new AccountManager(context);
        SmsMessage[] smsMessages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage smsMessage:smsMessages){
            String message_body =smsMessage.getMessageBody();
            for(Account account:accountManager.getAccounts()) {
                if (smsMessage.getDisplayOriginatingAddress().contains(account.getMessageString())) {
                    String amount = extractAmount(message_body);
                    Log.d("Amount", "onReceive: " + amount);
                    long amount1 = amount != null ? (long) Double.parseDouble(amount) : 0;
                    if (message_body.toLowerCase().contains("debited"))
                        transactionManager.addDebitTransaction(amount1, "",account.getAid());
                    else
                        transactionManager.addCreditTransaction(amount1, "",account.getAid());
                }
            }
        }
    }
    private String extractAmount(String messageBody) {
        // Regular expression pattern to match currency and amount format
        Pattern pattern = Pattern.compile("(\\s*(\\d+(?:\\.\\d{1,2})?))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(messageBody);

        // If a match is found, return the amount
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null; // return null if amount not found
    }
}

