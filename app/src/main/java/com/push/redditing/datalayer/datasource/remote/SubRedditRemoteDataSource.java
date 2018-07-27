package com.push.redditing.datalayer.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.utils.AppExecutors;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SubRedditRemoteDataSource implements SubRedditDataSource{

    private ApiService apiService;
    private AppExecutors mAppExecutors;

    @Inject
    public SubRedditRemoteDataSource(@NonNull ApiService apiService, AppExecutors executors ){
     this.apiService= apiService;
     this.mAppExecutors =executors;
    }


    @Override
    public void getSubreddits(LoadSubredditCallback loadSubredditCallback) {
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {
                List<Subreddit> subredditList = apiService.getUserSubReddit();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!subredditList.isEmpty()) loadSubredditCallback.onSubredditLoaded(subredditList);
                        else loadSubredditCallback.onDataNotAvailable();
                    }
                });

            }
        };
        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void saveSubReddits(List<Subreddit> subredditList) {

    }

    @Override
    public void getSubmissions(@NonNull String SubReddit_fullname, LoadSubmissionCallback loadSubmissionCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Timber.d(" getSubmission running in back ground******");
                List<Submission> submissions = apiService.getSubmission(SubReddit_fullname);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (submissions!= null ) loadSubmissionCallback.onSubmissionLoad(SubReddit_fullname, submissions);
                        else loadSubmissionCallback.onDataNotAvailable(SubReddit_fullname);
                    }
                });
            }
        };
        mAppExecutors.networkIO().execute(runnable);

    }



    @Override
    public void saveSubmission(List<Submission> submissions) {

    }
}
