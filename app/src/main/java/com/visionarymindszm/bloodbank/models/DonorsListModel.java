package com.visionarymindszm.bloodbank.models;

public class DonorsListModel {
    private String donorID;
    private String donorName;
    private String donorAddress;
    private String donorTown;
    private String donorPhoneNumber;
    private String donorEmailAddress;
    private String donorBloodGroup;

    public DonorsListModel(String donorID,String donorName, String donorAddress,
                           String donorTown, String donorPhoneNumber,
                           String donorEmailAddress,
                           String donorBloodGroup) {
        this.donorName = donorName;
        this.donorAddress = donorAddress;
        this.donorTown = donorTown;
        this.donorPhoneNumber = donorPhoneNumber;
        this.donorEmailAddress = donorEmailAddress;
        this.donorBloodGroup = donorBloodGroup;
        this.donorID = donorID;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public String getDonorTown() {
        return donorTown;
    }

    public String getDonorBloodGroup() {
        return donorBloodGroup;
    }

    public String getDonorEmailAddress() {
        return donorEmailAddress;
    }

    public String getDonorPhoneNumber() {
        return donorPhoneNumber;
    }

    public String getDonorID() {
        return donorID;
    }
}
