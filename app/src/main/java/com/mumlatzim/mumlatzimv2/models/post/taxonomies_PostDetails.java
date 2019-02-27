package com.mumlatzim.mumlatzimv2.models.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class taxonomies_PostDetails implements Parcelable {

    @SerializedName("categories")
    private ArrayList<categoriesDetails> mcategories_Details=new ArrayList<>();
    @SerializedName("tags")
    private ArrayList<tagsDetails> mtags_Details= new ArrayList<>();

    public taxonomies_PostDetails() {
    }

    protected taxonomies_PostDetails(Parcel in) {
        mcategories_Details = in.createTypedArrayList(categoriesDetails.CREATOR);
        mtags_Details = in.createTypedArrayList(tagsDetails.CREATOR);
    }

    public static final Creator<taxonomies_PostDetails> CREATOR = new Creator<taxonomies_PostDetails>() {
        @Override
        public taxonomies_PostDetails createFromParcel(Parcel in) {
            return new taxonomies_PostDetails(in);
        }

        @Override
        public taxonomies_PostDetails[] newArray(int size) {
            return new taxonomies_PostDetails[size];
        }
    };

    public ArrayList<categoriesDetails> getMcategories_Details() {
        return mcategories_Details;
    }

    public ArrayList<tagsDetails> getMtags_Details() {
        return mtags_Details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mcategories_Details);
        dest.writeTypedList(mtags_Details);
    }
}
