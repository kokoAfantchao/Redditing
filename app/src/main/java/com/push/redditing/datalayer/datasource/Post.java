package com.push.redditing.datalayer.datasource;

import net.dean.jraw.models.SubmissionKind;

public class Post {
    private String full_name ;
    private SubmissionKind submissionKind;
    private String title;
    private String content;
    private Boolean sendReplies;

    public Post(){
    }

    public Post(String full_name, SubmissionKind submissionKind, String title, String content, Boolean sendReplies) {
        this.full_name = full_name;
        this.submissionKind = submissionKind;
        this.title = title;
        this.content = content;
        this.sendReplies = sendReplies;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public SubmissionKind getSubmissionKind() {
        return submissionKind;
    }

    public void setSubmissionKind(SubmissionKind submissionKind) {
        this.submissionKind = submissionKind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSendReplies() {
        return sendReplies;
    }

    public void setSendReplies(Boolean sendReplies) {
        this.sendReplies = sendReplies;
    }


}
