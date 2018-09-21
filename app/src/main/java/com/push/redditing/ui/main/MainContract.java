package com.push.redditing.ui.main;

import android.support.v4.app.LoaderManager;
import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;

import java.util.List;

public class MainContract {
    interface  View  extends BaseView{
        void showLoadingIndicator( Boolean  aBoolean);
        void showTabs(List<LSubreddit> subredditList);
        void transferSubmission(String full_name, List<LSubmission> submissions);
        void showLoginView();
    }

    interface LoginView extends  BaseView{
        void showWebview(String Url);
        void showLoginError();
        void showMainfragment();
        void setLoadingIndicator(Boolean isloading);
        void returnToMainActivity();
        boolean isActive();
    }


    interface  Presenter extends BasePresenter<View>{
        //login Logic
        void takeLoginView(LoginView loginView);
        void dropeLoginView();
        void gernarateOauthUrl();
        void getOAuthToken(String uri);

        void loadSubmission(String full_name );

        void loadSubreddits(boolean forceLoad);

        void setLoaderManager(LoaderManager loaderManager);
        @Override
        void takeView(View view);
        @Override
        void dropView();
    }
}
