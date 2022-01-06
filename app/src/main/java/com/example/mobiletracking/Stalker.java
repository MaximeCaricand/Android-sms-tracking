package com.example.mobiletracking;

public class Stalker {
    private int id;
    private String tel;
    private String localisation;

    Stalker(int id,String tel, String localisation){
        this.tel=tel;
        this.localisation=localisation;
    }

    public int getId(){return this.id;}
    public String getTel() { return this.tel; }
    public String getLocalisation() { return this.localisation; }
}
