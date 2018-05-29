package com.push.redditing.di;

import com.push.redditing.ui.login.LoginActivity;
import com.push.redditing.ui.login.LoginModule;
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

}
