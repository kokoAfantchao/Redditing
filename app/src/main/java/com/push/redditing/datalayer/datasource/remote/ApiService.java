package com.push.redditing.datalayer.datasource.remote;

//import com.push.redditing.RedditingApplication;
//import net.dean.jraw.RedditClient;
//import net.dean.jraw.oauth.OAuthException;
//import net.dean.jraw.oauth.StatefulAuthHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import timber.log.Timber;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApiService {

    private static RedditClient mRedditClient;
    private static AuthManager mAuthManager;
    private final StatefulAuthHelper helper;
    boolean requestRefreshToken = true;
    boolean useMobileSite = true;
    //    identity, edit, flair, history, modconfig, modflair,
//    modlog, modposts, modwiki, mysubreddits, privatemessages,
//    read, report, save, submit, subscribe, vote, wikiedit, wikiread.
    String[] scopes = new String[]{"identity", "edit", "flair", "history", "modconfig", "modflair",
            "modlog", "modposts", "modwiki", "mysubreddits", "privatemessages",
            "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread"};
    private NetworkAdapter mNetworkAdapter;

    public ApiService() {
        helper = RedditingApplication.getAccountHelper().switchToNewUser();
    }

    public static RedditClient getmRedditClient() {
        return mRedditClient;
    }

    public String getAuthorizationUlr() {
        return helper.getAuthorizationUrl(requestRefreshToken, useMobileSite, scopes);
    }

    public RedditClient getOAuthtoken(@NotNull String callbackUrl) throws OAuthException {
        mRedditClient = helper.onUserChallenge(callbackUrl);
        mAuthManager = mRedditClient.getAuthManager();
        return mRedditClient;
    }

    public List<Subreddit> getUserSubReddit() {
        if (mRedditClient != null) {
            BarebonesPaginator<Subreddit> subscriber = mRedditClient.me().subreddits("subscriber").build();
            if (true) {
                subscriber = mRedditClient.userSubreddits("popular").build();
            }

            return subscriber.accumulateMerged(1);
        }

        return new ArrayList<Subreddit>();
    }

    //provide subreddit fullname as paramet
    public @Nullable
    List<Submission> getSubmission(@NonNull String fullname) {
        try {
            Preconditions.checkNotNull(mRedditClient);
            Listing<Submission> submissionListing = mRedditClient.subreddit(fullname).posts().build().next();
            return submissionListing;

        } catch (NullPointerException e) {

            return null;
        }
    }

    public Submission postNewSubmission(@NonNull Post post) {
        if (mRedditClient != null) {
            try {
                SubmissionReference submissionReference = mRedditClient.subreddit(post.getFull_name())
                        .submit(post.getSubmissionKind(), post.getTitle(), post.getContent(), post.getSendReplies());
                return submissionReference.inspect();

            }catch (net.dean.jraw.ApiException  e ){
                return null;
            }
            }

        return null ;
    }

    public List<CommentNode<Comment>> getComments(@NonNull String submissionId) {
        if (mRedditClient != null) {
            List<CommentNode<Comment>> replies = mRedditClient.submission(submissionId).comments().getReplies();
            return replies;
        }
        return null;
    }

    public class RedditClientNullException extends Exception {


    }
}
