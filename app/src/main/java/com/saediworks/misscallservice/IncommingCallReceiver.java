package com.saediworks.misscallservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by smjrifle on 5/30/16.
 */
public class IncommingCallReceiver extends BroadcastReceiver {

    private static boolean ring = false;
    private static boolean callReceived = false;
    private static String callerPhoneNumber;
    String string_out = "", i = "";


    @Override
    public void onReceive(Context mContext, Intent intent) {

        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d("state ", state);

        if (state == null)
            return;

        // If phone state "Rininging"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            ring = true;
            // Get the Caller's Phone Number
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d("incoming number : ", number);
            callerPhoneNumber = number;

        }


        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if (ring == true && callReceived == false) {
                Toast.makeText(mContext, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                String filename = "smssettings";
                try {
                    InputStream inputStream = mContext.openFileInput(filename);

                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ((receiveString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(receiveString);
                        }

                        inputStream.close();
                        i = stringBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Integer.valueOf(i) == 1) {
                    try {
                        sendSMS(callerPhoneNumber, "Hello, The wifi username is 'kmjmail' and password is 'smjrifle12345'");
                        Log.d("Sms Sent", "Sms Sent to " + callerPhoneNumber);

                        String filename2 = "smsfile";

                        try {
                            InputStream inputStream = mContext.openFileInput(filename2);

                            if (inputStream != null) {
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();

                                while ((receiveString = bufferedReader.readLine()) != null) {
                                    stringBuilder.append(receiveString);
                                }

                                inputStream.close();
                                string_out = stringBuilder.toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String string = string_out + "\n" + "Sms Sent to " + callerPhoneNumber;
                        FileOutputStream outputStream;

                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput(filename2, Context.MODE_PRIVATE));
                            outputStreamWriter.write(string);
                            outputStreamWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        Log.d("Sms Error", e.toString());
                    }

                }

            }
            ring = false;
            callReceived = false;
        }

    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}