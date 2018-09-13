package com.push.redditing.datalayer.datasource.local.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class LSubreddit implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String fullName;

    @ColumnInfo(name = "banner_img")
    private String bannerImage;

    @ColumnInfo(name = "display_name")
    private String name;

    @ColumnInfo(name = "public_description")
    private String publicDescription;

    @ColumnInfo(name = "subscribers")
    private Integer subscribers;

    public  LSubreddit(){

    }

    protected LSubreddit(Parcel in) {
        fullName = in.readString();
        bannerImage = in.readString();
        name = in.readString();
        publicDescription = in.readString();
        if (in.readByte() == 0) {
            subscribers = null;
        } else {
            subscribers = in.readInt();
        }
    }

    public static final Creator<LSubreddit> CREATOR = new Creator<LSubreddit>() {
        @Override
        public LSubreddit createFromParcel(Parcel in) {
            return new LSubreddit(in);
        }

        @Override
        public LSubreddit[] newArray(int size) {
            return new LSubreddit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(bannerImage);
        dest.writeString(name);
        dest.writeString(publicDescription);
        if (subscribers == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(subscribers);
        }
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }
}
