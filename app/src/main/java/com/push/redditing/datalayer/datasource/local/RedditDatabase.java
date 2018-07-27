package com.push.redditing.datalayer.datasource.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.push.redditing.datalayer.datasource.local.RedditingProvider.Tables;


public class RedditDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "redditing.db";
    private static final int DATABASE_VERSION = 1;

    public RedditDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE "+Tables.SUBREDDITS + " ("
                + RedditContract.SubRedditColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RedditContract.SubRedditColumns.ACCOUNTS_ACTIVE +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. CREATED_UTC  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. NAME  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. KEY_COLOR  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. DISPLAY_NAME  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. DESCRITION  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. SPOILERS_ENABLED  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. SUBMISSION_TYPE  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. USER_IS_MUTED  +" BOOLEAN NOT NULL,"
                + RedditContract.SubRedditColumns. USER_IS_BANNED  +" BOOLEAN NOT NULL,"
                + RedditContract.SubRedditColumns. USER_IS_MODERATOR  +" BOOLEAN NOT NULL,"
                + RedditContract.SubRedditColumns. USER_IS_SUBSCRIBER  +" BOOLEAN NOT NULL,"
                + RedditContract.SubRedditColumns. USER_FLAIR_TEXT  +" TEXT NOT NULL,"
                + RedditContract.SubRedditColumns. USER_FLAIR_ENABLED_IN_SR  +" TEXT NOT NULL"
                +")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditingProvider.Tables.SUBREDDITS);
        onCreate(db);
    }
}
