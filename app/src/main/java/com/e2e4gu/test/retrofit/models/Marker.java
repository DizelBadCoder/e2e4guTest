package com.e2e4gu.test.retrofit.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class Marker {
    private final String DEFAULT_COLOR = "ffffff";
    private final String DEFAULT_NAME = "None";

    @Expose private String name;
    @Expose private String color;
    @Expose private final double lng;
    @Expose private final double lat;

    public Marker(double lng, double lat) {
        this.name = DEFAULT_NAME;
        this.color = DEFAULT_COLOR;
        this.lng = lng;
        this.lat = lat;
    }

    public Marker(double lng, double lat, String name) {
        this.name = name;
        this.color = DEFAULT_COLOR;
        this.lng = lng;
        this.lat = lat;
    }

    public Marker(double lng, double lat, String name, String color) {
        this.name = name;
        this.color = color;
        this.lng = lng;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
                "\nColor : " + color +
                "\nLng : " + lng +
                "\nLat : " + lat + "\n";
    }
}
