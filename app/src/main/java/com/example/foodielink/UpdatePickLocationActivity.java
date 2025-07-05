package com.example.foodielink;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import android.widget.Button;

public class UpdatePickLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng selectedLatLng;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText searchLocationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity_pick_location);
        searchLocationEditText = findViewById(R.id.search_location);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = mAuth.getCurrentUser().getUid();
                GeoPoint location = new GeoPoint(selectedLatLng.latitude, selectedLatLng.longitude);

                db.collection("Users").document(userId)
                        .set(Collections.singletonMap("location", location), SetOptions.merge())
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Location updated successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to update location", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
            }
        });

        searchLocationEditText.setOnEditorActionListener((v, actionId, event) -> {
            String locationName = searchLocationEditText.getText().toString().trim();
            if (!locationName.isEmpty()) {
                searchLocationByName(locationName);
            }
            return true;
        });
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //Button to navigate to Chat screen on click
        ImageView navChat = findViewById(R.id.nav_chat);
        navChat.setOnClickListener(v -> {
            Intent intent = new Intent(UpdatePickLocationActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        //Button to navigate to Profile screen on click
        ImageView navProfile = findViewById(R.id.nav_profile);
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UpdatePickLocationActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Button to navigate to Search screen on click
        ImageView navSearch = findViewById(R.id.nav_search);
        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(UpdatePickLocationActivity.this, SwipePage.class);
            startActivity(intent);
        });

        // Button to navigate to Login Page
        ImageView logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpdatePickLocationActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void searchLocationByName(String locationName) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                selectedLatLng = latLng;

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();

        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                        }
                    });

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }
}