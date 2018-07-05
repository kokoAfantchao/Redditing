package com.push.redditing.ui.main.SubReddit;

import com.push.redditing.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public  abstract  class SubRedditModule {

    @FragmentScoped
    @Binds
    abstract SubRedditContract.Presenter SubRedditPresenter(SubRedditPresenter presenter);

}
