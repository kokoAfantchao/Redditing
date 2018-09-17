package com.push.redditing.datalayer.datasource.local.deprecate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.push.redditing.datalayer.datasource.local.deprecate.RedditingProvider.Tables;


public class RedditDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "redditing.db";
    private static final int DATABASE_VERSION = 2;

    public RedditDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE "+Tables.SUBREDDITS + " ("
                + RedditContract.SubRedditColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RedditContract.SubRedditColumns. FULL_NAME  +" TEXT  NOT NULL,"
                + RedditContract.SubRedditColumns. DISPLAY_NAME  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. BANNER_IMAGE  +" TEXT,"
                + RedditContract.SubRedditColumns. PUBLIC_DESCRIPTION  +"TEXT,"
                + RedditContract.SubRedditColumns. SUBSCRIBERS  +" INTEGER"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditingProvider.Tables.SUBREDDITS);
        onCreate(db);
    }
}
