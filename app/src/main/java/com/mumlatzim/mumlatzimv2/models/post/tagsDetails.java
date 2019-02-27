package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class tagsDetails implements Parcelable {

    @SerializedName("term_id")
    private Double mID;
    @SerializedName("name")
    private String mName;

    public tagsDetails() {
    }

    protected tagsDetails(Parcel in) {
        if (in.readByte() == 0) {
            mID = null;
        } else {
            mID = in.readDouble();
        }
        mName = in.readString();
    }

    public static final Creator<tagsDetails> CREATOR = new Creator<tagsDetails>() {
        @Override
        public tagsDetails createFromParcel(Parcel in) {
            return new tagsDetails(in);
        }

        @Override
        public tagsDetails[] newArray(int size) {
            return new tagsDetails[size];
        }
    };

    public Double getmID() {
        return mID;
    }

    public void setmID(Double mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mID);
        }
        dest.writeString(mName);
    }
}
