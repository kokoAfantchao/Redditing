package com.push.redditing.ui.Post;

import com.push.redditing.di.ActivityScoped;
import com.push.redditing.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PostModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostFragment PostFragment();

    @ActivityScoped
    @Binds
    abstract PostContract.Presenter  PostPresenter(PostPresenter   presenter);
}
