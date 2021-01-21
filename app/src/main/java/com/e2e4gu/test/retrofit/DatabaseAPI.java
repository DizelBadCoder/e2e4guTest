package com.e2e4gu.test.retrofit;

import com.e2e4gu.test.retrofit.models.Marker;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DatabaseAPI {
    //TODO release variant
//    @GET("markers")
//    Call<List<Marker>> getMarkerList(@Query("x") double x, @Query("y") double y);

    @GET("markers")
    Call<List<Marker>> getMarkerList();
}
