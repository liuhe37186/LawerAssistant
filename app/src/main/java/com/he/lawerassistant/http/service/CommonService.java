package com.he.lawerassistant.http.service;

import com.he.lawerassistant.http.bean.UpdateBean;
import com.he.lawerassistant.http.response.Response;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommonService {
    @POST("/api/v1.0/app/update")
    Observable<Response<UpdateBean>> getChuKuXiaoXi(@Query("version_code") int code);
}
