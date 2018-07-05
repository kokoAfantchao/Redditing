package com.push.redditing.datalayer.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SubRedditRemoteDataSource implements SubRedditDataSource{

    private ApiService apiService;

    @Inject
    public SubRedditRemoteDataSource(@NonNull ApiService apiService ){
     this.apiService= apiService;
    }


    @Nullable
    @Override
    public List<Subreddit> getSubreddits() {
        return apiService.getUserSubReddit();
    }

    @Override
    public Integer saveSubReddits(List<Subreddit> subredditList) {
        return null;
    }


    @Nullable
    @Override
    public List<Submission> getSubmission(@NonNull String subReddit_fullname) {
        return apiService.getSubmission(subReddit_fullname);
    }

    @Override
    public Integer saveSubmission(List<Submission> submissions) {
        return null;
    }
}
