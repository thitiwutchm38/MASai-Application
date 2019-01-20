package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MasaiServerAPI {

    @GET("/api/search")
    Call<List<TargetApplicationInfo>> getAllTargetApplicationInfo(@Query("keyword") String keyword);
}
