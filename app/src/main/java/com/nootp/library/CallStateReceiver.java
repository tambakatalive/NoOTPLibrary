package com.nootp.library;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Handler;
import android.os.Looper;

import android.telephony.TelephonyManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallStateReceiver extends BroadcastReceiver {

    public static String TAG = "CallStateReceiver";

    SharedPreferences sharedPref;

    private String verificationUrl;
    private NoOTPApiService apiService;
    private VerificationStatusListener verificationStatusListener;

    public CallStateReceiver(String verificationUrl, NoOTPApiService apiService, VerificationStatusListener verificationStatusListener) {

        this.apiService = apiService;
        this.verificationUrl = verificationUrl;
        this.verificationStatusListener = verificationStatusListener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action != null && action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state != null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                    delayedCall(context);
                }
            }
        }
    }

    private void delayedCall(Context context){

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                fetchVerificationStatus();
            }
        }, 3000);

    }


    private void fetchVerificationStatus() {
        Call<VerificationResponse> call = apiService.getVerificationStatus(verificationUrl);
        call.enqueue(new Callback<VerificationResponse>() {
            @Override
            public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                if (response.isSuccessful() && verificationStatusListener != null) {
                    VerificationResponse verificationResponse = response.body();
                    verificationStatusListener.onVerificationStatusReceived(verificationResponse.getMobileNumber(), verificationResponse.isStatus());
                }
            }

            @Override
            public void onFailure(Call<VerificationResponse> call, Throwable t) {
                // Handle the error
            }
        });
    }

}

