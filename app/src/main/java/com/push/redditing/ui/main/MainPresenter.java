package com.push.redditing.ui.main;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.datalayer.datasource.local.deprecate.SubredditsLoader;
import com.push.redditing.datalayer.repository.OAuthRepository;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.utils.PreferencesHelper;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.oauth.OAuthException;

import timber.log.Timber;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

@ActivityScoped
final class MainPresenter implements MainContract.Presenter, LoaderManager.LoaderCallbacks<List<LSubreddit>> {

    private final static int SUBREDDITS_QUERY = 1;
    private List<Subreddit> mCurrentSubreddits;
    private LoaderManager mLoaderManager;
    private SubRedditRepository mSubRedditRepository;
    private PreferencesHelper mPreferencesHelper;
    private OAuthRepository oAuthRepository;
    private boolean mFirstLoad = true;
    private Context mContext;


    @Nullable
    private MainContract.View mMainView;

    @NonNull
    private MainContract.LoginView mLoginView;

    @Inject
    public MainPresenter(SubRedditRepository subRedditRepository,
                         PreferencesHelper mPreferencesHelper,
                         OAuthRepository authRepository , Application application) {
        this.mSubRedditRepository = subRedditRepository;
        this.mPreferencesHelper = mPreferencesHelper;
        this.oAuthRepository = authRepository;
        this.mContext = application.getApplicationContext();
    }


    @Override
    public void loadSubreddits(boolean forceLoad) {
      //loadSubreddit(forceLoad, true);
     //  mLoaderManager.restartLoader(SUBREDDITS_QUERY, null, this);
        mLoaderManager.initLoader(SUBREDDITS_QUERY, null, this);
    }

    @Override
    public void takeLoginView(MainContract.LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void dropeLoginView() {
        mLoginView = null;
    }

    @Override
    public void gernarateOauthUrl() {
        String authorizationUlr = oAuthRepository.getOAuthUrl();
        mLoginView.showWebview(authorizationUlr);
    }

    @Override
    public void getOAuthToken(String uri) {
        new AsyncLoader().execute(uri);
    }

    @Override
    public void loadSubmission(String full_name) {
        mSubRedditRepository.getSubmissions(full_name, new SubRedditDataSource.LoadSubmissionCallback() {
            @Override
            public void onSubmissionLoad(String full_name, List<LSubmission> submissionList) {
                mMainView.transferSubmission(full_name, submissionList);
            }

            @Override
            public void onDataNotAvailable(String s) {
                mMainView.transferSubmission(s, null);
            }

            @Override
            public void onRedditClientNull() {
                mMainView.showLoginView();
            }
        });
    }

    private void loadSubreddit(boolean forceLoad,final boolean showLoadIndicatorIU) {
        if (showLoadIndicatorIU) {
            if (mMainView != null) {
                mMainView.showLoadingIndicator(true);
            }
        }
        //EspressoIdlingResource.increment();
        mSubRedditRepository.getSubreddits(new SubRedditDataSource.LoadSubredditCallback() {
            @Override
            public void onSubredditLoaded(List<LSubreddit> subredditList) {
                // USE THIS CODE WHEN USING ESPRESSO
                //! !EspressoIdlingResource.getIdlingResource().isIdleNow()
                // EspressoIdlingResource.decrement();
                // Set app as idle.


                // The view may not be able to handle UI updates anymore
                //TODO Create isActive function in  view Interface
                if (mMainView == null) {

                    Timber.d(" I am  so sorry this view is null have no reference ");
                    return;
                }
                if (showLoadIndicatorIU) {
                    mMainView.showLoadingIndicator(false);
                }
                processSubReddit(subredditList);


            }

            private void processSubReddit(List<LSubreddit> subredditList) {

                if (mMainView != null) {
                    mMainView.showTabs(subredditList);
                }
            }

            @Override
            public void onDataNotAvailable() {


            }

            @Override
            public void onRedditClientNull() {
                mMainView.showLoginView();
            }
        });
    }

    @Override
    public void setLoaderManager (LoaderManager mLoaderManager) {
        this.mLoaderManager = mLoaderManager;
    }

    @Override
    public void takeView(MainContract.View view) {
        this.mMainView = view;
       /// loadSubreddits(true);
    }

    @Override
    public void dropView() {
        this.mMainView = null;
    }


    @NonNull
    @Override
    public Loader<List<LSubreddit>> onCreateLoader(int id, @Nullable Bundle args) {
    //    this.mMainView.showLoadingIndicator(true);
        return new SubredditsLoader( mContext,mSubRedditRepository);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<LSubreddit>> loader, List<LSubreddit> data) {
        this.mMainView.showLoadingIndicator(false);
        if (data == null) {

        }else {
            mMainView.showTabs(data);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<LSubreddit>> loader) {

    }

    private class AsyncLoader extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoginView.setLoadingIndicator(true);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean oAuthtoken = false;
            String string = strings[0];
            if (string != null) {
                try {
                    RedditClient redditClient = oAuthRepository.getAuthorizeClient(string);
                    Map<String, Object> prefs = redditClient.me().prefs();
                    Timber.d(prefs.toString());
                    oAuthtoken = true;
                } catch (OAuthException e) {
                    Timber.d("doInbackGround %s", e.toString());
                    oAuthtoken = false;
                }
            }
            return oAuthtoken;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                mPreferencesHelper.put(PreferencesHelper.Key.IS_OAUTH, true);
                mLoginView.showMainfragment();
                mLoginView.setLoadingIndicator(false);
            } else {
                mPreferencesHelper.put(PreferencesHelper.Key.IS_OAUTH, false);
                mLoginView.setLoadingIndicator(false);
                mLoginView.showLoginError();
            }

        }
    }
}





