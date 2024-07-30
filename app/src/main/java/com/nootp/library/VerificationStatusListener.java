// VerificationStatusListener.java
package com.nootp.library;

public interface VerificationStatusListener {
    void onVerificationStatusReceived(String mobileNumber, boolean isVerified);
}
