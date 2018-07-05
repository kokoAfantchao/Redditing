package com.push.redditing.datalayer.datasource.remote;

//import com.push.redditing.RedditingApplication;
//import net.dean.jraw.RedditClient;
//import net.dean.jraw.oauth.OAuthException;
//import net.dean.jraw.oauth.StatefulAuthHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.push.redditing.RedditingApplication;
import com.push.redditing.ui.main.SubReddit.SubRedditContract;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.oauth.OAuthException;
import net.dean.jraw.oauth.StatefulAuthHelper;
import net.dean.jraw.pagination.BarebonesPaginator;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApiService {

    private final StatefulAuthHelper helper ;
    boolean requestRefreshToken = true;
    boolean useMobileSite = true;
    private static  RedditClient mRedditClient;
    private NetworkAdapter mNetworkAdapter;



    String[] scopes = new String[]{ "read", "identity","mysubreddits" };

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
        if (mRedditClient!= null ){
        BarebonesPaginator<Subreddit> subscriber = mRedditClient.me().subreddits("subscriber").build();
        return subscriber.accumulateMerged(1);
        }
        return null ;
    }
    // provide subreddit fullname as paramet
    public  List<Submission> getSubmission(@NonNull String fullname){

      if(mRedditClient != null ){

          Listing<Submission> submissionListing = mRedditClient.subreddit(fullname).posts().build().getCurrent();
       return  submissionListing;
      }
      return new ArrayList<Submission>();
    }




}
