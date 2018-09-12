package com.push.redditing.ui.SubmissionDetail;

import com.push.redditing.di.ActivityScoped;
import com.push.redditing.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SubmissionModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SubmissionFragment submissionFragment();

    @ActivityScoped
    @Binds
    abstract SubmissionContract.Prenseter SubmissionPresenter(SubmissionPresenter presenter);
}
