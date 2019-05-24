package com.corelibs.api;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private static String BASE_URL;
    private static String TOKEN = "";
    private static String PLATFORM = "Android";
    private static String USER_ID = "";

    private Retrofit mRetrofit;
    OkHttpClient.Builder okHttpBuilder;

    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }


    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitManager() {
        initClient();
    }

    private void initClient() {
        // 创建 OKHttpClient
        okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        okHttpBuilder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
        HttpCommonInterceptor headerInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("Platform", PLATFORM)
                .addHeaderParams("Token", TOKEN)
                .addHeaderParams("UserId", USER_ID)
                .build();

        okHttpBuilder.addInterceptor(headerInterceptor);
        okHttpBuilder.addInterceptor(new HttpLoggingInterceptor());
        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public void setToken(String token,String userId) {
        if(!TextUtils.isEmpty(token)){
            TOKEN = token;
        }
        if(!TextUtils.isEmpty(userId)){
            USER_ID = userId;
        }
        initClient();
    }

    public static void init(String url) {
        BASE_URL = url;
    }

    public <T> T create(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }
}
