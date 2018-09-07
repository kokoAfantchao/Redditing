package com.push.redditing.datalayer.datasource;

import android.support.annotation.NonNull;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import java.util.ArrayList;
import java.util.List;

public class Carbonate {
    public static List<LSubreddit> castToLSubreddits(List<Subreddit> subredditList){
        List<LSubreddit> lSubreddits = new ArrayList<>();
        for (Subreddit subreddit: subredditList) {
            LSubreddit lSubreddit = new LSubreddit();
            lSubreddit.setName(subreddit.getName());
            lSubreddit.setFullName(subreddit.getFullName());
            lSubreddit.setPublicDescription(subreddit.getPublicDescription());
            lSubreddit.setBannerImage(subreddit.getBannerImage());
            lSubreddit.setSubscribers(subreddit.getSubscribers());
            lSubreddits.add(lSubreddit);
        }

        return lSubreddits ;
    }


    public static  List<LSubmission> castToLSubmission(@NonNull List<Submission>submissionList) {
        List<LSubmission> lSubmissions = new ArrayList<>();
        for (Submission submission: submissionList) {
            LSubmission lSubmission = new LSubmission();
            lSubmission.setId(submission.getId());
            lSubmission.setTitle(submission.getTitle());
            lSubmission.setAuthor(submission.getAuthor());
            lSubmission.setCommentCount(submission.getCommentCount());
            lSubmission.setCreated(submission.getCreated());
            lSubmission.setSelfText(submission.getSelfText());
            lSubmission.setSelfPost(submission.isSelfPost());
            lSubmission.setThumbnail(submission.getThumbnail());
            lSubmission.setSubreddit(submission.getSubreddit());
            lSubmission.setUrl(submission.getUrl());

            lSubmissions.add(lSubmission);
        }
        return lSubmissions;
    }


}
