package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Sizes implements Parcelable {
    @SerializedName("full")
    private FullSize fullSize = new FullSize();
    @SerializedName("thumbnail")
    private ThumbnailSize thumbnailSize=new ThumbnailSize();
    @SerializedName("medium")
    private Medium medium=new Medium();


    public Sizes() {
    }


    protected Sizes(Parcel in) {
        fullSize = in.readParcelable(FullSize.class.getClassLoader());
        thumbnailSize = in.readParcelable(ThumbnailSize.class.getClassLoader());
        medium = in.readParcelable(Medium.class.getClassLoader());
    }

    public static final Creator<Sizes> CREATOR = new Creator<Sizes>() {
        @Override
        public Sizes createFromParcel(Parcel in) {
            return new Sizes(in);
        }

        @Override
        public Sizes[] newArray(int size) {
            return new Sizes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(fullSize, flags);
        dest.writeParcelable(thumbnailSize, flags);
        dest.writeParcelable(medium, flags);
    }

    public FullSize getFullSize() {
        return fullSize;
    }

    public ThumbnailSize getThumbnailSize() {
        return thumbnailSize;
    }

    public Medium getMedium() {
        return medium;
    }

    public static Creator<Sizes> getCREATOR() {
        return CREATOR;
    }
}
