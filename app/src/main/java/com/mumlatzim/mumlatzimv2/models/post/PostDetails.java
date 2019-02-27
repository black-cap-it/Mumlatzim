package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mumlatzim.mumlatzimv2.utility.DateUtilities;

import java.util.ArrayList;

public class PostDetails implements Parcelable {

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
    @SerializedName("content")
    private Content mContent = new Content();
    @SerializedName("link")
    private String mPostUrl;
    @SerializedName("_links")
    private Links mLinks = new Links();
    @SerializedName("categories")
    private ArrayList<Integer> mCategories = new ArrayList<>();
    private String mFormattedDate;
    private boolean mIsBookmark;

    public taxonomies_PostDetails getMtaxonomies_PostDetails() {
        return mtaxonomies_PostDetails;
    }

    public void setMtaxonomies_PostDetails(taxonomies_PostDetails mtaxonomies_PostDetails) {
        this.mtaxonomies_PostDetails = mtaxonomies_PostDetails;
    }

    public acf_PostDetails getMacf_PostDetails() {
        return macf_PostDetails;
    }

    public void setMacf_PostDetails(acf_PostDetails macf_PostDetails) {
        this.macf_PostDetails = macf_PostDetails;
    }

    public Double getID() {
        return mID;
    }

    public Title getTitle() {
        return mTitle;
    }

    public Embedded getEmbedded() {
        return mEmbedded;
    }

    public Content getContent() {
        return mContent;
    }

    public void setContent(Content mContent) {
        this.mContent = mContent;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public Links getLinks() {
        return mLinks;
    }

    public ArrayList<Integer> getCategories() {
        return mCategories;
    }

    public static Creator<PostDetails> getCREATOR() {
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
        dest.writeParcelable(mContent, flags);
        dest.writeParcelable(mtaxonomies_PostDetails,flags);
        dest.writeParcelable(macf_PostDetails,flags);
        dest.writeString(mPostUrl);
        dest.writeParcelable(mLinks, flags);
        dest.writeList(mCategories);
        dest.writeInt(mIsBookmark ? 1 : 0);
    }

    protected PostDetails(Parcel in) {
        mID = in.readDouble();
        mTitle = in.readParcelable(Title.class.getClassLoader());
        mEmbedded = in.readParcelable(Embedded.class.getClassLoader());
        mOldDate = in.readString();
        mFormattedDate = in.readString();
        mContent = in.readParcelable(Content.class.getClassLoader());
        mtaxonomies_PostDetails=in.readParcelable(taxonomies_PostDetails.class.getClassLoader());
        macf_PostDetails=in.readParcelable(acf_PostDetails.class.getClassLoader());
        mLinks = in.readParcelable(Links.class.getClassLoader());
        mPostUrl = in.readString();
        in.readList(mCategories, Integer.class.getClassLoader());
        mIsBookmark = in.readInt() != 0;
    }

    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel source) {
            return new PostDetails(source);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

    public String getFormattedDate() {
        return DateUtilities.getFormattedDate(mOldDate);
    }
}
