package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView addProfilePhoto, imgDish1, imgDish2, imgDish3;
    private android.net.Uri currentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollViewRegisterAll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addProfilePhoto = findViewById(R.id.profileImage);
        imgDish1 = findViewById(R.id.imgDish1);
        imgDish2 = findViewById(R.id.imgDish2);
        imgDish3 = findViewById(R.id.imgDish3);

        addProfilePhoto.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .title("Select Profile Photo")
                    .start(uri -> {
                        addProfilePhoto.setImageURI(uri);
                        currentImageUri = uri;
                    });
        });

        imgDish1.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .title("Select Dish 1 Photo")
                    .start((OnSelectedListener) uri -> imgDish1.setImageURI(uri));
        });

        imgDish2.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .title("Select Dish 2 Photo")
                    .start((OnSelectedListener) uri -> imgDish2.setImageURI(uri));
        });

        imgDish3.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .title("Select Dish 3 Photo")
                    .start((OnSelectedListener) uri -> imgDish3.setImageURI(uri));
        });

        EditText edtUserName = findViewById(R.id.edtUserName2);
        EditText edtEmail = findViewById(R.id.edtEmailAddressLog2);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtAge = findViewById(R.id.edtAge2);
        EditText edtAboutMe = findViewById(R.id.edtAboutMe);

        Spinner spinnerCooking = findViewById(R.id.spinner_cooking_preferences);
        ArrayAdapter<CharSequence> adapterCooking = ArrayAdapter.createFromResource(
                this,
                R.array.cooking_preferences,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCooking.setAdapter(adapterCooking);

        Spinner spinnerDietary = findViewById(R.id.spinner_dietary_preferences);
        ArrayAdapter<CharSequence> adapterDietary = ArrayAdapter.createFromResource(
                this,
                R.array.dietary_preferences,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerDietary.setAdapter(adapterDietary);

        Spinner spinnerWhyHere = findViewById(R.id.spinner_why_here);
        ArrayAdapter<CharSequence> adapterWhyHere = ArrayAdapter.createFromResource(
                this,
                R.array.why_you_are_here,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerWhyHere.setAdapter(adapterWhyHere);

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            String name = edtUserName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String age = edtAge.getText().toString().trim();
            String aboutMe = edtAboutMe.getText().toString().trim();
            String cooking = spinnerCooking.getSelectedItem().toString();
            String dietary = spinnerDietary.getSelectedItem().toString();
            String whyHere = spinnerWhyHere.getSelectedItem().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty() || aboutMe.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cooking.equals("Select") || dietary.equals("Select") || whyHere.equals("Select")) {
                Toast.makeText(this, "Please select your preferences", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null) {
                            Toast.makeText(this, "FirebaseUser is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Sending an app Register event
                        Bundle analyticsBundle = new Bundle();
                        analyticsBundle.putString(FirebaseAnalytics.Param.METHOD, "email");
                        analyticsBundle.putString("user_email", email);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, analyticsBundle);

                        String uid = firebaseUser.getUid();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("age", age);
                        userMap.put("aboutMe", aboutMe);
                        userMap.put("cookingPreference", cooking);
                        userMap.put("dietaryPreference", dietary);
                        userMap.put("whyHere", whyHere);

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
}