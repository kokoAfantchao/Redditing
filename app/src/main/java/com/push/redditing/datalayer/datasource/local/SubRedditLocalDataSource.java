package com.push.redditing.datalayer.datasource.local;

import android.app.Application;
import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SubRedditLocalDataSource implements SubRedditDataSource {

   private ContentResolver  mContentResolver;

   @Inject
   public SubRedditLocalDataSource(ContentResolver  mContentResolver){
       this.mContentResolver = mContentResolver;

   }


    @Nullable
    @Override
    public List<Subreddit> getSubreddits() {
        return null;
    }

    @Override
    public Integer saveSubReddits(List<Subreddit> subredditList) {
        return null;
    }


    @Nullable
    @Override
    public List<Submission> getSubmission(@NonNull String SubReddit_fullname) {
        return null;
    }

    @Override
    public Integer saveSubmission(List<Submission> submissions) {
        return null;
    }


}
