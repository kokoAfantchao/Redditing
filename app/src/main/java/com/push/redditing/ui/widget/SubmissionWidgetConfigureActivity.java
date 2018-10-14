package com.push.redditing.ui.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.push.redditing.R;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The configuration screen for the {@link SubmissionWidget SubmissionWidget} AppWidget.
 */
public class SubmissionWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.push.redditing.ui.widget.SubmissionWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
     ArrayAdapter<String> arrayAdapter  ;
    public ListView listView;
    Map<String, LSubreddit> lSubredditMap = new HashMap<>();
    CharSequence [] nameList;


    ListView.OnItemClickListener mOnItemClickListenerr = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = SubmissionWidgetConfigureActivity.this;
            final LSubreddit subreddit = lSubredditMap.get(nameList[position]);

            saveTitlePref(context,mAppWidgetId,subreddit.getName());
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SubmissionWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
            // Make sure we pass back the original appWidgetId
            passOriginalAppWidgetID();
            WidgetService.GetSubmission(getApplicationContext(),subreddit.getName(),mAppWidgetId);
        }
    };


    public SubmissionWidgetConfigureActivity(){
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(WidgetService.GET_SUBREDDITS_ACTION)) {
                ArrayList<LSubreddit> arrayListExtra = intent.getParcelableArrayListExtra(WidgetService.SUBREDDITS_EXTRA);
                nameList = new CharSequence[arrayListExtra.size()];
                int i=0;
                arrayAdapter.clear();
                for (LSubreddit  lSubreddit:arrayListExtra) {
                    lSubredditMap.put(lSubreddit.getName(), lSubreddit);
                      nameList[i++] = lSubreddit.getName();
                      arrayAdapter.insert(lSubreddit.getName(),i);
                    }

                    arrayAdapter.notifyDataSetChanged();
            }

            }
        };


    void passOriginalAppWidgetID(){
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);

        listView =  findViewById(R.id.subreddit_wdg_list);
        arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mOnItemClickListenerr);


//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        //Register broadcast to get data from my intent call

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }///////////widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        WidgetService.GetSubreddit(this);
       // mAppWidgetText.setText(loadTitlePref(SubmissionWidgetConfigureActivity.this, mAppWidgetId));
   }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WidgetService.GET_SUBREDDITS_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }
    @Override
    protected void onDestroy() {
        if (broadcastReceiver!= null){
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }
}

