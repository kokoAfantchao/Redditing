package com.push.redditing.ui.login;

import com.push.redditing.datalayer.repository.OAuthService;
import com.push.redditing.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final  class LoginPresenter implements LoginContract.Presenter {


    OAuthService  mOAuthService;

    private LoginContract.View mloginView;

    @Inject
    public LoginPresenter(OAuthService mOAuthService) {
        this.mOAuthService = mOAuthService;
    }

    @Override
    public void takeView(LoginContract.View view) {
      this.mloginView =view;
    }


    @Override
    public void dropView() {
        mloginView= null ;

    }

    @Override
    public void gernarateOauthUrl() {
       // String authorizationUlr = mOAuthService.getAuthorizationUlr();
      //  mloginView.showWebview(authorizationUlr);

    }
}
