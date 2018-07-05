package com.push.redditing.datalayer.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import java.util.List;

public interface SubRedditDataSource {

    interface LoadSubredditCallback {
        void onSubredditLoaded(List<Subreddit> subredditList);
        void onDataNotAvailable();
    }
    interface LoadSubmissionCallback{
        void onSubmissionLoad(List<Submission> submissionList);
        void onDataNotAvailable();


    }


    /*
    *
    *
    *
    * */
    @Nullable
    List<Subreddit> getSubreddits();

    Integer saveSubReddits(List<Subreddit> subredditList);



    /*
    *
    *
    * */
    @Nullable
    List<Submission> getSubmission(@NonNull  String SubReddit_fullname);

    Integer saveSubmission(List<Submission> submissions);




}
