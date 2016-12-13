package com.revenue_express.ziamfoods.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChetPC on 10/31/2016.
 */
public class ReviewsItemDao implements Parcelable {
    @SerializedName("memh_firstname") private String name;
    @SerializedName("bsrh_title") private String title;
    @SerializedName("bsrh_desc") private String detail;
    @SerializedName("bsrh_score") private String score;


    protected ReviewsItemDao(Parcel in) {
        name = in.readString();
        title = in.readString();
        detail = in.readString();
        score = in.readString();
    }

    public static final Creator<ReviewsItemDao> CREATOR = new Creator<ReviewsItemDao>() {
        @Override
        public ReviewsItemDao createFromParcel(Parcel in) {
            return new ReviewsItemDao(in);
        }

        @Override
        public ReviewsItemDao[] newArray(int size) {
            return new ReviewsItemDao[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static Creator<ReviewsItemDao> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(detail);
        parcel.writeString(score);
    }
}
