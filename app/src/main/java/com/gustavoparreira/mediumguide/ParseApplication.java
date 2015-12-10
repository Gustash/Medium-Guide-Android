package com.gustavoparreira.mediumguide;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YOUR_APPLICATION_ID", "YOUR_CLIENT_KEY");
    }

}