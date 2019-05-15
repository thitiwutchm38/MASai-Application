package com.example.bookthiti.masai2.internet;

import com.example.bookthiti.masai2.mainscreen.model.PostRequestBody;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen.TargetApplicationInfo;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen.TargetApplicationScanningResult;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MasaiServerAPI {

    @GET("/api/search")
    Call<List<TargetApplicationInfo>> getAllTargetApplicationInfo(@Query("keyword") String keyword);

    @GET("/api/info")
    Call<TargetApplicationScanningResult> getAppScanningResult(@Query("package_name") String packageName, @Query("version_code") int versionCode);

    @POST("/api/report")
    Call<ResponseBody> postGenerateReport(@Body PostRequestBody postRequestBody);
}
