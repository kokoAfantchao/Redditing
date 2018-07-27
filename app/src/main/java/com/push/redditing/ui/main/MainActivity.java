package com.push.redditing.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.push.redditing.R;
import com.push.redditing.utils.ActivityUtils;
import com.push.redditing.utils.PreferencesHelper;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

import javax.inject.Inject;

public class MainActivity extends DaggerAppCompatActivity implements LoginFragment.OnOauthSuccessListener {


    @Inject
    MainContract.Presenter mMainPresenter;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    Lazy<LoginFragment> mLoginFragment;

    @Inject
    Lazy<MainFragment> mMainFragment;



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final  String action = intent.getAction();
        if(action.equals(Intent.ACTION_VIEW)){
            mMainPresenter.getOAuthToken( intent.getData().toString().replace("redditing","https"));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oAuthCheck();
    }

    private void oAuthCheck() {
     boolean isOauth = mPreferencesHelper.getBoolean(PreferencesHelper.Key.IS_OAUTH, false);
         ///   TODO Don't forget  to  check  the the Oauth before realese
//         if(!isOauth){
//             ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mLoginFragment.get(),
//                     R.id.fragment_container);
//         }else {
//             ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mMainFragment.get(),
//                     R.id.fragment_container);
//
//         }
        ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),
                   mLoginFragment.get().setSetOAuthSuccessListener(this),
                   R.id.fragment_container);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OAuthIsSuccess() {
        ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mMainFragment.get(),
                R.id.fragment_container);
    }
}
