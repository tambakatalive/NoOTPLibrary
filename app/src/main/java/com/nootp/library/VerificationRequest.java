package com.nootp.library;

// VerificationRequest.java


public class VerificationRequest {
    private String mobileNumber;
    private boolean status;

    public VerificationRequest(String mobileNumber, boolean status) {
        this.mobileNumber = mobileNumber;
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean isStatus() {
        return status;
    }
}



