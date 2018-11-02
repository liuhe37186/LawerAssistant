package com.he.lawerassistant.service;

import com.he.lawerassistant.http.bean.ResponseBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface CommonService {
    @POST("/api/v1.0/app/update")
    Call<ResponseBean> getChuKuXiaoXi(@Query("version_code") String code);
}
