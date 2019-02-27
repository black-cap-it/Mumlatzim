package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class acf_PostDetails implements Parcelable {
    @SerializedName("link")
    private String mlink;
    @SerializedName("ok")
    private String mok;
    @SerializedName("wtsp")
    private String mwtsp;
    @SerializedName("title")
    private String mtitle;
    @SerializedName("coupon")
    private String mcoupon;

    public acf_PostDetails() {
    }

    protected acf_PostDetails(Parcel in) {
        mlink = in.readString();
        mok = in.readString();
        mwtsp = in.readString();
        mtitle = in.readString();
        mcoupon = in.readString();
    }

    public static final Creator<acf_PostDetails> CREATOR = new Creator<acf_PostDetails>() {
        @Override
        public acf_PostDetails createFromParcel(Parcel in) {
            return new acf_PostDetails(in);
        }

        @Override
        public acf_PostDetails[] newArray(int size) {
            return new acf_PostDetails[size];
        }
    };

    public String getMlink() {
        return mlink;
    }

    public String getMok() {
        return mok;
    }

    public String getMwtsp() {
        return mwtsp;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMcoupon() {
        return mcoupon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mlink);
        dest.writeString(mok);
        dest.writeString(mwtsp);
        dest.writeString(mtitle);
        dest.writeString(mcoupon);
    }
}
