package com.example.foodielink;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editAge, editAbout;
    private Spinner spinnerCooking, spinnerDietary, spinnerWhyHere;
    private Button btnSave;

    private FirebaseFirestore db; // Firestore
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Bind UI components
        editName = findViewById(R.id.edit_name);
        editAge = findViewById(R.id.edit_age);
        editAbout = findViewById(R.id.edit_about);

        spinnerCooking = findViewById(R.id.spinner_cooking_preference);
        spinnerDietary = findViewById(R.id.spinner_dietary_preference);
        spinnerWhyHere = findViewById(R.id.spinner_why_here);

        btnSave = findViewById(R.id.btn_save_profile);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Dropdown adapters
        ArrayAdapter<CharSequence> cookingAdapter = ArrayAdapter.createFromResource(this,
                R.array.cooking_preferences, android.R.layout.simple_spinner_item);
        cookingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCooking.setAdapter(cookingAdapter);

        ArrayAdapter<CharSequence> dietaryAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_preferences, android.R.layout.simple_spinner_item);
        dietaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDietary.setAdapter(dietaryAdapter);

        ArrayAdapter<CharSequence> whyHereAdapter = ArrayAdapter.createFromResource(this,
                R.array.why_you_are_here, android.R.layout.simple_spinner_item);
        whyHereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWhyHere.setAdapter(whyHereAdapter);

        loadUserData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    // Load current user data and populate UI fields
    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("Users").document(uid);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    editName.setText(documentSnapshot.getString("name"));

                    // Handle age safely
                    Long age = documentSnapshot.getLong("age");
                    if (age != null) {
                        editAge.setText(String.valueOf(age));
                    }

                    editAbout.setText(documentSnapshot.getString("aboutMe"));

                    setSpinnerSelection(spinnerCooking, documentSnapshot.getString("cookingPreference"));
                    setSpinnerSelection(spinnerDietary, documentSnapshot.getString("dietaryPreference"));
                    setSpinnerSelection(spinnerWhyHere, documentSnapshot.getString("whyHere"));
                }
            }
        });
    }

    // Set spinner selection based on value from Firestore
    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value == null) return;
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (value.equals(adapter.getItem(i))) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    // Save updated profile to Firestore
    private void saveProfile() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("Users").document(uid);

        String ageText = editAge.getText().toString().trim();
        Long ageValue = null;
        try {
            ageValue = Long.parseLong(ageText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age number", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef.update(
                "name", editName.getText().toString(),
                "age", ageValue,
                "aboutMe", editAbout.getText().toString(),
                "cookingPreference", spinnerCooking.getSelectedItem().toString(),
                "dietaryPreference", spinnerDietary.getSelectedItem().toString(),
                "whyHere", spinnerWhyHere.getSelectedItem().toString()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Show success message and close activity
                Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show error message
                Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
}
