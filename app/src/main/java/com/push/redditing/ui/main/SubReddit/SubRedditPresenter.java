package com.push.redditing.ui.main.SubReddit;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import com.push.redditing.datalayer.datasource.loaders.SubmissionsLoader;
import com.push.redditing.di.FragmentScoped;
import net.dean.jraw.models.Submission;

import javax.inject.Inject;
import java.util.List;

@FragmentScoped
final  class SubRedditPresenter  implements SubRedditContract.Presenter, LoaderManager.LoaderCallbacks<List<Submission>> {

    @Inject
    Context context;
    private  final static int  SUBMISSION_QUERY = 1 ;
    private SubmissionsLoader  submissionsLoader;
    private List<Submission>   mSubmission;

    @Nullable
    private SubRedditContract.View mSubRedditView;


    @Override
    public void takeView(SubRedditContract.View view) {
        this.mSubRedditView= view;

    }
    public void start(LoaderManager loaderManager,String full_name){
        Bundle bundle = new Bundle();
        bundle.putString("FULL_NAME", full_name);
        loaderManager.initLoader(SUBMISSION_QUERY, bundle, this);
    }

    @Override
    public void dropView() {
        this.mSubRedditView=  null ;

    }

    @Override
    public void getSubmissionList(String fullName) {


    }

    @NonNull
    @Override
    public Loader<List<Submission>> onCreateLoader(int id, @Nullable Bundle args) {
        String full_name = args.getString("FULL_NAME");
        return  new SubmissionsLoader(context, full_name);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Submission>> loader, List<Submission> data) {
        if(data!= null || !data.isEmpty()){

            mSubRedditView.showSubmissionList(data);
        }else{
            mSubRedditView.showLoadindError();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Submission>> loader) {

    }
}
