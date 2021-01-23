package com.e2e4gu.test.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.e2e4gu.test.R;
import com.e2e4gu.test.retrofit.DatabaseAPI;
import com.e2e4gu.test.retrofit.RetrofitUtils;
import com.e2e4gu.test.retrofit.models.Marker;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnLocationStaleListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeta;
import com.mapbox.turf.TurfTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MapActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnCameraMoveListener {

    private final String CURRENT_LOCATION_SOURCE_ID = "CURRENT_LOCATION_SOURCE_ID";
    private final String FILL_LAYER_ID = "FILL_LAYER_ID";

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location currentLocation;
    private TextView textViewDebug;
    private boolean isDebug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Mapbox.getInstance(this,
                getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        textViewDebug = findViewById(R.id.textViewDebug);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        String STYLE_URI = "mapbox://styles/dizelbadcoder/ckk5g8f991k4g17qqdj6bls5q";
        mapboxMap.setStyle(new Style.Builder().fromUri(STYLE_URI), style -> {
            style.addSource(new GeoJsonSource(CURRENT_LOCATION_SOURCE_ID));

            FillLayer fillLayer = new FillLayer(FILL_LAYER_ID,
                    CURRENT_LOCATION_SOURCE_ID);
            fillLayer.setProperties(
                    fillColor(getResources().getColor(R.color.soundcloud_50pct)),
                    fillOpacity(.3f));
            style.addLayer(fillLayer);

            enableLocationComponent(style);
            mapboxMap.addOnCameraMoveListener(this);
        });
    }

    public void addMarker(View view) {
        Toast.makeText(this, "Developing...", Toast.LENGTH_LONG).show();
    }

    public void checkMyLocation(View view) {
        if (currentLocation != null) {
            moveCameraToMyLocation();
        } else {
            requestLocationPermission();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponentOptions customLocationComponentOptions =
                    LocationComponentOptions.builder(this)
                            .pulseEnabled(false)
                            .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(this, style)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            mapboxMap.getUiSettings().setCompassEnabled(false);
            mapboxMap.getUiSettings().setAttributionEnabled(false);
            mapboxMap.getUiSettings().setLogoEnabled(false);

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

            currentLocation = locationComponent.getLastKnownLocation();
            if (currentLocation != null) {

                Polygon polygonArea = TurfTransformation.circle(
                        locationToPoint(currentLocation), 10000, 360,
                        TurfConstants.UNIT_METERS);
                GeoJsonSource polygonCircleSource = style.getSourceAs(CURRENT_LOCATION_SOURCE_ID);
                polygonCircleSource.setGeoJson(Polygon.fromOuterInner(
                        LineString.fromLngLats(TurfMeta.coordAll(polygonArea, false))));

                initMarkers(style);
                moveCameraToMyLocation();
                locationComponent.addOnIndicatorPositionChangedListener(indicator -> {
                    //update current position
                    currentLocation = locationComponent.getLastKnownLocation();

                });
            }
        } else {
            requestLocationPermission();
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
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, "Not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void moveCameraToMyLocation() {
        if (currentLocation != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            currentLocation.getAltitude()), 12), 1400);
        }
    }

    private void requestLocationPermission() {
        permissionsManager = new PermissionsManager(this);
        permissionsManager.requestLocationPermissions(this);
    }

    private void initMarkers(@NonNull Style style) {
        RetrofitUtils.getRetrofit()
                .create(DatabaseAPI.class)
//                TODO release variant
//                .getMarkerList(currentLocation.getLatitude(),
//                        currentLocation.getLongitude())
                .getMarkerList()
                .enqueue(new Callback<List<Marker>>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<List<Marker>> call,
                                           Response<List<Marker>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() == null) return;
                            List<Marker> markers = response.body();
                            List<Feature> features = new ArrayList<>();

                            for (Marker it : markers) {
                                Location location = new Location((String) null);
                                location.setLongitude(it.getLng());
                                location.setLatitude(it.getLat());
                                if (currentLocation.distanceTo(location) > 10000) continue;
                                features.add(Feature.fromGeometry(
                                        Point.fromLngLat(it.getLng(), it.getLat())
                                ));
                            }

                            style.addImage("MARKER",
                                    Objects.requireNonNull(ResourcesCompat.getDrawable(
                                            getResources(),
                                            R.drawable.ic_baseline_location_on_24,
                                            null
                                    )));
                            style.addSource(new GeoJsonSource("SOURCE_MARKER",
                                    FeatureCollection.fromFeatures(features)));
                            style.addLayer(new SymbolLayer("LAYER_MARKER",
                                    "SOURCE_MARKER").withProperties(
                                            iconImage("MARKER"),
                                            iconAllowOverlap(true),
                                            iconIgnorePlacement(true),
                                            iconAnchor(ICON_ANCHOR_BOTTOM)));
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<List<Marker>> call, Throwable t) {
                        Toast.makeText(MapActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }

    private void postNewMarker(Marker marker) {
        RetrofitUtils.getRetrofit()
                .create(DatabaseAPI.class)
                .newMarker(marker)
                .enqueue(new Callback<ResponseBody>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MapActivity.this, R.string.toast_successful,
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MapActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }

    private Point locationToPoint(Location location) {
        return Point.fromLngLat(
                location.getLongitude(),
                location.getLatitude()
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCameraMove() {
        if (isDebug) {
            textViewDebug.setText("Lng: " + mapboxMap.getCameraPosition().target.getLongitude() +
                    "\nLat: " + mapboxMap.getCameraPosition().target.getLatitude() +
                    "\nZoom: " + mapboxMap.getCameraPosition().zoom +
                    "\nBearing: " + mapboxMap.getCameraPosition().bearing +
                    "\nTilt: " + mapboxMap.getCameraPosition().tilt
            );
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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