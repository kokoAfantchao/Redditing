package com.push.redditing.datalayer;

import android.app.Application;
import com.push.redditing.datalayer.datasource.Local;
import com.push.redditing.datalayer.datasource.Remote;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.loaders.SubRedditsLoader;
import com.push.redditing.datalayer.datasource.local.SubRedditLocalDataSource;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import com.push.redditing.datalayer.datasource.remote.SubRedditRemoteDataSource;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
abstract public class DataLayerModule {


    @Singleton
    @Binds
    @Local
    abstract SubRedditDataSource provideSubRedditLocalDataSource(SubRedditLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract  SubRedditDataSource provideSubRedditREmoteDataSource(SubRedditRemoteDataSource dataSource);


    @Singleton
    @Provides
    static  public SubRedditRepository provideSubRedditRepository(@Remote SubRedditDataSource remoteDataSource,
                                                                  @Local SubRedditDataSource localDataSource){
        return  new SubRedditRepository(localDataSource,remoteDataSource);
    };



    @Singleton
    @Provides
    static public ApiService provideOAuthService(){
        return  new ApiService();
    }


    @Provides
    static public  SubRedditsLoader provideSubRedditsLoader(Application application, SubRedditRepository subRedditRepository){
        return new SubRedditsLoader(application.getApplicationContext(),subRedditRepository);
    };




}
