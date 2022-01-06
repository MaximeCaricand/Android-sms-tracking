package com.example.mobiletracking;

public class Victim {
    private int id;
    private int nbPas;
    private float avgSpeed;
    private float distParcouru;
    private String date;
    private String heure;

    Victim(int nbPas,float avgSpeed, float distParcouru, String date, String heure){
        this.nbPas = nbPas;
        this.avgSpeed = avgSpeed;
        this.distParcouru = distParcouru;
        this.date = date;
        this.heure = heure;
    }

    public int getId(){return this.id;}
    public int getNbPas() { return this.nbPas; }
    public float getAvgSpeed() { return this.avgSpeed; }
    public float getDistParcouru() { return this.distParcouru; }
    public String getDate() { return this.date; }
    public String getHeure() { return this.heure; }

}
