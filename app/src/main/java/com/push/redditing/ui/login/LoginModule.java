package com.push.redditing.ui.login;

import com.push.redditing.di.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public abstract  class LoginModule {

    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter loginPrensenter(LoginPresenter presenter);
}
