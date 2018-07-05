package com.push.redditing.ui.main;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.push.redditing.datalayer.datasource.loaders.SubRedditsLoader;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import com.push.redditing.di.ActivityScoped;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import java.util.List;

@ActivityScoped
final class  MainPresenter  implements  MainContract.Presenter, LoaderManager.LoaderCallbacks<List<Subreddit>> {

    private final static int SUBREDDITS_QUERY = 1;
    private SubRedditsLoader redditsLoader;
    private List<Subreddit> mCurrentSubreddits;


    @Nullable
    private MainContract.View  mMainView;


    @Inject
    public MainPresenter(@NonNull SubRedditsLoader redditsLoader) {

        this.redditsLoader = redditsLoader;
    }

    @Override
    public void LoadSubreddits() {

    }

    @Override
    public void takeView(MainContract.View view) {
        mMainView= view;
    }

    public void start(LoaderManager loaderManager){
      loaderManager.initLoader(SUBREDDITS_QUERY,null, this);
    }



    @Override
    public void dropView() {
        mMainView= null;
    }


    @NonNull
    @Override
    public Loader<List<Subreddit>> onCreateLoader(int id, @Nullable Bundle args) {
     //   mMainView.showLoadingIndicator(true);
        return redditsLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Subreddit>> loader, List<Subreddit> data) {
        //mMainView.showLoadingIndicator(false);
        mCurrentSubreddits= data;
        if(mCurrentSubreddits== null){
            // there is no subcribed  subreddit
        }else{
           mMainView.showTabs(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Subreddit>> loader) {
     //
    }

    public void reSetLoader(LoaderManager supportLoaderManager) {
        supportLoaderManager.restartLoader(SUBREDDITS_QUERY,null, this );
    }
}
