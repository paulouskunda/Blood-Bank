package com.visionarymindszm.bloodbank.models;

public class FindHospitalModel {
    private String hospitalID;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalLat;
    private String hospitalLong;
    private String hospitalCity;
    private String hospitalLocation;


    public FindHospitalModel(String hospitalID, String hospitalName, String hospitalAddress, String hospitalLat, String hospitalLong, String hospitalCity, String hospitalLocation) {
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalLat = hospitalLat;
        this.hospitalLong = hospitalLong;
        this.hospitalCity = hospitalCity;
        this.hospitalLocation = hospitalLocation;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public String getHospitalLocation() {
        return hospitalLocation;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public String getHospitalLat() {
        return hospitalLat;
    }

    public String getHospitalLong() {
        return hospitalLong;
    }
}
