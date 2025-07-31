package com.example.foodielink;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import gun0912.tedimagepicker.builder.TedImagePicker;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    // Request codes for permissions and activities
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 102;

    // Firebase analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    // UI components
    private ImageView addProfilePhoto;
    private ImageView imgDish1, imgDish2, imgDish3;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    // Check camera permission and open camera if allowed
    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            openCameraDirectly();
        }
    }

    // Launch the camera intent
    private void openCameraDirectly() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraDirectly();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        1000);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000);
            }
        }

        // Handle system window insets (for scroll padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollViewRegisterAll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize image views
        addProfilePhoto = findViewById(R.id.profileImage);
        imgDish1 = findViewById(R.id.imgDish1);
        imgDish2 = findViewById(R.id.imgDish2);
        imgDish3 = findViewById(R.id.imgDish3);

        // Open camera on profile photo click
        addProfilePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });

        // Select dish images from gallery
        imgDish1.setOnClickListener(v -> {TedImagePicker.with(this).title("Select Dish 1 picture").
                start(uri -> {imgDish1.setImageURI(uri);});});
        imgDish2.setOnClickListener(v -> {TedImagePicker.with(this).title("Select Dish 2 picture").
                start(uri -> {imgDish2.setImageURI(uri);});});
        imgDish3.setOnClickListener(v -> {TedImagePicker.with(this).title("Select Dish 3 picture").
                start(uri -> {imgDish3.setImageURI(uri);});});

        // Form fields
        EditText edtUserName = findViewById(R.id.edtUserName2);
        EditText edtEmail = findViewById(R.id.edtEmailAddressLog2);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtAge = findViewById(R.id.edtAge2);
        EditText edtAboutMe = findViewById(R.id.edtAboutMe);

        // Location selection
        Button btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, PickLocationRegistrationActivity.class);
            startActivityForResult(intent, 1001);
        });

        // Dropdown spinners
        Spinner spinnerCooking = findViewById(R.id.spinner_cooking_preferences);
        ArrayAdapter<CharSequence> adapterCooking = ArrayAdapter.createFromResource(
                this, R.array.cooking_preferences, android.R.layout.simple_spinner_dropdown_item);
        spinnerCooking.setAdapter(adapterCooking);

        Spinner spinnerDietary = findViewById(R.id.spinner_dietary_preferences);
        ArrayAdapter<CharSequence> adapterDietary = ArrayAdapter.createFromResource(
                this, R.array.dietary_preferences, android.R.layout.simple_spinner_dropdown_item);
        spinnerDietary.setAdapter(adapterDietary);

        Spinner spinnerWhyHere = findViewById(R.id.spinner_why_here);
        ArrayAdapter<CharSequence> adapterWhyHere = ArrayAdapter.createFromResource(
                this, R.array.why_you_are_here, android.R.layout.simple_spinner_dropdown_item);
        spinnerWhyHere.setAdapter(adapterWhyHere);

        // Finish button (submit form)
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            // Read inputs
            String name = edtUserName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String age = edtAge.getText().toString().trim();
            String aboutMe = edtAboutMe.getText().toString().trim();
            String cooking = spinnerCooking.getSelectedItem().toString();
            String dietary = spinnerDietary.getSelectedItem().toString();
            String whyHere = spinnerWhyHere.getSelectedItem().toString();

            // Validate inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty() || aboutMe.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cooking.equals("Select") || dietary.equals("Select") || whyHere.equals("Select")) {
                Toast.makeText(this, "Please select your preferences", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register user with Firebase Authentication
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null) {
                            Toast.makeText(this, "FirebaseUser is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Log analytics event
                        Bundle analyticsBundle = new Bundle();
                        analyticsBundle.putString(FirebaseAnalytics.Param.METHOD, "email");
                        analyticsBundle.putString("user_email", email);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, analyticsBundle);

                        // Save user data to Firestore
                        String uid = firebaseUser.getUid();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("password", password);
                        userMap.put("age", Integer.parseInt(age));
                        userMap.put("aboutMe", aboutMe);
                        userMap.put("cookingPreference", cooking);
                        userMap.put("dietaryPreference", dietary);
                        userMap.put("whyHere", whyHere);
                        userMap.put("location", new GeoPoint(selectedLatitude, selectedLongitude));

                        FirebaseFirestore.getInstance()
                                .collection("Users")
                                .document(uid)
                                .set(userMap)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    // Handle results from camera, location picker, and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle camera result
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            addProfilePhoto.setImageBitmap(photo);
        }

        // Handle location result
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            selectedLatitude = data.getDoubleExtra("latitude", 0.0);
            selectedLongitude = data.getDoubleExtra("longitude", 0.0);
            Toast.makeText(this, "Selected location: " + selectedLatitude + ", " + selectedLongitude, Toast.LENGTH_LONG).show();
        }
    }
}
