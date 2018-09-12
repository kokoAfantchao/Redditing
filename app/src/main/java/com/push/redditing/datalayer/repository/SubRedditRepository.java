package com.push.redditing.datalayer.repository;

import android.support.annotation.NonNull;
import com.google.common.base.Preconditions;
import com.push.redditing.datalayer.datasource.Local;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.Remote;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.CommentNode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Singleton
public class SubRedditRepository  implements SubRedditDataSource{


   private final   SubRedditDataSource localDataSource;
   private final   SubRedditDataSource remoteDataSource;


   Map<String, LSubreddit> mCachedSubreddits;
   Map<String, List<LSubmission>> mCachedSubmission;

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
          //getSubredditsFromRemoteDataSource(loadSubredditCallback);
          getSubredditsFromLocalDataSource(loadSubredditCallback);
      }
    }


    private void getSubredditsFromLocalDataSource( final LoadSubredditCallback callback ){
        localDataSource.getSubreddits(new LoadSubredditCallback() {
            @Override
            public void onSubredditLoaded(List<LSubreddit> subredditList) {
              callback.onSubredditLoaded(subredditList);
            }

            @Override
            public void onDataNotAvailable() {
              getSubredditsFromRemoteDataSource(callback);
            }

            @Override
            public void onRedditClientNull() {

            }
        });
    }

    private void getSubredditsFromRemoteDataSource( final LoadSubredditCallback callback) {
         remoteDataSource.getSubreddits(new LoadSubredditCallback() {
             @Override
             public void onSubredditLoaded(List<LSubreddit> subredditList) {
                 refresheCache( subredditList);
                 refresheLocaleDataSource(subredditList);
                 callback.onSubredditLoaded(subredditList);
             }

             @Override
             public void onDataNotAvailable() {
                callback.onDataNotAvailable();
             }

             @Override
             public void onRedditClientNull() {
                  callback.onRedditClientNull();
             }
         });



    }

    void refresheCache(List<LSubreddit> subredditList){
        if(mCachedSubreddits== null ){
            mCachedSubreddits= new LinkedHashMap<>();
        }
        mCachedSubreddits.clear();
        for (LSubreddit subreddit :subredditList){
            mCachedSubreddits.put(subreddit.getFullName(), subreddit);
        }
    }

    void refresheLocaleDataSource(List<LSubreddit> subredditList){
      // TODO create  deleteAll and saveAll  methode in {Locale dataSource class }
        localDataSource.deletAllSubreddits();
        localDataSource.saveSubReddits(subredditList);
    }

    @Override
    public void saveSubReddits(List<LSubreddit> subredditList) {

    }

    @Override
    public void deletAllSubreddits() {

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
            public void onSubmissionLoad(String full_name, List<LSubmission> submissionList) {
                loadSubmissionCallback.onSubmissionLoad(full_name,submissionList);
            }

            @Override
            public void onDataNotAvailable(String  s) {
                getSubmissionFromRemoteDataSource(subReddit_fullname,loadSubmissionCallback);
            }

            @Override
            public void onRedditClientNull() {

            }
        });

    }

    private void getSubmissionFromRemoteDataSource(String subReddit_fullname,LoadSubmissionCallback loadSubmissionCallback) {
        remoteDataSource.getSubmissions(subReddit_fullname, new LoadSubmissionCallback() {
            @Override
            public void onSubmissionLoad(String full_name, List<LSubmission> submissionList) {
                loadSubmissionCallback.onSubmissionLoad( full_name , submissionList);
            }

            @Override
            public void onDataNotAvailable(String s ) {
                loadSubmissionCallback.onDataNotAvailable(s);
            }

            @Override
            public void onRedditClientNull() {
                loadSubmissionCallback.onRedditClientNull();
            }
        });
    }


    @Override
    public void saveSubmission(List<Submission> submissions) {

    }

    @Override
    public void postSubmission(Post post, PostSubmissionCallback callback) {
        remoteDataSource.postSubmission(post, new PostSubmissionCallback() {
            @Override
            public void onPostSuccess(Submission submission) {
                callback.onPostSuccess( submission );
            }

            @Override
            public void onPostFailed() {
                callback.onPostFailed();
            }
        });
    }

    @Override
    public void getComments(String submissionId, LoadCommentCallback callback) {
        remoteDataSource.getComments(submissionId, new LoadCommentCallback() {
            @Override
            public void onCommentsLoad(List<CommentNode<Comment>> comments) {
                callback.onCommentsLoad(comments);
            }

            @Override
            public void onDataNotAvailable() {
             callback.onDataNotAvailable();
            }
        });

    }
}
