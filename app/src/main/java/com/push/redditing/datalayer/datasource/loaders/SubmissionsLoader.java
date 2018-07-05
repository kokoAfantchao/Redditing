package com.push.redditing.datalayer.datasource.loaders;

import android.app.Application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import net.dean.jraw.models.Submission;

import javax.inject.Inject;
import java.util.List;

public class SubmissionsLoader extends AsyncTaskLoader<List<Submission>> implements SubRedditRepository.SubRedditsRepositoryObserver {

    @Inject
    SubRedditRepository subRedditRepository;

    @Inject
    Application application;

    private  String fullName ;

    public SubmissionsLoader(@NonNull Context  context ,String  fullName) {
        super(context);
        this.fullName=fullName;
    }


    @Override
    public List<Submission> loadInBackground() {
        return subRedditRepository.getSubmission(fullName);
    }


    @Override
    protected void onStartLoading() {
        //check is any data available in cache
        if(subRedditRepository.cachedSubmissionAvailable()){
            deliverResult(subRedditRepository.getCachedSubmission(fullName));
        }

        subRedditRepository.addContentObserver(this);

        if(takeContentChanged() || !subRedditRepository.cachedSubmissionAvailable()){
            forceLoad();
        }
    }
    @Override
    public void onSubRedditsChanged() {

    }
}
