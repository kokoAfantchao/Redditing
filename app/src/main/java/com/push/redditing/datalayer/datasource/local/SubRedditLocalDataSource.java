package com.push.redditing.datalayer.datasource.local;

import android.support.annotation.NonNull;
import com.google.common.util.concurrent.Runnables;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.utils.AppExecutors;
import net.dean.jraw.models.Submission;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SubRedditLocalDataSource implements SubRedditDataSource {

  private AppExecutors mAppExecutors;
  private SubredditDao mSubredditDao;

   @Inject
   public SubRedditLocalDataSource(SubredditDao subredditDao ,  AppExecutors executors){
       this.mSubredditDao = subredditDao;
       this.mAppExecutors = executors;
   }


    @Override
    public void getSubreddits(LoadSubredditCallback loadSubredditCallback) {
       Runnable runnable = () -> {
           List<LSubreddit> subreddits = mSubredditDao.getSubreddits();
           mAppExecutors.mainThread().execute(new Runnable() {
               @Override
               public void run() {
                   if(subreddits!= null && subreddits.size()>0) loadSubredditCallback.onSubredditLoaded(subreddits);
                   else loadSubredditCallback.onDataNotAvailable();
               }
           });
       };
       mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveSubReddits(List<LSubreddit> subredditList) {
       Runnable runnable = () -> {
           for (LSubreddit subreddit : subredditList) {
              mSubredditDao.insertSubreddit(subreddit);
           }
       };
       mAppExecutors.diskIO().execute( runnable);


    }

    @Override
    public void deletAllSubreddits() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mSubredditDao.deleteAllSubreddits();
            }
        };
        mAppExecutors.diskIO().execute(runnable);
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
