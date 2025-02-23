package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Registration screen for creating a new account
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.register_activity);

        // Adjusting insets to fit system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollViewRegisterAll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing user input fields
        EditText edtUserName = findViewById(R.id.edtUserName2);
        EditText edtEmail = findViewById(R.id.edtEmailAddressLog2);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtAge = findViewById(R.id.edtAge2);
        EditText edtAboutMe = findViewById(R.id.edtAboutMe);

        // Spinner Cooking Preferences
        Spinner spinnerCooking = findViewById(R.id.spinner_cooking_preferences);
        ArrayAdapter<CharSequence> adapterCooking = ArrayAdapter.createFromResource(
                this,
                R.array.cooking_preferences,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCooking.setAdapter(adapterCooking);

        // Spinner Dietary Preferences
        Spinner spinnerDietary = findViewById(R.id.spinner_dietary_preferences);
        ArrayAdapter<CharSequence> adapterDietary = ArrayAdapter.createFromResource(
                this,
                R.array.dietary_preferences,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerDietary.setAdapter(adapterDietary);

        // Spinner Why You Are Here
        Spinner spinnerWhyHere = findViewById(R.id.spinner_why_here);
        ArrayAdapter<CharSequence> adapterWhyHere = ArrayAdapter.createFromResource(
                this,
                R.array.why_you_are_here,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerWhyHere.setAdapter(adapterWhyHere);

        // Finish Button
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
