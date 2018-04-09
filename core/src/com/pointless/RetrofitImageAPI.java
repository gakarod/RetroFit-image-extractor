package com.pointless;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

public interface RetrofitImageAPI {
    @GET("api/RetrofitAndroidImageResponse")
    Call<ResponseBody> getImageDetails();
}
