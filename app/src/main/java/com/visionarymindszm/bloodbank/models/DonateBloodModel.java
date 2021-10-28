package com.visionarymindszm.bloodbank.models;

public class DonateBloodModel {
    private String donID;
    private String donName;
    private String donBG;
    private String donHos;
    private String donReason;
    private String donDate;
    private String donCity;
    private String donIDRef;


    public DonateBloodModel(String donID, String donName, String donBG, String donHos, String donReason, String donDate, String donCity, String donIDRef) {
        this.donID = donID;
        this.donName = donName;
        this.donBG = donBG;
        this.donHos = donHos;
        this.donReason = donReason;
        this.donDate = donDate;
        this.donCity = donCity;
        this.donIDRef = donIDRef;
    }

    public String getDonID() {
        return donID;
    }

    public String getDonName() {
        return donName;
    }

    public String getDonBG() {
        return donBG;
    }

    public String getDonHos() {
        return donHos;
    }

    public String getDonReason() {
        return donReason;
    }

    public String getDonDate() {
        return donDate;
    }

    public String getDonCity() {
        return donCity;
    }

    public String getDonIDRef() {
        return donIDRef;
    }
}
