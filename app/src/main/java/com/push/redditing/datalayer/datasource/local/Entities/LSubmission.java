package com.push.redditing.datalayer.datasource.local.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class LSubmission  implements Parcelable{

    private String id;

    private String subredditFullName;

    private String author;

    private Date created;

    private Boolean selfPost;

    private String linkFlairText;

    private String selfText;

    private String subreddit;

    private String thumbnail;

    private String title;

    private Boolean visited;

    private String url;

    private int commentCount;

    public  LSubmission(){

    }

    protected LSubmission(Parcel in) {
        id = in.readString();
        subredditFullName = in.readString();
        author = in.readString();
        byte tmpSelfPost = in.readByte();
        selfPost = tmpSelfPost == 0 ? null : tmpSelfPost == 1;
        linkFlairText = in.readString();
        selfText = in.readString();
        subreddit = in.readString();
        thumbnail = in.readString();
        title = in.readString();
        byte tmpVisited = in.readByte();
        visited = tmpVisited == 0 ? null : tmpVisited == 1;
        commentCount = in.readInt();
    }

    public static final Creator<LSubmission> CREATOR = new Creator<LSubmission>() {
        @Override
        public LSubmission createFromParcel(Parcel in) {
            return new LSubmission(in);
        }

        @Override
        public LSubmission[] newArray(int size) {
            return new LSubmission[size];
        }
    };

    public final boolean hasThumbnail() {
        String thumb = getThumbnail();
        return thumb != null && !thumb.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubredditFullName() {
        return subredditFullName;
    }

    public void setSubredditFullName(String subredditFullName) {
        this.subredditFullName = subredditFullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean isSelfPost() {
        return selfPost;
    }

    public void setSelfPost(Boolean selfPost) {
        this.selfPost = selfPost;
    }

    public String getLinkFlairText() {
        return linkFlairText;
    }

    public void setLinkFlairText(String linkFlairText) {
        this.linkFlairText = linkFlairText;
    }

    public String getSelfText() {
        return selfText;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subredditFullName);
        dest.writeString(author);
        dest.writeByte((byte) (selfPost == null ? 0 : selfPost ? 1 : 2));
        dest.writeString(linkFlairText);
        dest.writeString(selfText);
        dest.writeString(subreddit);
        dest.writeString(thumbnail);
        dest.writeString(title);
        dest.writeByte((byte) (visited == null ? 0 : visited ? 1 : 2));
        dest.writeInt(commentCount);
    }
}
