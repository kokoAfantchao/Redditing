package com.push.redditing.ui.SubmissionDetail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.push.redditing.R;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.utils.ActivityUtils;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import net.dean.jraw.models.Submission;

import javax.inject.Inject;
import java.io.Serializable;

public class SubmissionActivity extends DaggerAppCompatActivity{

    public static final String SUBMISSION_EXTRA = "SUBMISSION_EXTRA";
    public static final String SUBMISSION_BUNDLE = "SUBMISSION_BUNDLE";


    @Inject
    SubmissionPresenter mSubmissionPresenter;

    @Inject
    Lazy<SubmissionFragment> mSubmissionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundleExtra = getIntent().getBundleExtra(SUBMISSION_BUNDLE);
        if(bundleExtra!= null ){
           LSubmission submission  =(LSubmission) bundleExtra.getParcelable(SUBMISSION_EXTRA);
            mSubmissionFragment.get().setSubmission(submission);
            getSupportActionBar().setTitle(submission.getTitle());
            toolbar.setSubtitle(submission.getSubreddit());
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                mSubmissionFragment.get(),R.id.submission_content);
    }

}
