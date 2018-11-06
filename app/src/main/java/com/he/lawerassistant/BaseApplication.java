package com.he.lawerassistant;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

import com.he.lawerassistant.http.Constant;
import com.he.lawerassistant.utils.LogUtil;
import com.he.lawerassistant.utils.SharedPreferencesUtil;
import com.he.lawerassistant.utils.Utils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constant.ISNIGHT, false);
        LogUtil.d("isNight=" + isNight);
        if (isNight) {
            //使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //不使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
