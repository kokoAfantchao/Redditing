package com.push.redditing.datalayer.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.push.redditing.datalayer.datasource.Local;
import com.push.redditing.datalayer.datasource.Remote;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.RedditContract;
import com.push.redditing.datalayer.datasource.local.SubRedditLocalDataSource;
import com.push.redditing.datalayer.datasource.remote.SubRedditRemoteDataSource;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;


@Singleton
public class SubRedditRepository  implements SubRedditDataSource{


   private final   SubRedditDataSource localDataSource;
   private final   SubRedditDataSource remoteDataSource;


   Map<String, Subreddit> mCachedSubreddits;
   Map<String, List<Submission>> mCachedSubmission;

   // this  will be check before loading from Remote or Local Database
   boolean subredditCacheIsDirty =  false ;
   boolean submissionCAcheIsDirty = false ;

    @Inject
    public  SubRedditRepository( @NonNull @Local SubRedditDataSource localDataSource,
                                 @NonNull @Remote SubRedditDataSource remoteDataSource ){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }


    @Override
    public void getSubreddits(@NonNull LoadSubredditCallback loadSubredditCallback) {
     Preconditions.checkNotNull(loadSubredditCallback);
      /*
      *Respond immediately from cache is exist and not dirty
      * */
      if(mCachedSubreddits!= null && !subredditCacheIsDirty){
          loadSubredditCallback.onSubredditLoaded(new ArrayList<>(mCachedSubreddits.values()));
          return ;
      }
      if(subredditCacheIsDirty){
          getSubredditsFromRemoteDataSource(loadSubredditCallback);
      }else{
          getSubredditsFromRemoteDataSource(loadSubredditCallback);
          //getSubredditsFromLocalDataSource(loadSubredditCallback);
      }
    }


    private void getSubredditsFromLocalDataSource( final LoadSubredditCallback callback ){
        localDataSource.getSubreddits(new LoadSubredditCallback() {
            @Override
            public void onSubredditLoaded(List<Subreddit> subredditList) {
              callback.onSubredditLoaded(subredditList);
            }

            @Override
            public void onDataNotAvailable() {
              getSubredditsFromRemoteDataSource(callback);
            }
        });
    }

    private void getSubredditsFromRemoteDataSource( final LoadSubredditCallback callback) {
         remoteDataSource.getSubreddits(new LoadSubredditCallback() {
             @Override
             public void onSubredditLoaded(List<Subreddit> subredditList) {
                 refresheCache( subredditList);
                 refresheLocaleDataSource(subredditList);
                 callback.onSubredditLoaded(subredditList);
             }

             @Override
             public void onDataNotAvailable() {
                callback.onDataNotAvailable();
             }
         });



    }

    void refresheCache(List<Subreddit> subredditList){
        if(mCachedSubreddits== null ){
            mCachedSubreddits= new LinkedHashMap<>();
        }
        mCachedSubreddits.clear();
        for (Subreddit subreddit :subredditList){
            mCachedSubreddits.put(subreddit.getFullName(), subreddit);
        }
    }

    void refresheLocaleDataSource(List<Subreddit> subredditList){
      // TODO create  deleteAll and saveAll  methode in {Locale dataSource class }
        //localDataSource.deleteAll(subredditLists )
        //localDAtaSource.savedAll(subredditLists)

    }

    @Override
    public void saveSubReddits(List<Subreddit> subredditList) {

    }

    @Override
    public void getSubmissions(@NonNull String subReddit_fullname, LoadSubmissionCallback loadSubmissionCallback) {
        if( mCachedSubmission != null ) {
            if (mCachedSubmission.get(subReddit_fullname) != null && !submissionCAcheIsDirty) {

                loadSubmissionCallback.onSubmissionLoad(subReddit_fullname, mCachedSubmission.get(subReddit_fullname));
                return;
            }
        }
        if(submissionCAcheIsDirty){
            getSubmissionFromRemoteDataSource(subReddit_fullname ,loadSubmissionCallback);
        }else {
           // getSubmissionFromLocalDataSource(subReddit_fullname,loadSubmissionCallback);
            getSubmissionFromRemoteDataSource(subReddit_fullname ,loadSubmissionCallback);
        }



    }

    private void getSubmissionFromLocalDataSource(String subReddit_fullname, LoadSubmissionCallback loadSubmissionCallback) {
        localDataSource.getSubmissions(subReddit_fullname, new LoadSubmissionCallback() {
            @Override
            public void onSubmissionLoad(String full_name, List<Submission> submissionList) {
                loadSubmissionCallback.onSubmissionLoad(full_name,submissionList);
            }

            @Override
            public void onDataNotAvailable(String  s) {
                getSubmissionFromRemoteDataSource(subReddit_fullname,loadSubmissionCallback);
            }
        });

    }

    private void getSubmissionFromRemoteDataSource(String subReddit_fullname,LoadSubmissionCallback loadSubmissionCallback) {
        remoteDataSource.getSubmissions(subReddit_fullname, new LoadSubmissionCallback() {
            @Override
            public void onSubmissionLoad(String full_name, List<Submission> submissionList) {
                loadSubmissionCallback.onSubmissionLoad( full_name , submissionList);
            }

            @Override
            public void onDataNotAvailable(String s ) {

            }
        });
    }


    @Override
    public void saveSubmission(List<Submission> submissions) {

    }
}
