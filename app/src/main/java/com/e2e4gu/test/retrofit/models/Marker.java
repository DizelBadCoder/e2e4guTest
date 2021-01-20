package com.e2e4gu.test.retrofit.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class Marker {
    private final String DEFAULT_COLOR = "ffffff";
    private final String DEFAULT_NAME = "None";

    @Expose private String name;
    @Expose private String color;
    @Expose private final float x;
    @Expose private final float y;

    public Marker(float x, float y) {
        this.name = DEFAULT_NAME;
        this.color = DEFAULT_COLOR;
        this.x = x;
        this.y = y;
    }

    public Marker(float x, float y, String name) {
        this.name = name;
        this.color = DEFAULT_COLOR;
        this.x = x;
        this.y = y;
    }

    public Marker(float x, float y, String name, String color) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name : " + name +
                "\nColor : " + color +
                "\nX : " + x +
                "\nY : " + y + "\n";
    }
}
