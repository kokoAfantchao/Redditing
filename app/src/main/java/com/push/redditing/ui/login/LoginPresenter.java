package com.push.redditing.ui.login;

import android.os.AsyncTask;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.utils.PreferencesHelper;
import net.dean.jraw.RedditClient;
import net.dean.jraw.oauth.OAuthException;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.Map;

@ActivityScoped
final  class LoginPresenter implements LoginContract.Presenter {


    ApiService mOAuthService;
    private LoginContract.View mloginView;
    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    public LoginPresenter(ApiService mApiService) {
        this.mOAuthService = mApiService;
    }

    @Override
    public void takeView(LoginContract.View view) {
      this.mloginView =view;
    }

    @Override
    public void dropView() {
        mloginView= null;
    }

    @Override
    public void gernarateOauthUrl() {
        String authorizationUlr = mOAuthService.getAuthorizationUlr();
        mloginView.showWebview(authorizationUlr);
    }

    @Override
    public void getOAuthToken(String uri) {
      new AsyncLoader().execute(uri);

    }


    private  class  AsyncLoader extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mloginView.setLoadingIndicator(true);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean oAuthtoken = false;
            String string = strings[0];
            if(string!= null) {
                try {
                    RedditClient  redditClient = mOAuthService.getOAuthtoken(string);
                    Map<String, Object> prefs =redditClient.me().prefs();
                    Timber.d(prefs.toString());
                    oAuthtoken = true ;
                }catch (OAuthException  e){
                    Timber.d("doInbackGround %s", e.toString());
                    oAuthtoken = false;
                }
            }
            return oAuthtoken ;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean ){
                mPreferencesHelper.put(PreferencesHelper.Key.IS_OAUTH, true);
                mloginView.launchMainActivity();
                mloginView.setLoadingIndicator(false);
            }else{
                mPreferencesHelper.put(PreferencesHelper.Key.IS_OAUTH, false);
                mloginView.setLoadingIndicator(false);
                mloginView.showLoginError();
            }

        }
    }
}
