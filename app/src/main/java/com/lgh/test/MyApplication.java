package com.lgh.test;

import android.app.Application;

import com.bulong.rudeness.RudenessScreenHelper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new RudenessScreenHelper(this, 1920).activate();
    }
}
