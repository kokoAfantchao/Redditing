package com.push.redditing.utils;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class UtilsModule {

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


    @Singleton
    @Provides
    public static Context provideContext(Application application){
        return  application.getBaseContext();
    }




}
