package com.push.redditing.datalayer.datasource.local.deprecate;

import android.net.Uri;

public class RedditContract {
    public static final String CONTENT_AUTHORITY = "com.push.redditing";
    public static final Uri BASE_URI = Uri.parse("content://com.push.redditing");

    private RedditContract() {
    }

    // Todo put here all subReddit columns here
    interface SubRedditColumns {
        String _ID = "_id";
        String FULL_NAME = "full_name";
        String BANNER_IMAGE = "banner_img";
        String DISPLAY_NAME = "display_name";
        String PUBLIC_DESCRIPTION = "public_description";
        String SUBSCRIBERS = "subscribers";

    }

    public static class SubReddits implements SubRedditColumns {
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
        public static Uri buildItemUri(String displayName) {
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
