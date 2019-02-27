package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class categoriesDetails implements Parcelable {

    @SerializedName("category_nicename")
    private String mCatName;

    public categoriesDetails() {
    }

    public String getmCatName() {
        return mCatName;
    }

    protected categoriesDetails(Parcel in) {
        mCatName = in.readString();
    }

    public static final Creator<categoriesDetails> CREATOR = new Creator<categoriesDetails>() {
        @Override
        public categoriesDetails createFromParcel(Parcel in) {
            return new categoriesDetails(in);
        }

        @Override
        public categoriesDetails[] newArray(int size) {
            return new categoriesDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCatName);
    }
}
