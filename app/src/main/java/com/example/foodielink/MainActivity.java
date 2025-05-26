package com.example.foodielink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


// Main login screen for the application
public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private
    EditText edtEmailAddressLog, edtPasswordLog;
    Button btnLogin, btnCreateAccount;
    ImageView googleButton, facebookButton, xButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Sending an app opening event
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "app_start");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        // Initializing UI components
        edtEmailAddressLog = findViewById(R.id.edtEmailAddressLog);
        edtPasswordLog = findViewById(R.id.edtPasswordLog);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);
        xButton = findViewById(R.id.xButton);

        // Navigate to the registration screen when "Create Account" is clicked
        btnCreateAccount.setOnClickListener(view -> {
            //throw new RuntimeException("Trash Crash!");
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        // Google login via web
        googleButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signin"));
            startActivity(intent);
        });

        // Facebook login via web
        facebookButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login"));
            startActivity(intent);
        });

        // Twitter login via web (missing startActivity)
        xButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/login"));
            startActivity(intent);
        });

        // Navigate to the profile swipe page after login
        btnLogin.setOnClickListener(v -> {
            String email = edtEmailAddressLog.getText().toString().trim();
            String password = edtPasswordLog.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            // Sending an app Login event
                            Bundle loginBundle = new Bundle();
                            loginBundle.putString(FirebaseAnalytics.Param.METHOD, "email");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle);

                            Intent intent = new Intent(MainActivity.this, SwipePage.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "The email or password is incorrect", Toast.LENGTH_LONG).show();
                    });
        });
    }
}