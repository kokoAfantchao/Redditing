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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import dagger.android.support.DaggerAppCompatActivity;

import javax.inject.Inject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends DaggerAppCompatActivity implements  LoginContract.View{
    @Inject
    LoginPresenter mLoginPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        mLoginPresenter.takeView(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.dropView();
        super.onDestroy();
    }

    public void OnLoginButtonClick(View view){
        mLoginPresenter.gernarateOauthUrl();

    }

    @Override
    public void showWebview(String Url){
        Intent intentWebBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(intentWebBrowser);
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}

