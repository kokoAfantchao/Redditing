package com.push.redditing.datalayer.datasource.loaders;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
@Singleton
public class SubRedditsLoader extends AsyncTaskLoader<List<Subreddit>> implements SubRedditRepository.SubRedditsRepositoryObserver {

    private SubRedditRepository mSubRedditRepository;


    public SubRedditsLoader(@NonNull Context context,SubRedditRepository subRedditRepository ) {
        super(context);
        this.mSubRedditRepository=subRedditRepository;
    }

    @Nullable
    @Override
    public List<Subreddit> loadInBackground() {
        return mSubRedditRepository.getSubreddits();
    }

    @Override
    protected void onStartLoading() {
        //check is any data available in cache
        if(mSubRedditRepository.cachedTasksAvailable()){
            deliverResult(mSubRedditRepository.getCachedSubReddits());
        }

        mSubRedditRepository.addContentObserver(this);

        if(takeContentChanged() ||!mSubRedditRepository.cachedTasksAvailable()){
            forceLoad();
        }
    }

    @Override
    public void onSubRedditsChanged() {

        if (isStarted()) forceLoad();
    }

    @Override
    protected void onReset() {
        stopLoading();
        mSubRedditRepository.removeContentObserver(this);
    }

    @Override
    protected void onStopLoading() {
      cancelLoad();
    }
}
