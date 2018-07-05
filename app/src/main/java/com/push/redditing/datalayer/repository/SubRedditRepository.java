package com.push.redditing.datalayer.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

   private List<SubRedditsRepositoryObserver> mObservers = new ArrayList<SubRedditsRepositoryObserver>();

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


    public void addContentObserver(SubRedditsRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }
    public void removeContentObserver(SubRedditsRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    @Nullable
    @Override
    public List<Subreddit> getSubreddits() {
        List<Subreddit> subreddits = null ;
        // if cache is not dirty  responde from cache
        if(!subredditCacheIsDirty){
            if(mCachedSubreddits!= null){
                subreddits = getCachedSubReddits();
                return subreddits;
            }else{
        // query local storage  is cache is empty
         subreddits = localDataSource.getSubreddits();
            }
        //if local storage return empty
            if( subreddits==null || subreddits.isEmpty() ){
                subreddits = remoteDataSource.getSubreddits();
                saveSubRedditsInLocalDataSource(subreddits);
            }
        }
        processLoadedTasks(subreddits);
        return getCachedSubReddits();
    }

    @Override
    public Integer saveSubReddits(List<Subreddit> subredditList) {
        return null;
    }

  
    public boolean cachedTasksAvailable() {
        return mCachedSubreddits != null && !subredditCacheIsDirty;
    }
    private void saveSubRedditsInLocalDataSource(List<Subreddit> subreddits) {
        if (subreddits!= null) {
                localDataSource.saveSubReddits(subreddits);
        }
    }

    private void processLoadedTasks(List<Subreddit> subreddits) {
        if (subreddits == null) {
            mCachedSubreddits = null;
            subredditCacheIsDirty = false;
            return;
        }
        if (mCachedSubreddits == null) {
            mCachedSubreddits = new LinkedHashMap<>();
        }
        mCachedSubreddits.clear();
        for (Subreddit subreddit :subreddits) {
            mCachedSubreddits.put(subreddit.getId(), subreddit);
        }
        subredditCacheIsDirty = false;
    }
    public List<Subreddit> getCachedSubReddits() {
        return mCachedSubreddits == null ? null : new ArrayList<>(mCachedSubreddits.values());
    }

    /*
    * Everything data procession(CRUD)  about Submission Start
    *
    *
    * */
    @Nullable
    public List<Submission>   getSubmission( String subReddit_fullname) {
        List<Submission> submissionList = null;
        if(!submissionCAcheIsDirty){
            if(mCachedSubmission != null ){
                submissionList = getCachedSubmission(subReddit_fullname);
                return  submissionList;
            }else {
                submissionList= localDataSource.getSubmission(subReddit_fullname);
            }
            if(submissionList == null || submissionList.isEmpty()){
                submissionList = remoteDataSource.getSubmission(subReddit_fullname);
                saveSubmissionInLocalDataSource(submissionList);
            }
        }
        processLoadSubmissions(subReddit_fullname,submissionList);
        return getCachedSubmission(subReddit_fullname);
    }

    @Override
    public Integer saveSubmission(List<Submission> submissions) {
        return null;
    }
    private void processLoadSubmissions(@NonNull String fullname ,@Nullable List<Submission> submissions){
        if(submissions== null ){
            mCachedSubmission= null ;
            submissionCAcheIsDirty = false;
            return ;
        }
        if (mCachedSubmission == null) {
            mCachedSubmission = new LinkedHashMap<>();
        }
        mCachedSubmission.get(fullname).clear();

        mCachedSubmission.put(fullname,submissions);
        submissionCAcheIsDirty= false;

    }

    private void saveSubmissionInLocalDataSource(List<Submission> submissionList) {
        if(submissionList != null ){
            localDataSource.saveSubmission(submissionList);
        }
    }

    public  List<Submission> getCachedSubmission(String full_name ) {
        List<Submission> submissionCachedList = mCachedSubmission.get(full_name);
        return  submissionCachedList== null ? null :submissionCachedList;
    }

    public boolean cachedSubmissionAvailable() {
        return mCachedSubreddits != null && !subredditCacheIsDirty;
    }

    public interface SubRedditsRepositoryObserver {
        void onSubRedditsChanged();
    }
}
