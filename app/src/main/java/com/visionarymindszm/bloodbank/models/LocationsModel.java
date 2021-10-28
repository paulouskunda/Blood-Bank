package com.visionarymindszm.bloodbank.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationsModel implements Parcelable {
    private Double latitude;
    private Double longitude;
    private String placeName;

    public LocationsModel() {
    }

    public LocationsModel(Double latitude, Double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    protected LocationsModel(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        placeName = in.readString();
    }

    public static final Creator<LocationsModel> CREATOR = new Creator<LocationsModel>() {
        @Override
        public LocationsModel createFromParcel(Parcel in) {
            return new LocationsModel(in);
        }

        @Override
        public LocationsModel[] newArray(int size) {
            return new LocationsModel[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(placeName);
    }
}
