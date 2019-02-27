package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostReport implements Parcelable {
    @SerializedName("success")
    private int Success;
    @SerializedName("message")
    private String Message;

    public PostReport() {
    }

    protected PostReport(Parcel in) {
        Success = in.readInt();
        Message = in.readString();
    }

    public static final Creator<PostReport> CREATOR = new Creator<PostReport>() {
        @Override
        public PostReport createFromParcel(Parcel in) {
            return new PostReport(in);
        }

        @Override
        public PostReport[] newArray(int size) {
            return new PostReport[size];
        }
    };

    public int getSuccess() {
        return Success;
    }

    public String getMessage() {
        return Message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Success);
        dest.writeString(Message);
    }
}
