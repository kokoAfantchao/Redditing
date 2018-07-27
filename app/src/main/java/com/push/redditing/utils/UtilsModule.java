package com.push.redditing.utils;

import android.app.Application;
import android.content.ContentResolver;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public abstract class UtilsModule {

    @Singleton
    @Provides
    public static ContentResolver provideContentprovider(Application application){
      return     application.getContentResolver();
    }

    @Singleton
    @Provides
    public static  PreferencesHelper providePreferencesHelper(Application application){
        return new PreferencesHelper(application.getBaseContext());
    }





}
