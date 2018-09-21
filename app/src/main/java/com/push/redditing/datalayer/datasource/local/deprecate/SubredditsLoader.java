package com.push.redditing.datalayer.datasource.local.deprecate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.datalayer.repository.SubRedditRepository;

import java.util.List;

public class SubredditsLoader extends AsyncTaskLoader<List<LSubreddit>>{

    private SubRedditRepository mRedditRepository;
    private List<LSubreddit> lSubredditList= null;
    private SubRedditDataSource.LoadSubredditCallback mLoadSubredditCallback;


    public SubredditsLoader(@NonNull Context context, SubRedditRepository mRedditRepository) {
        super(context);
        this.mRedditRepository = mRedditRepository;
    }


    @Override
    protected void onStartLoading() {
        if (lSubredditList != null) {
            deliverResult(lSubredditList);
        }else {
            forceLoad();
        }

    }

    @Nullable
    @Override
    public List<LSubreddit> loadInBackground(){
        lSubredditList = mRedditRepository.getSubreddits();
        return  lSubredditList ;
    }

    @Override
    public void deliverResult(List<LSubreddit> data) {
            lSubredditList = data;
        super.deliverResult(data);
    }
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
    }


}
