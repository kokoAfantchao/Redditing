package com.push.redditing.datalayer.datasource.local;

import android.arch.persistence.room.*;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;

import java.util.List;

@Dao
public interface SubredditDao {

    @Query("SELECT * FROM LSubreddit")
    List<LSubreddit> getSubreddits();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubreddit(LSubreddit subreddit);

    @Update
    int updateSubreddit(LSubreddit subreddit);


    /**
     * Delete all Subreddit.
     */
    @Query("DELETE FROM LSubreddit")
    void deleteAllSubreddits();




}
