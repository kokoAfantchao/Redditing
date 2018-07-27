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
        void onSubmissionLoad(String full_name,List<Submission> submissionList);
        void onDataNotAvailable(String  full_nameß);


    }


    /*
    *
    *
    *
    * */
    void  getSubreddits( LoadSubredditCallback loadSubredditCallback);

    void saveSubReddits(List<Subreddit> subredditList);



    /*
    *
    *
    * */


    void getSubmissions(@NonNull  String SubReddit_fullname, LoadSubmissionCallback  loadSubmissionCallback);

    void saveSubmission(List<Submission> submissions);




}
