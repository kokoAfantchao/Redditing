package com.push.redditing.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.push.redditing.R;
import com.push.redditing.utils.ActivityUtils;
import com.push.redditing.utils.PreferencesHelper;
import dagger.android.support.DaggerAppCompatActivity;

import javax.inject.Inject;

public class MainActivity extends DaggerAppCompatActivity implements LoginFragment.OnOauthSuccessListener, MainFragment.OnOauthRequired {


    @Inject
    MainContract.Presenter mMainPresenter;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    LoginFragment mLoginFragment;

    @Inject
    MainFragment mMainFragment;





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
        mMainPresenter.setLoaderManager(getSupportLoaderManager());
        oAuthCheck();
    }

    private void oAuthCheck() {
     boolean isOauth = mPreferencesHelper.getBoolean(PreferencesHelper.Key.IS_OAUTH, false);
//        Timber.d(" the oauth is "+isOauth+" so what so you want me to do +++++++++");
//         // TODO Don't forget  to  check  the the Oauth before realese
         if(!isOauth){
             ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),
                   mLoginFragment.setSetOAuthSuccessListener(this),R.id.fragment_container);
         }else {
             ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mMainFragment,
                     R.id.fragment_container);
         }
//         Use this code white testing
//        ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mMainFragment.get(),
//                   R.id.fragment_container);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginFragment.setSetOAuthSuccessListener(this);
        mMainFragment.setOauthRequired(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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
        ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mMainFragment,
                R.id.fragment_container);
    }

    @Override
    public void OnOauthFailed() {
        ActivityUtils.replaceFragmenToActivity(getSupportFragmentManager(),mLoginFragment,R.id.fragment_container);
    }
}
