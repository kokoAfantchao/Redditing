package com.push.redditing.datalayer.datasource.local;

import android.app.Application;
import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.utils.AppExecutors;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SubRedditLocalDataSource implements SubRedditDataSource {

  private ContentResolver  mContentResolver;
  private AppExecutors mAppExecutors;

   @Inject
   public SubRedditLocalDataSource(ContentResolver  mContentResolver, AppExecutors executors){
       this.mContentResolver = mContentResolver;
       this.mAppExecutors = executors;
   }


    @Override
    public void getSubreddits(LoadSubredditCallback loadSubredditCallback) {

    }

    @Override
    public void saveSubReddits(List<Subreddit> subredditList) {

    }

    @Override
    public void getSubmissions(@NonNull String SubReddit_fullname, LoadSubmissionCallback loadSubmissionCallback) {

    }

    @Override
    public void saveSubmission(List<Submission> submissions) {

    }

    @Override
    public void postSubmission(Post post, PostSubmissionCallback callback) {

    }

   @Override
   public void getComments(String submissionId, LoadCommentCallback callback) {

   }
}
