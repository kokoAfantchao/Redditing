package com.push.redditing.ui.login;

import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showWebview(String Url);
        void showLoginError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {
        void gernarateOauthUrl();

    }
}
