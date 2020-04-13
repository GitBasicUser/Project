package com.example.deliveryapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
    String place;
    Bitmap image;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place);
        dest.writeParcelable(image, flags);
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data(Parcel src){
        readFromParcel(src);
    }

    public void readFromParcel(Parcel src){
        place = src.readString();
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place){
        this.place = place;
    }
}
