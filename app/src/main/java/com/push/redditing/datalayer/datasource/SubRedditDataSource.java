package com.push.redditing.datalayer.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.tree.CommentNode;

import java.util.List;

public interface SubRedditDataSource {

    /*
    *
    *
    *
    * */
    void  getSubreddits( LoadSubredditCallback loadSubredditCallback);

    void saveSubReddits(List<Subreddit> subredditList);

    interface LoadSubredditCallback {
        void onSubredditLoaded(List<Subreddit> subredditList);
        void onDataNotAvailable();
        void onRedditClientNull();
    }



    /*
    *
    *
    * */
    void getSubmissions(@NonNull  String SubReddit_fullname, LoadSubmissionCallback  loadSubmissionCallback);

    void saveSubmission(List<Submission> submissions);


    interface LoadSubmissionCallback{
        void onSubmissionLoad(String full_name,List<Submission> submissionList);
        void onDataNotAvailable(String  full_nameß);

    }

    void postSubmission(Post post, PostSubmissionCallback callback);


    interface  PostSubmissionCallback{
        void onPostSuccess(Submission submission);
        void onPostFailed();
    }



    /*
    *
    *
    *
    *
    * */
    void getComments(String submissionId, LoadCommentCallback callback);

    interface LoadCommentCallback{
        void onCommentsLoad (List < CommentNode < Comment >> comments);
        void onDataNotAvailable();
    }

}
