// NoOTPApiService.java
package com.nootp.library;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NoOTPApiService {

    @GET
    Call<VerificationResponse> getVerificationStatus(@Url String url);

}
