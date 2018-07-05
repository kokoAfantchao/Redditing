package com.push.redditing.di;

import com.push.redditing.datalayer.DataLayerModule;
import com.push.redditing.ui.login.LoginActivity;
import com.push.redditing.ui.login.LoginModule;
import com.push.redditing.ui.main.MainActivity;
import com.push.redditing.ui.main.MainContract;
import com.push.redditing.ui.main.MainModule;
import com.push.redditing.ui.main.SubReddit.SubRedditFragment;
import com.push.redditing.ui.main.SubReddit.SubRedditModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

//    @ActivityScoped
//    @ContributesAndroidInjector(modules = TasksModule.class)
//    abstract TasksActivity tasksActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();




}
