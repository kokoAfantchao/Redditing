package com.push.redditing.datalayer.datasource.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.datalayer.datasource.local.deprecate.RedditContract;
import com.push.redditing.utils.AppExecutors;
import net.dean.jraw.models.Submission;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class SubRedditLocalDataSource implements SubRedditDataSource {

    private AppExecutors mAppExecutors;
    private ContentResolver mContentResolver;

    public interface Query {
        String[] PROJECTION = {
                RedditContract.SubReddits._ID,
                RedditContract.SubReddits.FULL_NAME,
                RedditContract.SubReddits.BANNER_IMAGE,
                RedditContract.SubReddits.DISPLAY_NAME,
                RedditContract.SubReddits.SUBSCRIBERS,
        };
    }

    int _ID = 0;
    int FULL_NAME = 1;
    int BANNER_IMAGE = 2;
    int DISPLAY_NAME = 3;
    int SUBSCRIBERS = 4;
//    int PUBLIC_DESCRIPTION = 5;


    @Inject
    public SubRedditLocalDataSource(ContentResolver contentResolverCompat, AppExecutors executors) {
//       this.mSubredditDao = subredditDao;
        this.mAppExecutors = executors;
        this.mContentResolver = contentResolverCompat;
    }


    @Override
    public void getSubreddits(LoadSubredditCallback loadSubredditCallback) {
        Runnable runnable = () -> {
            List<LSubreddit> subreddits =  new ArrayList<>();
            Cursor cursor = mContentResolver.query(RedditContract.SubReddits.buildDirUri(),
                    Query.PROJECTION,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                LSubreddit lSubreddit = lSubredditValueOf(cursor);
                subreddits.add(lSubreddit);
            }
            if (cursor != null) {
                cursor.close();
            }

            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    if (subreddits.size() > 0)
                        loadSubredditCallback.onSubredditLoaded(subreddits);
                    else loadSubredditCallback.onDataNotAvailable();
                }
            });
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveSubReddits(List<LSubreddit> subredditList) {
        Runnable runnable = () -> {
            ContentValues[] contentValues = new ContentValues[subredditList.size()];
            int i = 0;
            for (LSubreddit subreddit : subredditList) {
                contentValues [i++] = contentValueOf(subreddit);
                Timber.d("Saving Subreddit here looo +"+ subreddit.toString());
            }

            int insertions  = mContentResolver.bulkInsert(RedditContract.SubReddits.buildDirUri(), contentValues);

        };
        mAppExecutors.diskIO().execute(runnable);


    }

    @Override
    public void deletAllSubreddits() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mContentResolver.delete(RedditContract.SubReddits.buildDirUri(), null, null);
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

    private LSubreddit lSubredditValueOf(@NonNull Cursor cursor) {
        LSubreddit lSubreddit = new LSubreddit();
        lSubreddit.setFullName(cursor.getString(FULL_NAME));
        lSubreddit.setName(cursor.getString(DISPLAY_NAME));
        lSubreddit.setBannerImage(cursor.getString(BANNER_IMAGE));
       // lSubreddit.setPublicDescription(cursor.getString(PUBLIC_DESCRIPTION));
        lSubreddit.setSubscribers(cursor.getInt(SUBSCRIBERS));

        return lSubreddit;
    }

    private  ContentValues contentValueOf(LSubreddit lSubreddit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RedditContract.SubReddits.FULL_NAME,lSubreddit.getFullName());
        contentValues.put(RedditContract.SubReddits.DISPLAY_NAME,lSubreddit.getName());
        contentValues.put(RedditContract.SubReddits.BANNER_IMAGE,lSubreddit.getBannerImage());
        ///contentValues.put(RedditContract.SubReddits.PUBLIC_DESCRIPTION, lSubreddit.getPublicDescription());
        contentValues.put(RedditContract.SubReddits.SUBSCRIBERS,lSubreddit.getSubscribers());
        return contentValues;
    }



}
