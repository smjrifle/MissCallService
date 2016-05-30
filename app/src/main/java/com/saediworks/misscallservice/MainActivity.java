package com.saediworks.misscallservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Activity activity;
    public String i = "0";
    public Button btn, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.smstoggle);
        btn2 = (Button) findViewById(R.id.viewlog);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewLog.class);
                startActivity(intent);
            }
        });

        String filename = "smssettings";
        try {
            InputStream inputStream = openFileInput(filename);

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
                if (Integer.valueOf(i) == 0) {
                    btn.setText("Enable Sms");
                } else {
                    btn.setText("Disable Sms");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(i) == 0) {
                    i = "1";
                    btn.setText("Disable Sms");
                } else {
                    i = "0";
                    btn.setText("Enable Sms");
                }
                String filename = "smssettings";
                String string = i;
                FileOutputStream outputStream;

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput(filename, Context.MODE_PRIVATE));
                    outputStreamWriter.write(string);
                    outputStreamWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
