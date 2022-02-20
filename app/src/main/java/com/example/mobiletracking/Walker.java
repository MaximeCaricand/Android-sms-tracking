package com.example.mobiletracking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Walker {
    private int numberStep;
    private float currentTimeSpeed;
    private float distTraveled;
    private long date;

    Walker(int numberStep, float currentTimeSpeed, float distTraveled, long date){
        this.numberStep = numberStep;
        this.currentTimeSpeed = currentTimeSpeed;
        this.distTraveled = distTraveled;
        this.date = date;
    }

    public int getNumberStep() { return this.numberStep; }
    public float getCurrentTimeSpeed() { return this.currentTimeSpeed; }
    public float getDistTraveled() { return this.distTraveled; }
    public long getDate() { return date; }
    public String getDateFormat() {
        SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return formater.format(new Date(date));
    }
}
