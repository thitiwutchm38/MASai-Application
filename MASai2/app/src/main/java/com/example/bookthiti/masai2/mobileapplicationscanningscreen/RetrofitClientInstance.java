package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.137.127:5001/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(AppIcon.class, new MyDeserializer<AppIcon>())
//                    .create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
