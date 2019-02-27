package com.mumlatzim.mumlatzimv2.models.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tags implements Parcelable {
    @SerializedName("id")
   private Double mID;
    @SerializedName("count")
   private Integer mCount;
    @SerializedName("description")
   private String mDescription;
    @SerializedName("link")
    private String mPostUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("slug")
    private String mSlug;

    protected Tags(Parcel in) {
        mID=in.readDouble();
        mCount=in.readInt();
        mDescription = in.readString();
        mPostUrl = in.readString();
        mName = in.readString();
        mSlug = in.readString();
    }

    public static final Creator<Tags> CREATOR = new Creator<Tags>() {
        @Override
        public Tags createFromParcel(Parcel in) {
            return new Tags(in);
        }

        @Override
        public Tags[] newArray(int size) {
            return new Tags[size];
        }
    };

    public Double getmID() {
        return mID;
    }

    public void setmID(Double mID) {
        this.mID = mID;
    }

    public Integer getmCount() {
        return mCount;
    }

    public void setmCount(Integer mCount) {
        this.mCount = mCount;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPostUrl() {
        return mPostUrl;
    }

    public void setmPostUrl(String mPostUrl) {
        this.mPostUrl = mPostUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSlug() {
        return mSlug;
    }

    public void setmSlug(String mSlug) {
        this.mSlug = mSlug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mID);
        dest.writeInt(mCount);
        dest.writeString(mDescription);
        dest.writeString(mPostUrl);
        dest.writeString(mName);
        dest.writeString(mSlug);
    }
}
