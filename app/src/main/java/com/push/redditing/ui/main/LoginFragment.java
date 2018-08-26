package com.push.redditing.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.push.redditing.R;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.utils.AppConnectivity;
import com.push.redditing.utils.PreferencesHelper;
import dagger.android.support.DaggerFragment;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 *
 *
 */
@ActivityScoped
public class LoginFragment extends DaggerFragment implements  MainContract.LoginView {

    @BindView(R.id.login_layout)
    View loginLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sign_in_btn)
    Button buttonSignUp;

    private Unbinder unbinder;
    @Inject
    MainPresenter mainPresenter;
    OnOauthSuccessListener mCallback;

    public interface OnOauthSuccessListener {
        void OAuthIsSuccess();
    }

    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
       unbinder = ButterKnife.bind(this, view);
       buttonSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean isOnline = AppConnectivity.isOnline(getContext());
               if ( isOnline) mainPresenter.gernarateOauthUrl();
               else showLoginError();

           }
       });
       return view;
    }

    public LoginFragment setSetOAuthSuccessListener(OnOauthSuccessListener mCallback){
        this.mCallback = null ;
        this.mCallback= mCallback;
        return this ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainPresenter.takeLoginView(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.dropeLoginView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showWebview(String Url) {
        Intent intentWebBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(intentWebBrowser);

    }

    @Override
    public void showLoginError() {
        Snackbar.make(getView(),R.string.msg_no_connectvity,Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void showMainfragment() {
      PreferencesHelper.getInstance(getContext()).put(PreferencesHelper.Key.IS_OAUTH, true);
      mCallback.OAuthIsSuccess();
    }

    @Override
    public void setLoadingIndicator(Boolean isloading) {
        if(isloading){
            progressBar.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.INVISIBLE);
        }else {
            loginLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void returnToMainActivity() {

    }

    @Override
    public boolean isActive() {
        return false;
    }




}
