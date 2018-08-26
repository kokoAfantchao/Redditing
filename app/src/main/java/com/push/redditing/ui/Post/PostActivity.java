package com.push.redditing.ui.Post;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.push.redditing.R;
import com.push.redditing.utils.ActivityUtils;
import dagger.android.support.DaggerAppCompatActivity;
import net.dean.jraw.models.Subreddit;

import javax.inject.Inject;
import java.io.Serializable;

public class PostActivity extends DaggerAppCompatActivity implements PostFragment.OnFragmentInteractionListener{
     final static public  String SUBREDDIT_NAME_EXTRA ="SUBREDDIT_NAME_EXTRA";

     @Inject PostFragment mPostFragment;
     @Inject PostPresenter mPostPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        if( getIntent().hasExtra(SUBREDDIT_NAME_EXTRA)){
            String fullName = getIntent().getStringExtra(SUBREDDIT_NAME_EXTRA);
            supportActionBar.setTitle(R.string.post_actionbar_text);
            supportActionBar.setSubtitle("r/"+fullName);
            mPostFragment.setSubredditName(fullName);
        }
        ActivityUtils.addFragmentToActivity (getSupportFragmentManager(),mPostFragment, R.id.post_fragment );
    }

    @Override
    protected void onResume() {
        mPostPresenter.takeView(mPostFragment);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
       mPostPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
