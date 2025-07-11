package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics; // Analytics
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Request code for Google Sign-In
    private static final int RC_SIGN_IN = 1000;

    // Firebase components
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // UI components
    private EditText edtEmailAddressLog, edtPasswordLog;
    private Button btnLogin, btnCreateAccount;
    private ImageView googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.login_page);

        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Log app open event
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "app_start");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        // Initialize UI components
        edtEmailAddressLog = findViewById(R.id.edtEmailAddressLog);
        edtPasswordLog = findViewById(R.id.edtPasswordLog);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        googleButton = findViewById(R.id.googleButton);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Handle email/password login
        btnLogin.setOnClickListener(v -> {
            String email = edtEmailAddressLog.getText().toString().trim();
            String password = edtPasswordLog.getText().toString().trim();

            // Check if fields are empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sign in with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString(FirebaseAnalytics.Param.METHOD, "email");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle);
                        startActivity(new Intent(this, SwipePage.class));
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_LONG).show()
                    );
        });

        // Navigate to registration screen
        btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Handle Google Sign-In button click
        googleButton.setOnClickListener(v -> {
            // Force account chooser to show up by signing out first
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle result from Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Authenticate with Firebase using Google account
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get current Firebase user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        // Save user data to Firestore
                        saveUserToFirestore(firebaseUser);

                        // Log login event
                        Bundle loginBundle = new Bundle();
                        loginBundle.putString(FirebaseAnalytics.Param.METHOD, "google");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle);

                        startActivity(new Intent(this, SwipePage.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Firebase Auth failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save signed-in user to Firestore
    private void saveUserToFirestore(FirebaseUser user) {
        if (user == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(user.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                // Create user with default fields if new
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", user.getDisplayName());
                userData.put("email", user.getEmail());

                // Add default fields like in RegisterActivity
                userData.put("aboutMe", "");
                userData.put("cookingPreference", "");
                userData.put("dietaryPreference", "");
                userData.put("whyHere", "");

                userRef.set(userData, SetOptions.merge());
            }
        });
    }
}