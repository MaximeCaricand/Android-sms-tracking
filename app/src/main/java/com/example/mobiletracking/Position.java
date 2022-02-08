package com.example.mobiletracking;

import com.google.android.gms.maps.model.LatLng;

public class Position {

    private String phoneNumber;
    private double lat;
    private double lng;
    private long date;

    public Position(String phoneNumber, double lat, double lng, long date) {
        this.phoneNumber = phoneNumber;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public Position(String phoneNumber, LatLng position, long date) {
        this(phoneNumber, position.latitude, position.longitude, date);
    }

    public String getPhoneNumber() { return phoneNumber; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public long getDate() { return date; }

    public LatLng getLatLng() { return new LatLng(this.lat, this.lng); }
    public String getFormatedDate() {
        return "";
    }
}
