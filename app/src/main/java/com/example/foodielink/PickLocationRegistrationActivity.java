package com.example.foodielink;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PickLocationRegistrationActivity extends FragmentActivity implements OnMapReadyCallback {

    // Google Map instance
    private GoogleMap mMap;

    // Selected coordinates from map click
    private LatLng selectedLatLng;

    // Save button reference
    private Button btnSaveLocation;

    // Location provider for current location
    private FusedLocationProviderClient fusedLocationClient;

    // Permission request code
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout for the activity
        setContentView(R.layout.activity_pick_location_registration);

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize UI components
        btnSaveLocation = findViewById(R.id.btnSaveLocation);
        EditText searchLocation = findViewById(R.id.search_location);

        // Handle location search input
        searchLocation.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            String locationName = searchLocation.getText().toString();
            if (!locationName.isEmpty()) {

                // Check if network is available
                if (!isNetworkAvailable()) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return true;
                }

                // Perform geocoding
                Geocoder geocoder = new Geocoder(PickLocationRegistrationActivity.this, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // Move map to searched location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    } else {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        });

        // Handle save location button click
        btnSaveLocation.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                // Return selected coordinates to previous screen
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLatLng.latitude);
                resultIntent.putExtra("longitude", selectedLatLng.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize and load the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // When the map is ready
        mMap = googleMap;

        // Enable location features if permission granted
        enableMyLocation();

        // Handle user click on map to select a location
        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    // Enable current location and move camera if available
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Enable my-location button on map
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Get last known location and move camera
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        } else {
                            // Move to default location (Tel Aviv) if location unavailable
                            LatLng defaultLocation = new LatLng(32.0853, 34.7818);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));
                        }
                    });

        } else {
            // Request location permission from user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permission denied. Showing default location.", Toast.LENGTH_SHORT).show();
                LatLng defaultLocation = new LatLng(32.0853, 34.7818);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));
            }
        }
    }

    // Check if the device is connected to the internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
