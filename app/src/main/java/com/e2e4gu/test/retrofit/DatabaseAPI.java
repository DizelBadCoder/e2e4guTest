package com.e2e4gu.test.retrofit;

import com.e2e4gu.test.retrofit.models.Marker;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DatabaseAPI {
    @GET("markers")
    List<Marker> getMarkerList(@Query("x") float x, @Query("y") float y, @Query("radius") float radius);
}
