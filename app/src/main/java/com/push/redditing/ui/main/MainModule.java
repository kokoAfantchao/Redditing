package com.push.redditing.ui.main;


import com.push.redditing.di.ActivityScoped;
import com.push.redditing.di.FragmentScoped;
import com.push.redditing.ui.main.SubReddit.SubRedditFragment;
import com.push.redditing.ui.main.SubReddit.SubRedditModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract  class MainModule {



    @ActivityScoped
    @Binds
    abstract MainContract.Presenter MainPrensenter(MainPresenter  presenter);

}
