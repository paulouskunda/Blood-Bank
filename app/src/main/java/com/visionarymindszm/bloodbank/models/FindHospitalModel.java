package com.visionarymindszm.bloodbank.models;

public class FindHospitalModel {
    private String hospitalID;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalLat;
    private String hospitalLong;

    public FindHospitalModel(String hospitalID, String hospitalName, String hospitalAddress, String hospitalLat, String hospitalLong) {
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalLat = hospitalLat;
        this.hospitalLong = hospitalLong;
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
