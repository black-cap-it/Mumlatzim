package com.mumlatzim.mumlatzimv2.models.comment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Link implements Parcelable {

    @SerializedName("children")
    private ArrayList<CommentLink> mComments = new ArrayList<>();

    public Link() {
    }

    protected Link(Parcel in) {
        mComments = in.createTypedArrayList(CommentLink.CREATOR);
    }

    public static final Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel in) {
            return new Link(in);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    public ArrayList<CommentLink> getmComments() {
        return mComments;
    }

    public void setmComments(ArrayList<CommentLink> mComments) {
        this.mComments = mComments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mComments);
    }
}
