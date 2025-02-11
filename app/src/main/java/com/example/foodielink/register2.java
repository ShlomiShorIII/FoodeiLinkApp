package com.example.foodielink;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class register2 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // CookingSpinner

        Spinner spinner = findViewById(R.id.spinner_cooking_preferences);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cooking_preferences, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // DietarySpinner

        Spinner dietarySpinner = findViewById(R.id.spinner_dietary_preferences);

        ArrayAdapter<CharSequence> dietaryAdapter = ArrayAdapter.createFromResource(this, R.array.dietary_preferences, android.R.layout.simple_spinner_dropdown_item);

        dietarySpinner.setAdapter(dietaryAdapter);

        // WhyYouAreHereSpinner

        Spinner whyhereSpinner = findViewById(R.id.spinner_why_here);

        ArrayAdapter<CharSequence> whyhereAdapter = ArrayAdapter.createFromResource(this, R.array.why_you_are_here, android.R.layout.simple_spinner_dropdown_item);

        whyhereSpinner.setAdapter(whyhereAdapter);



    }
}
