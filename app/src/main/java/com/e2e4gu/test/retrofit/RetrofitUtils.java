package com.e2e4gu.test.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static final String BASE_URL =
            "https://my-json-server.typicode.com/DizelBadCoder/e2e4gu-rest/";

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
