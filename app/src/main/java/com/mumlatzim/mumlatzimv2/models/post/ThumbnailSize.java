package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ThumbnailSize implements Parcelable {
    @SerializedName("source_url")
    private String sourceUrl;

    public ThumbnailSize() {
    }

    protected ThumbnailSize(Parcel in) {
        sourceUrl = in.readString();
    }

    public static final Creator<ThumbnailSize> CREATOR = new Creator<ThumbnailSize>() {
        @Override
        public ThumbnailSize createFromParcel(Parcel in) {
            return new ThumbnailSize(in);
        }

        @Override
        public ThumbnailSize[] newArray(int size) {
            return new ThumbnailSize[size];
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
