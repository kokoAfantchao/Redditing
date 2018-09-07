package com.push.redditing.ui.main;


import com.push.redditing.di.ActivityScoped;
import com.push.redditing.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract  class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment LoginFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainFragment MainFragment();

    @ActivityScoped
    @Binds
    abstract MainContract.Presenter MainPrensenter(MainPresenter  presenter);

}
