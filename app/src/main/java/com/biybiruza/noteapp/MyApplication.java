package com.biybiruza.noteapp;

import android.app.Application;
import android.text.format.DateFormat;


import java.util.Calendar;
import java.util.Locale;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String formatTimeStamp(Long timeStamp)  {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp);
        //format dd/MM/yyyy
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }
}
