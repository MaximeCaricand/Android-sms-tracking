package com.example.mobiletracking;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getPhoneNumber() { return phoneNumber; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public long getDate() { return date; }

    public LatLng getLatLng() { return new LatLng(this.lat, this.lng); }

    public String getHourFormat() {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
        return formater.format(new Date(this.date));
    }

    public String getFullDateFormat() {
        SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return formater.format(new Date(this.date));
    }
}
