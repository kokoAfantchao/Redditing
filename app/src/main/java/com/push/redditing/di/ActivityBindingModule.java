package com.push.redditing.di;


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



}
