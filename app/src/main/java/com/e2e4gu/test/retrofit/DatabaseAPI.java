package com.e2e4gu.test.retrofit;

import com.e2e4gu.test.retrofit.models.Marker;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DatabaseAPI {
    //TODO release variant
//    @GET("markers")
//    Call<List<Marker>> getMarkerList(@Query("x") double x, @Query("y") double y);

    @GET("markers")
    Call<List<Marker>> getMarkerList();

    @POST("newMarker")
    Call<ResponseBody> newMarker(@Body Marker marker);
}
