package com.push.redditing.di;


import com.push.redditing.ui.Post.PostActivity;
import com.push.redditing.ui.Post.PostModule;
import com.push.redditing.ui.SubmissionDetail.SubmissionActivity;
import com.push.redditing.ui.SubmissionDetail.SubmissionModule;
import com.push.redditing.ui.main.MainActivity;
import com.push.redditing.ui.main.MainModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

//    @ActivityScoped
//    @ContributesAndroidInjector(modules = TasksModule.class)
//    abstract TasksActivity tasksActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PostModule.class})
    abstract PostActivity postActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {SubmissionModule.class})
    abstract SubmissionActivity submissionActivity();



}
