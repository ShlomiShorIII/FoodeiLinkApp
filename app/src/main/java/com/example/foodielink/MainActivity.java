package com.example.foodielink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

// Main login screen for the application
public class MainActivity extends AppCompatActivity {

    EditText edtEmailAddressLog, edtPasswordLog;
    Button btnLogin, btnCreateAccount;
    ImageView googleButton, facebookButton, xButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SwipePage.class);
                startActivity(intent);
            }
        });

    }
}