package com.he.lawerassistant;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.corelibs.api.RetrofitManager;
import com.corelibs.utils.ToastMgr;
import com.he.lawerassistant.http.Constant;
import com.he.lawerassistant.utils.LogUtil;
import com.he.lawerassistant.utils.SharedPreferencesUtil;
import com.he.lawerassistant.utils.Utils;

import static com.he.lawerassistant.http.Constant.BASE_URL;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        RetrofitManager.init(BASE_URL);
        ToastMgr.init(getApplicationContext());

//        HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(GITHUB_DOMAIN_NAME);
//        if (httpUrl == null || !httpUrl.toString().equals(mUrl1.getText().toString())) { //可以在 App 运行时随意切换某个接口的 BaseUrl
//            RetrofitUrlManager.getInstance().putDomain(GITHUB_DOMAIN_NAME, mUrl1.getText().toString());
//        }

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
