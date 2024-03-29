package com.example.bookthiti.masai2.internet;

import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.mainscreen.model.PostRequestBody;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen.AppIcon;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen.AppVulnerability;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen.Permission;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen.TargetApplicationInfo;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen.TargetApplicationScanningResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    public static String BASE_URL = "http://192.168.137.1:5000/";

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
                    .client(new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance(String url) throws Exception {
        Pattern pattern = Pattern.compile("http://.+\\.ngrok\\.io/");
        Matcher matcher = pattern.matcher(url);
        if (! matcher.matches()) {
            throw new Exception("The url is not corrected to the API Server");
        }
        BASE_URL = url;
        if (retrofit != null) {
            clear();
        }
        return getRetrofitInstance();
    }

    public static void clear() {
        retrofit = null;
    }
}
