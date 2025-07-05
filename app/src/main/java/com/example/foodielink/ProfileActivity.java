package com.example.foodielink;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView aboutText, nameText, ageText, locationText;
    private TextView cookingPrefText, dietaryPrefText, whyHereText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind UI TextViews
        aboutText = findViewById(R.id.about_text);
        nameText = findViewById(R.id.user_name);
        ageText = findViewById(R.id.user_age);
        locationText = findViewById(R.id.user_location);
        cookingPrefText = findViewById(R.id.cooking_preferences);
        dietaryPrefText = findViewById(R.id.dietary_preferences);
        whyHereText = findViewById(R.id.why_here);

        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch user document from Firestore
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String about = documentSnapshot.getString("aboutMe");
                        String name = documentSnapshot.getString("name");
                        Long age = documentSnapshot.getLong("age");
                        GeoPoint geoPoint = documentSnapshot.getGeoPoint("location");
                        String cooking = documentSnapshot.getString("cookingPreference");
                        String dietary = documentSnapshot.getString("dietaryPreference");
                        String why = documentSnapshot.getString("whyHere");

                        aboutText.setText(about != null ? about : "");
                        nameText.setText("Name: " + (name != null ? name : ""));
                        ageText.setText("Age: " + (age != null ? age.toString() : ""));

                        // Convert coordinates to address
                        if (geoPoint != null) {
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    String address = addresses.get(0).getAddressLine(0); // או getLocality() לשם העיר בלבד
                                    locationText.setText("Location: " + address);
                                } else {
                                    locationText.setText("Location: Unknown");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                locationText.setText("Location: Error");
                            }
                        } else {
                            locationText.setText("Location: N/A");
                        }

                        cookingPrefText.setText("Cooking Preferences: " + (cooking != null ? cooking : ""));
                        dietaryPrefText.setText("Dietary Preferences: " + (dietary != null ? dietary : ""));
                        whyHereText.setText("Why I'm here: " + (why != null ? why : ""));
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        // Button to navigate to Chat screen on click
        ImageView chatButton = findViewById(R.id.nav_chat);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        // Button to navigate to Location screen on click
        ImageView navLocation = findViewById(R.id.nav_location);
        navLocation.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpdatePickLocationActivity.class);
            startActivity(intent);
        });

        // Button to navigate to Search screen on click
        ImageView navSearch = findViewById(R.id.nav_search);
        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SwipePage.class);
            startActivity(intent);
        });

        // Button to navigate to Login Page
        ImageView logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button to navigate to Edit Profile Page
        ImageView editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
}