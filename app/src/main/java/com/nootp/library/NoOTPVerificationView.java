package com.nootp.library;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class NoOTPVerificationView extends LinearLayout {
    private static final String TAG = "NoOTPVerificationView";
    private MaterialButton callToVerifyButton;
    private TextView poweredByTextView;
    private TextView poweredByNoOTPTextView;
    private String destinationMobileNumber;
    private String verificationUrl;
    private NoOTPApiService apiService;
    private VerificationStatusListener verificationStatusListener;
    private CallStateReceiver callStateReceiver;

    public NoOTPVerificationView(Context context) {
        super(context);
        init(context);
    }

    public NoOTPVerificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_nootp_verification, this, true);
        callToVerifyButton = findViewById(R.id.call_to_verify_button);
        poweredByTextView = findViewById(R.id.powered_by);
        poweredByNoOTPTextView = findViewById(R.id.powered_by_nootp);

        callToVerifyButton.setOnClickListener(v -> initiateCall());

        // Initialize the API service
        apiService = RetrofitClient.getClient().create(NoOTPApiService.class);
    }

    public void setDestinationMobileNumber(String mobileNumber) {
        this.destinationMobileNumber = mobileNumber;
    }

    public void setVerificationUrl(String url) {
        this.verificationUrl = url;
    }

    public void setVerificationStatusListener(VerificationStatusListener listener) {
        this.verificationStatusListener = listener;
    }

    private void initiateCall() {
        makeCall();
    }

    public void makeCall() {
        Context context = getContext();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "CALL_PHONE permission not granted");
            return;
        }

        // Register the call state receiver
        callStateReceiver = new CallStateReceiver(verificationUrl, apiService, verificationStatusListener);
        context.registerReceiver(callStateReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

        // Start the phone call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + destinationMobileNumber));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(callIntent);
        Log.d(TAG, "Calling " + destinationMobileNumber);
    }
}
