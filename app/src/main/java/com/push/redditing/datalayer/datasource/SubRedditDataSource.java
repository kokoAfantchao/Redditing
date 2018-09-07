package com.push.redditing.datalayer.datasource;

import android.support.annotation.NonNull;

import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.CommentNode;

import java.util.List;

public interface SubRedditDataSource {

    /*
    *
    *
    *
    * */
    void  getSubreddits( LoadSubredditCallback loadSubredditCallback);

    void saveSubReddits(List<LSubreddit> subredditList);

    void deletAllSubreddits();


    interface LoadSubredditCallback {
        void onSubredditLoaded(List<LSubreddit> subredditList);
        void onDataNotAvailable();
        void onRedditClientNull();
    }



    /*
    *
    *
    * */
    void getSubmissions(@NonNull  String SubReddit_fullname, LoadSubmissionCallback  loadSubmissionCallback);

    void saveSubmission(List<Submission> submissions);


    interface LoadSubmissionCallback {

        void onSubmissionLoad(String full_name, List<LSubmission> submissionList);

        void onDataNotAvailable(String full_nameß);

        void onRedditClientNull();

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
