package com.example.gabriel.app.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 10/07/16.
 */

public class Movie implements Parcelable {

    private String mtitle;
    private String mOverview;
    private String mGridImageUrl;
    private String mDetailImageUrl;
    private String mReleaseDate;
    private double mRating;

    public Movie(String title, String overview, String imageUrl, String detailImageUrl, String releaseDate, double rating){
        mtitle = title;
        mOverview = overview;
        mGridImageUrl = imageUrl;
        mDetailImageUrl = detailImageUrl;
        mReleaseDate = releaseDate;
        mRating = rating;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getGridImageUrl() {
        return mGridImageUrl;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getRating() {
        return mRating;
    }

    public String getDetailImageUrl() {
        return mDetailImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mtitle);
        dest.writeString(mOverview);
        dest.writeString(mDetailImageUrl);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mRating);
    }
    public static final Parcelable.Creator<Movie> CREATOR  = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private Movie(Parcel in){
        mtitle = in.readString();
        mOverview = in.readString();
        mDetailImageUrl = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readDouble();
    }
}
