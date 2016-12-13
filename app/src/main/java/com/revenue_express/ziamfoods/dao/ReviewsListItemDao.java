package com.revenue_express.ziamfoods.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ChetPC on 10/31/2016.
 */
public class ReviewsListItemDao implements Parcelable {


    @SerializedName("data") private List<ReviewsItemDao> data;

    protected ReviewsListItemDao(Parcel in) {
        data = in.createTypedArrayList(ReviewsItemDao.CREATOR);
    }

    public static final Creator<ReviewsListItemDao> CREATOR = new Creator<ReviewsListItemDao>() {
        @Override
        public ReviewsListItemDao createFromParcel(Parcel in) {
            return new ReviewsListItemDao(in);
        }

        @Override
        public ReviewsListItemDao[] newArray(int size) {
            return new ReviewsListItemDao[size];
        }
    };

    public List<ReviewsItemDao> getData() {
        return data;
    }

    public void setData(List<ReviewsItemDao> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(data);
    }
}
