package com.e2e4gu.test.retrofit.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class Marker {
    private final String DEFAULT_NAME = "None";

    @Expose private String name;
    @Expose private final double lng;
    @Expose private final double lat;

    public Marker(double lng, double lat) {
        this.name = DEFAULT_NAME;
        this.lng = lng;
        this.lat = lat;
    }

    public Marker(double lng, double lat, String name) {
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name : " + name +
                "\nLng : " + lng +
                "\nLat : " + lat + "\n";
    }
}
