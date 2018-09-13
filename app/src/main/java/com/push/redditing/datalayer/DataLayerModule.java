package com.push.redditing.datalayer;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.push.redditing.datalayer.datasource.Local;
import com.push.redditing.datalayer.datasource.Remote;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.RedditingDatabase;
import com.push.redditing.datalayer.datasource.local.SubRedditLocalDataSource;
import com.push.redditing.datalayer.datasource.local.SubredditDao;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import com.push.redditing.datalayer.datasource.remote.SubRedditRemoteDataSource;
import com.push.redditing.datalayer.repository.OAuthRepository;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import com.push.redditing.utils.AppExecutors;
import com.push.redditing.utils.DiskIOThreadExecutor;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.Executors;

@Module
abstract public class DataLayerModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Binds
    @Local
    abstract SubRedditDataSource provideSubRedditLocalDataSource(SubRedditLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract SubRedditDataSource provideSubRedditREmoteDataSource(SubRedditRemoteDataSource dataSource);


    @Singleton
    @Provides
    static public SubRedditRepository provideSubRedditRepository(@Remote SubRedditDataSource remoteDataSource,
                                                                 @Local SubRedditDataSource localDataSource) {
        return new SubRedditRepository(localDataSource, remoteDataSource);
    }

    @Singleton
    @Provides
    static RedditingDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), RedditingDatabase.class, "Tasks.db")
                .build();
    }


    @Singleton
    @Provides
    static SubredditDao provideTasksDao(RedditingDatabase db) {
        return db.subredditDao();
    }



    @Singleton
    @Provides
    static public ApiService provideOAuthService() {
        return new ApiService();
    }

    @Singleton
    @Provides
    static public  OAuthRepository provideOAuthRepository(ApiService apiService){
        return  new OAuthRepository(apiService);
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }


}
