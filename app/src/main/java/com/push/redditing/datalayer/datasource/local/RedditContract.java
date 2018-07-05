package com.push.redditing.datalayer.datasource.local;

import android.net.Uri;

public class RedditContract {
    public static final String CONTENT_AUTHORITY = "com.push.redditing";
    public static final Uri BASE_URI = Uri.parse("content://com.push.redditing");

    private RedditContract() {
    }
    // Todo put here all subReddit columns here
    interface SubRedditColumns{
        String _ID ="_id" ;

        String ACCOUNTS_ACTIVE ="accounts_active";

        String CREATED_UTC = "created_utc'";

        String NAME ="name";

        String KEY_COLOR ="key_color";

        String DISPLAY_NAME  ="display_name";

        String DESCRITION="description";

        String SPOILERS_ENABLED= "spoilers_enabled";

        String SUBMISSION_TYPE ="submission_type";

        String USER_IS_MUTED ="user_is_muted";

        String USER_IS_BANNED ="use_is_banned";

        String USER_IS_MODERATOR ="user_is_moderator";

        String USER_IS_SUBSCRIBER = "user_is_subscriber";

        String USER_FLAIR_TEXT ="user_flair_text";

        String USER_FLAIR_ENABLED_IN_SR = "user_flair_enabled_in_sr";


    }

    public static class SubReddits implements SubRedditColumns  {
        public static final String CONTENT_TYPE = "com.push.redditing.dir/com.push.redditing.subreddits";
        public static final String CONTENT_ITEM_TYPE = "com.push.redditing.item/vnd.com.example.xyzreader.subreddits";

       // public static final String DEFAULT_SORT = PUBLISHED_DATE + " DESC";

        /**
         * Matches: /subreddits/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("subreddits").build();
        }

        /**
         * Matches: /subreddits/[display_name]/
         */
        public static Uri buildItemUri(String displayName ) {
            return BASE_URI.buildUpon().appendPath("subreddits").appendPath(displayName).build();
        }

        /**
         * Read item ID item detail URI.
         */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }
}
