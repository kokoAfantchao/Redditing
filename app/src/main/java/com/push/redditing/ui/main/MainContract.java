package com.push.redditing.ui.main;

import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;
import net.dean.jraw.models.Subreddit;

import java.util.List;

public class MainContract {
    interface  View  extends BaseView{
        void showLoadingIndicator( Boolean  aBoolean);
        void showTabs(List<Subreddit> subredditList);
    }

    interface  Presenter extends BasePresenter<View>{
         void LoadSubreddits();

        @Override
        void takeView(MainContract.View view);

        @Override
        void dropView();
    }
}
