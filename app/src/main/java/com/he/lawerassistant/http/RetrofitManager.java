package com.he.lawerassistant.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private Retrofit mRetrofit;

    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitManager() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

//        // 添加公共参数拦截器
//        Interceptor commonInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request.Builder requestBuilder = originalRequest.newBuilder()
//                        .addHeader("Accept-Encoding", "gzip")
//                        .addHeader("Accept", "application/json")
//                        .addHeader("Content-Type", "application/json; charset=utf-8")
//                        .method(originalRequest.method(), originalRequest.body());
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//
//        };

        HttpCommonInterceptor interceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("Accept-Encoding", "gzip")
                .addHeaderParams("Accept", "application/json")
                .addHeaderParams("Content-Type", "application/json; charset=utf-8")
                .build();

        builder.addInterceptor(interceptor);

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();

    }

    public <T> T create(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }
}
