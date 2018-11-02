package com.he.lawerassistant.http;

import android.content.Context;

import com.he.lawerassistant.utils.NetworkUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018/4/26 0026.
 */
public class RetrofitManager {
        private Context context;
        private File httpCacheDirectory;
        private  int cacheSize = 10 * 1024 * 1024; // 10 MiB
        private Cache cache;
        private OkHttpClient client;
        private Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR;
        public RetrofitManager(Context context) {
            this.context = context;
            this.httpCacheDirectory=new File(context.getCacheDir(), "responses");
            this.cache = new Cache(httpCacheDirectory, cacheSize);
            this.REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);
                cacheBuilder.maxStale(365, TimeUnit.DAYS);
                CacheControl cacheControl = cacheBuilder.build();

                Request request = chain.request();
                if(!NetworkUtil.isAvailable(context)){
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetworkUtil.isAvailable(context)) {
                    int maxAge = 0; // read from cache
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public ,max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            };
            this.client=new OkHttpClient.Builder()
                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .cache(cache).build();
        }
        /**
         * 配置网络缓存
         */
//        public  final String API_BASE_URL = "http://192.168.0.201:8080/";
        private  Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        public  <S> S createService(Class<S> serviceClass) {
            Retrofit retrofit = builder.client(client).build();
            return retrofit.create(serviceClass);
        }
}
