package com.nootp.library;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements VerificationStatusListener {

    private NoOTPVerificationView nootpVerificationView;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nootpVerificationView = findViewById(R.id.nootp_verification_view);
        nootpVerificationView.setDestinationMobileNumber("09970515198");
        nootpVerificationView.setVerificationUrl("https://yourserver.com/verify");
        nootpVerificationView.setVerificationStatusListener(this);

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.READ_PHONE_STATE

            }, PERMISSION_REQUEST_CODE);
        } else {
            // If permissions are already granted, make the call
            nootpVerificationView.makeCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                nootpVerificationView.makeCall();
            } else {
                // Handle the case where permissions were not granted
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onVerificationStatusReceived(String mobileNumber, boolean isVerified) {
        if (isVerified) {
            // Handle verified number
            Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
        } else {
            // Handle unverified number
            Toast.makeText(this, "Unverified", Toast.LENGTH_SHORT).show();
        }
    }
}
