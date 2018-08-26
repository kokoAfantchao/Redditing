package com.push.redditing.datalayer.datasource.remote;

//import com.push.redditing.RedditingApplication;
//import net.dean.jraw.RedditClient;
//import net.dean.jraw.oauth.OAuthException;
//import net.dean.jraw.oauth.StatefulAuthHelper;

import android.support.annotation.NonNull;
import com.google.common.base.Preconditions;
import com.push.redditing.RedditingApplication;
import com.push.redditing.datalayer.datasource.Post;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.oauth.AuthManager;
import net.dean.jraw.oauth.OAuthException;
import net.dean.jraw.oauth.StatefulAuthHelper;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.CommentNode;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApiService {

    private final StatefulAuthHelper helper ;
    boolean requestRefreshToken = true;
    boolean useMobileSite = true;
    private static  RedditClient mRedditClient;
    private static AuthManager mAuthManager;
    private NetworkAdapter mNetworkAdapter;



//    identity, edit, flair, history, modconfig, modflair,
//    modlog, modposts, modwiki, mysubreddits, privatemessages,
//    read, report, save, submit, subscribe, vote, wikiedit, wikiread.
    String[] scopes = new String[]{"identity", "edit", "flair", "history", "modconfig", "modflair",
                                    "modlog", "modposts", "modwiki", "mysubreddits", "privatemessages",
                                     "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread"};

    public ApiService(){
        helper = RedditingApplication.getAccountHelper().switchToNewUser();
    }

    public  String  getAuthorizationUlr(){
        return helper.getAuthorizationUrl(requestRefreshToken,useMobileSite,scopes);
    }

    public RedditClient getOAuthtoken(@NotNull String callbackUrl) throws  OAuthException {
        mRedditClient = helper.onUserChallenge(callbackUrl);
        return  mRedditClient;
    }

    public List<Subreddit>  getUserSubReddit() {
        if (mRedditClient!= null){
        BarebonesPaginator<Subreddit> subscriber = mRedditClient.me().subreddits("subscriber").build();
            return subscriber.accumulateMerged(1);

        }
        return  new ArrayList<Subreddit>() ;
    }
    //provide subreddit fullname as paramet
    public  List<Submission> getSubmission(@NonNull String fullname){
    Preconditions.checkNotNull(mRedditClient);
      if(mRedditClient != null ){
          Listing<Submission> submissionListing = mRedditClient.subreddit(fullname).posts().build().next();
          return  submissionListing;
      }
      return new ArrayList<Submission>();
    }


    public  Submission  postNewSubmission(@NonNull Post post){
        if (mRedditClient != null ){
            SubmissionReference submissionReference = mRedditClient.subreddit(post.getFull_name())
                    .submit(post.getSubmissionKind(), post.getTitle(), post.getContent(), post.getSendReplies());
            return submissionReference.inspect();
        }
        return  null ;
    }

    public  List<CommentNode<Comment>>  getComments(String submissionId){
        if (mRedditClient!= null ){
            List<CommentNode<Comment>> replies = mRedditClient.submission(submissionId).comments().getReplies();
            return replies;
        }
         return  null ;
    }


    public static RedditClient getmRedditClient() {
        return mRedditClient;
    }


    public class RedditClientNullException extends  Exception{


    }
}
