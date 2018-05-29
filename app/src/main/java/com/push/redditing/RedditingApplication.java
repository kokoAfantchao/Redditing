package com.push.redditing;


import android.util.Log;
import com.push.redditing.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
//import kotlin.Unit;
//import kotlin.jvm.functions.Function1;
//import net.dean.jraw.RedditClient;
//import net.dean.jraw.android.*;
//import net.dean.jraw.http.LogAdapter;
//import net.dean.jraw.http.SimpleHttpLogger;
//import net.dean.jraw.oauth.AccountHelper;

import java.util.UUID;


public class RedditingApplication extends DaggerApplication {

//    for test perpose only
//    @Inject
//    TasksRepository tasksRepository;

    //this is used by the Reddit api wrapper to create  user Agent
//    private static AccountHelper accountHelper;
//    private static SharedPreferencesTokenStore tokenStore;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }


//////    @Override
//////    public void onCreate() {
//////     super.onCreate();
//////        AppInfoProvider provider = new ManifestAppInfoProvider(getApplicationContext());
//////
//////        // Ideally, this should be unique to every device
//////        UUID deviceUuid = UUID.randomUUID();
//////    // 1 Store our access tokens and refresh tokens in shared preferences
//////    // 2 Automatically save new tokens as they arrive
//////    // 3 Load stored tokens into memory
//////    // 4 An AccountHelper manages switching between accounts and into/out of userless mode.
//////        tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
//////        tokenStore.load();
//////        tokenStore.setAutoPersist(true);
//////        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore);
//////        accountHelper.onSwitch(new Function1<RedditClient, Unit>() {
//////            @Override
//////            public Unit invoke(RedditClient redditClient) {
//////                LogAdapter logAdapter = new SimpleAndroidLogAdapter(Log.INFO);
//////                redditClient.setLogger(
//////                        new SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter));
//////
//////                return null;
//////            }
//////        });
////
////
////    }
//
//    public static AccountHelper getAccountHelper() { return accountHelper; }
//    public static SharedPreferencesTokenStore getTokenStore() { return tokenStore; }
}
