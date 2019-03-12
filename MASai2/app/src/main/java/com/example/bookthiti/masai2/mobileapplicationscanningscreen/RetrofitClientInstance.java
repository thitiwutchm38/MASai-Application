package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.mainscreen.model.PostRequestBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    public static final String BASE_URL = "http://192.168.137.1:5001/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AppIcon.class, new AppIcon.AppIconDeserializer())
                    .registerTypeAdapter(TargetApplicationInfo.class, new TargetApplicationInfo.TargetApplicationInfoDeserializer())
                    .registerTypeAdapter(AppVulnerability.class, new AppVulnerability.AppVulnerabilityDeserializer())
                    .registerTypeAdapter(Permission.class, new Permission.PermissionDeserializer())
                    .registerTypeAdapter(TargetApplicationScanningResult.class, new TargetApplicationScanningResult.TargetApplicationScanningResultDeserializer())
                    .registerTypeAdapter(ActivityLogEntity.class, new ActivityLogEntity.ActivityLogEntitySerializer())
                    .registerTypeAdapter(PostRequestBody.class, new PostRequestBody.PostRequestBodySerializer())
                    .create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
