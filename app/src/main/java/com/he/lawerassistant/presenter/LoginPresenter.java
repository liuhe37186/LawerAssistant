package com.he.lawerassistant.presenter;

import android.text.TextUtils;

import com.corelibs.api.RetrofitManager;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.he.lawerassistant.contract.LoginContract;
import com.he.lawerassistant.http.bean.UpdateBean;
import com.he.lawerassistant.http.response.Response;
import com.he.lawerassistant.http.response.ResponseTransformer;
import com.he.lawerassistant.http.service.CommonService;
import com.he.lawerassistant.utils.LogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {


    @Override
    public void doLogin(String phone, String pwd) {
        view.showLoading();
        RetrofitManager.getInstance().create(CommonService.class)
                .doLogin(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseSubscriber<Response<UpdateBean>>(view) {
                    @Override
                    public void success(Response<UpdateBean> updateBeanResponse) {
                        LogUtil.e("xxxx",updateBeanResponse.toString());
                        String token = updateBeanResponse.getData().getToken();
                        String userId = updateBeanResponse.getData().getUserId()+"";
                        if(!TextUtils.isEmpty(token)){
                            RetrofitManager.getInstance().setToken(token,userId);
                        }
                        view.showEmptyHint();
                    }

                });

    }

    @Override
    public void onStart() {

    }
}
