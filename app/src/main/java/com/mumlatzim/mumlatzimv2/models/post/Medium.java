package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Medium implements Parcelable{

    @SerializedName("source_url")
    private String sourceUrl;

    public Medium() {
    }

    protected Medium(Parcel in) {
        sourceUrl = in.readString();
    }

    public static final Creator<Medium> CREATOR = new Creator<Medium>() {
        @Override
        public Medium createFromParcel(Parcel in) {
            return new Medium(in);
        }

        @Override
        public Medium[] newArray(int size) {
            return new Medium[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceUrl);
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public static Creator<Medium> getCREATOR() {
        return CREATOR;
    }
}
