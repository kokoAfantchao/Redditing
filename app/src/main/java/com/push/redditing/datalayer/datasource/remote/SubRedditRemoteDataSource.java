package com.push.redditing.datalayer.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.common.util.concurrent.Runnables;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.utils.AppExecutors;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.tree.CommentNode;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;


@Singleton
public class SubRedditRemoteDataSource implements SubRedditDataSource{

    private ApiService apiService;
    private AppExecutors mAppExecutors;

    @Inject
    public SubRedditRemoteDataSource(@NonNull ApiService apiService, AppExecutors executors ){
     this.apiService= apiService;
     this.mAppExecutors =executors;
    }


    @Override
    public void getSubreddits(LoadSubredditCallback loadSubredditCallback) {
        Runnable  runnable = new Runnable() {
            List<Subreddit> subredditList = new ArrayList<>();
            @Override
            public void run() {

                if(ApiService.getmRedditClient()!= null){
                    subredditList = apiService.getUserSubReddit();
                }

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        if(ApiService.getmRedditClient()== null){
                               loadSubredditCallback.onRedditClientNull();
                        }else{
                            if (!subredditList.isEmpty()) {
                                loadSubredditCallback.onSubredditLoaded(subredditList);
                            }else{
                                loadSubredditCallback.onDataNotAvailable();
                            }
                        }
                    }
                });

            }
        };
        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void saveSubReddits(List<Subreddit> subredditList) {

    }

    @Override
    public void getSubmissions(@NonNull String SubReddit_fullname, LoadSubmissionCallback loadSubmissionCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Timber.d(" getSubmission running in back ground******");
                List<Submission> submissions = apiService.getSubmission(SubReddit_fullname);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (submissions!= null ) loadSubmissionCallback.onSubmissionLoad(SubReddit_fullname, submissions);
                        else loadSubmissionCallback.onDataNotAvailable(SubReddit_fullname);
                    }
                });
            }
        };
        mAppExecutors.networkIO().execute(runnable);

    }



    @Override
    public void saveSubmission(List<Submission> submissions) {

    }

    @Override
    public void postSubmission(Post post, PostSubmissionCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Submission submission = apiService.postNewSubmission(post);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (submission != null){
                            callback.onPostSuccess(submission);
                        }else {
                            callback.onPostFailed();
                        }
                    }
                });
            }
        };
        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void getComments(String submissionId, LoadCommentCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<CommentNode<Comment>> comments = apiService.getComments(submissionId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (comments!= null ) callback.onCommentsLoad(comments);
                        else callback.onDataNotAvailable();
                    }
                });


            }
        };
        mAppExecutors.networkIO().execute(runnable);
    }



}
