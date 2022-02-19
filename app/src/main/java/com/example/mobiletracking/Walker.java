package com.example.mobiletracking;

public class Walker {
    private int numberStep;
    private float currentTimeSpeed;
    private float distTraveled;
    private String date;
    private String hour;

    Walker(int numberStep, float currentTimeSpeed, float distTraveled, String date, String hour){
        this.numberStep = numberStep;
        this.currentTimeSpeed = currentTimeSpeed;
        this.distTraveled = distTraveled;
        this.date = date;
        this.hour = hour;
    }

    public int getNbPas() { return this.numberStep; }
    public float getAvgSpeed() { return this.currentTimeSpeed; }
    public float getDistParcouru() { return this.distTraveled; }
    public String getDate() { return this.date; }
    public String getHeure() { return this.hour; }
}
