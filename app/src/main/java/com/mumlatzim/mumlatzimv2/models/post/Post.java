package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mumlatzim.mumlatzimv2.utility.DateUtilities;

public class Post implements Parcelable {

    @SerializedName("id")
    private Double mID;
    @SerializedName("title")
    private Title mTitle = new Title();
    @SerializedName("_embedded")
    private Embedded mEmbedded = new Embedded();
    @SerializedName("date")
    private String mOldDate;
    @SerializedName("pure_taxonomies")
    private taxonomies_PostDetails mtaxonomies_PostDetails=new taxonomies_PostDetails();
    @SerializedName("acf")
    private acf_PostDetails macf_PostDetails= new acf_PostDetails();
    @SerializedName("link")
    private String mPostUrl;
    private String mFormattedDate;
    private boolean mIsBookmark;

    public Double getID() {
        return mID;
    }

    public taxonomies_PostDetails getMtaxonomies_PostDetails() {
        return mtaxonomies_PostDetails;
    }

    public Title getTitle() {
        return mTitle;
    }

    public acf_PostDetails getMacf_PostDetails() {
        return macf_PostDetails;
    }

    public void setMacf_PostDetails(acf_PostDetails macf_PostDetails) {
        this.macf_PostDetails = macf_PostDetails;
    }

    public Embedded getEmbedded() {
        return mEmbedded;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public boolean isBookmark() {
        return mIsBookmark;
    }

    public void setBookmark(boolean mIsBookmark) {
        this.mIsBookmark = mIsBookmark;
    }

    public static Creator<Post> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mID);
        dest.writeParcelable(mTitle, flags);
        dest.writeParcelable(mEmbedded, flags);
        dest.writeString(mOldDate);
        dest.writeString(mFormattedDate);
        dest.writeParcelable(mtaxonomies_PostDetails,flags);
        dest.writeParcelable(macf_PostDetails,flags);
        dest.writeString(mPostUrl);
        dest.writeInt(mIsBookmark ? 1 : 0);
    }

    protected Post(Parcel in) {
        mID = in.readDouble();
        mTitle = in.readParcelable(Title.class.getClassLoader());
        mEmbedded = in.readParcelable(Embedded.class.getClassLoader());
        mOldDate = in.readString();
        mFormattedDate = in.readString();
        mtaxonomies_PostDetails=in.readParcelable(taxonomies_PostDetails.class.getClassLoader());
        macf_PostDetails=in.readParcelable(acf_PostDetails.class.getClassLoader());
        mPostUrl = in.readString();
        mIsBookmark = in.readInt() != 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getFormattedDate() {
        return DateUtilities.getFormattedDate(mOldDate);
    }
}
