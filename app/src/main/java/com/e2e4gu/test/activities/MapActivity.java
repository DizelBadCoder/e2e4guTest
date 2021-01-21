package com.e2e4gu.test.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e2e4gu.test.R;

import com.e2e4gu.test.retrofit.DatabaseAPI;
import com.e2e4gu.test.retrofit.RetrofitUtils;
import com.e2e4gu.test.retrofit.models.Marker;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, PermissionsListener {
    private final String STYLE_URI = "mapbox://styles/dizelbadcoder/ckk5g8f991k4g17qqdj6bls5q";

    private TextView textViewDebug;
    private PermissionsManager permissionsManager;
    private Style fullyLoadedStyleMap;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,
                getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        textViewDebug = findViewById(R.id.textViewDebug);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //TODO release textview debug
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri(STYLE_URI), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                fullyLoadedStyleMap = style;
                checkMyLocation(null);
            }
        });
    }

    public void addMarker(View view) {
        //TODO 
        Toast.makeText(this, "Developing...", Toast.LENGTH_LONG).show();
    }

    public void checkMyLocation(View view) {
        if (fullyLoadedStyleMap != null)
            enableLocationComponent();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponentOptions customLocationComponentOptions =
                    LocationComponentOptions.builder(this)
                    .pulseEnabled(false)
                    .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(this, fullyLoadedStyleMap)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());
            locationComponent.setLocationComponentEnabled(true);
            currentLocation = locationComponent.getLastKnownLocation();
            moveCameraToMyLocation();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent();
                }
            });
        } else {
            Toast.makeText(this, "Not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void moveCameraToMyLocation() {
        if (currentLocation != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            currentLocation.getAltitude()), 15), 1200);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}