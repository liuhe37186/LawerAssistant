package com.he.lawerassistant;

import android.app.Application;

import com.he.lawerassistant.utils.Utils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
