package com.he.lawerassistant.contract;

import com.corelibs.base.BaseView;

public interface LoginContract {
    interface Model {
    }

    interface View extends BaseView {
        void onLogin();
    }

    interface Presenter{
        void doLogin(String phone,String pwd);
    }
}
