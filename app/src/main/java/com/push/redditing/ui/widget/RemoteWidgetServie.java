package com.push.redditing.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import timber.log.Timber;

import java.util.ArrayList;

public class RemoteWidgetServie  extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return  new StackRemoteViewsFactory(getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private static final int mCount = 10;
       // private List<WidgetItem > mWidgetItems = new ArrayList<WidgetItem>();
        private Context mContext;
        private int mAppWidgetId;
        private ArrayList<String> dataSet ;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            ArrayList<String> submission_list =intent.getStringArrayListExtra("SUBMISSION_LIST");
            if (submission_list != null) {
                dataSet = submission_list;
                Timber.d("+++++++++++++ there is some thing in this "+dataSet.size());
            }

        }

        @Override
        public void onCreate() {


        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Timber.d("++++++++++++++++++++++ we are getting the view at position =====> "+ position);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),android.R.layout.simple_list_item_1);
            remoteViews.setTextViewText(android.R.id.text1, dataSet.get(position));
            return remoteViews;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }


}
