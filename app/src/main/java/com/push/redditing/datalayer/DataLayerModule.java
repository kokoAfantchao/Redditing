package com.push.redditing.datalayer;

import com.push.redditing.datalayer.datasource.Local;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.SubRedditLocalDataSource;
import com.push.redditing.datalayer.repository.OAuthService;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
abstract public class DataLayerModule {

    @Singleton
    @Binds
    @Local
    abstract SubRedditDataSource provideSubRedditLocalDataSource(SubRedditDataSource dataSource);

//
//    @Singleton
//    @Binds
//    abstract OAuthService provideOAuthService( OAuthService oAuthService);
//


}
