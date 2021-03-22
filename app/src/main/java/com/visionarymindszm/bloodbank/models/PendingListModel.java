package com.visionarymindszm.bloodbank.models;

public class PendingListModel {
    private String approvalStatus;
    private String name;
    private String hospital;
    private String dateOfBlood;
    private String reasonOfBlood;
    private String userType;
    private String ID;

    public PendingListModel(String ID, String approvalStatus, String name, String hospital, String dateOfBlood, String reasonOfBlood, String userType) {
        this.approvalStatus = approvalStatus;
        this.name = name;
        this.hospital = hospital;
        this.dateOfBlood = dateOfBlood;
        this.reasonOfBlood = reasonOfBlood;
        this.ID = ID;
        this.userType = userType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getName() {
        return name;
    }

    public String getHospital() {
        return hospital;
    }

    public String getDateOfBlood() {
        return dateOfBlood;
    }

    public String getReasonOfBlood() {
        return reasonOfBlood;
    }

    public String getUserType() {
        return userType;
    }

    public String getID() {
        return ID;
    }
}
