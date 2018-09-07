package com.push.redditing.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.datalayer.datasource.remote.ApiService;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import dagger.android.DaggerIntentService;
import net.dean.jraw.models.Submission;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class WidgetService extends DaggerIntentService {
    private static String FULL_NAME_EXTRA="FULL_NAME_EXTRA";
    private final String WIDGET_SERVICE_NAME = "com.push.redditing.ui.widget.WidgetService";

    public static  final  String GET_SUBREDDITS_ACTION = "GET_SUBREDDIT_ACTION";
    public static  final String GET_SUBMISSIONS_ACTION = "GET_SUBMISSIONS_ACTION";
    public static final String SUBREDDITS_EXTRA = "SUBREDDITS_EXTRA";
    public static final String SUBMISSIONS_EXTRA = "SUBMISSIONS_EXTRA";
    public static final String WIDGET_ID_EXTRA = "WIDGET_ID_EXTRA";

    @Inject
    SubRedditRepository subRedditRepository;

    @Inject
    ApiService mApiService;


    public WidgetService() {

        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       if(intent.getAction().equals(GET_SUBREDDITS_ACTION)){
           subRedditRepository.getSubreddits(new SubRedditDataSource.LoadSubredditCallback() {
               @Override
               public void onSubredditLoaded(List<LSubreddit> subredditList) {
                   if (subredditList != null ) {
                       Intent intent = new Intent();
                       intent.putParcelableArrayListExtra(SUBREDDITS_EXTRA, (ArrayList<? extends Parcelable>) subredditList);
                       intent.setAction(GET_SUBREDDITS_ACTION);
                       sendBroadcast(intent);

                   }
               }

               @Override
               public void onDataNotAvailable() {

               }

               @Override
               public void onRedditClientNull() {

               }
           });

       }

        if (intent.getAction().equals(GET_SUBMISSIONS_ACTION)) {
            String stringExtra = intent.getStringExtra(FULL_NAME_EXTRA);
            int widgetID = intent.getIntExtra(WIDGET_ID_EXTRA,0);

            List<Submission> submission = mApiService.getSubmission(stringExtra);
            List<String> submissionTitle = new ArrayList<>();
            Timber.d("----------------+++++++++++++ my Sevice request is finished time to print something");

            int i = 0;
            if(submission!=null){
                for (Submission subm: submission) {
                    submissionTitle.add(subm.getTitle());
                }
            }
            Context context = getApplicationContext();
                    SubmissionWidget
                           .updateWidget(context,
                                   AppWidgetManager.getInstance(context),
                                   widgetID,
                                   submissionTitle);



//            subRedditRepository.getSubmissions(stringExtra, new SubRedditDataSource.LoadSubmissionCallback() {
//                @Override
//                public void onSubmissionLoad(String full_name, List<Submission> submissionList) {
//                    List<String> submissionTitle = new ArrayList<>();
//                    Timber.d("----------------+++++++++++++ my Sevice request is finished time to print something");
//                    int i = 0;
//                    for (Submission subm: submissionList) {
//                        submissionTitle.add(subm.getTitle());
//                    }
//                    Context context = getApplicationContext();
//                    SubmissionWidget
//                           .updateWidget(context,
//                                   AppWidgetManager.getInstance(context),
//                                   widgetID,
//                                   submissionTitle);
//                }
//
//                @Override
//                public void onDataNotAvailable(String full_nameß) {
//                    Timber.d("__________+++++++++++++onDataNotAvailable: my Sevice request is finished time to print something");
//
//
//                }
//
//                @Override
//                public void onRedditClientNull() {
//                    Timber.d("________++++++++++++++++onRedditClientNull: my Sevice request is finished time to print something");
//
//
//                }
//            });


        }

    }

    public static void GetSubreddit(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(GET_SUBREDDITS_ACTION);
        context.startService(intent);
    }


    public static void GetSubmission(Context context, String fullName,  int appWidgetID ) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(GET_SUBMISSIONS_ACTION);
        intent.putExtra(FULL_NAME_EXTRA, fullName);
        intent.putExtra(WIDGET_ID_EXTRA, appWidgetID);
        context.startService(intent);
    }

}
