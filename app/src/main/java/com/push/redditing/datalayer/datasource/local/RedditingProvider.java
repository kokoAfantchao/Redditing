package com.push.redditing.datalayer.datasource.local;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RedditingProvider extends ContentProvider {
    private static final int SUBREDDITS = 0;
    private static final int SUBREDDIT_NAME = 1;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SQLiteOpenHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        // Todo implement later
//        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
//        final String authority = ItemsContract.CONTENT_AUTHORITY;
//        matcher.addURI(authority, "items", ITEMS);
//        matcher.addURI(authority, "items/#", ITEMS__ID);
//        return matcher;
        return  null ;
    }

    @Override
    public boolean onCreate() {
     //   mOpenHelper = new ItemsDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBREDDITS:
                return RedditContract.SubReddits.CONTENT_TYPE;
            case SUBREDDIT_NAME :
                return RedditContract.SubReddits.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
//todo rewrite later

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
      //  final SelectionBuilder builder = buildSelection(uri);
//       // Cursor cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
//        if (cursor != null) {
//            cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        }
        return  null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBREDDITS: {
                final long _id = db.insertOrThrow(Tables.SUBREDDITS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                //return ItemsContract.Items.buildItemUri(_id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBREDDITS: {
                int countInsert = 0;
                db.beginTransaction();
                try {

                    for (ContentValues contentValue : values) {
                        db.insert(Tables.SUBREDDITS, null, contentValue);
                        countInsert++;
                    }
                    db.setTransactionSuccessful();


                } catch (Exception e) {

                } finally {
                    db.endTransaction();
                }
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);

                return countInsert;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
//todo rewrite later
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //final SelectionBuilder builder = buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri, null);
        //return builder.where(selection, selectionArgs).update(db, values);
      return  0 ;
    }

    //todo rewrite later

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri, null);

        return builder.where(selection, selectionArgs).delete(db);
    }

    private SelectionBuilder buildSelection(Uri uri){
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        return buildSelection(uri, match, builder);
    }

    private SelectionBuilder buildSelection(Uri uri, int match, SelectionBuilder builder) {
        final List<String> paths = uri.getPathSegments();
        switch (match) {
            case SUBREDDITS: {
                return builder.table(Tables.SUBREDDITS);
            }
            case SUBREDDIT_NAME: {
                final String display_name = paths.get(1);
                return builder.table(Tables.SUBREDDITS).where(RedditContract.SubReddits.DISPLAY_NAME + "=?", display_name);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    interface Tables {
        String SUBREDDITS = "subreddits";
    }
}
