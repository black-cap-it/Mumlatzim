package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FullSize implements Parcelable {
    @SerializedName("source_url")
    private String sourceUrl;

    public FullSize() {
    }

    protected FullSize(Parcel in) {
        sourceUrl = in.readString();
    }

    public static final Creator<FullSize> CREATOR = new Creator<FullSize>() {
        @Override
        public FullSize createFromParcel(Parcel in) {
            return new FullSize(in);
        }

        @Override
        public FullSize[] newArray(int size) {
            return new FullSize[size];
        }
    };

    public String getSourceUrl() {
        return sourceUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceUrl);
    }
}