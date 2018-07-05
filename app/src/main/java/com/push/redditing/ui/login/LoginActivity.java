package com.push.redditing.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import com.push.redditing.ui.main.MainActivity;
import com.push.redditing.utils.AppConnectivity;
import com.push.redditing.utils.PreferencesHelper;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

import javax.inject.Inject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends DaggerAppCompatActivity implements  LoginContract.View{
    @Inject LoginPresenter mLoginPresenter;
    @Inject PreferencesHelper  mPreferencesHelper;
    @BindView(R.id.login_layout) View loginLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void OnLoginButtonClick(View view){
        boolean isOnline = AppConnectivity.isOnline(this);
        if ( isOnline) mLoginPresenter.gernarateOauthUrl();
        else showLoginError();
    }

    public void  OnUserlessTextViewClick(View  view ) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final  String action = intent.getAction();
        if(action.equals(Intent.ACTION_VIEW)){
            mLoginPresenter.getOAuthToken( intent.getData().toString().replace("redditing","https"));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.takeView(this);


    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public void showWebview(String Url){
        Intent intentWebBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(intentWebBrowser);
    }

    @Override
    public void showLoginError() {
     Snackbar.make(findViewById(R.id.login_layout),R.string.msg_no_connectvity,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void launchMainActivity() {
        // Save someting before launching Mainactivity
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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

