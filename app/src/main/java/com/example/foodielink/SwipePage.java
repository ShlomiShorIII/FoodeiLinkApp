package com.example.foodielink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.yalantis.library.Koloda;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// Screen for swiping food dishes
public class SwipePage extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private SwipeAdapter adapter;
    private List<Integer> list;
    private Koloda koloda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);

        // Initializing Koloda swipe component
        koloda = findViewById(R.id.koloda);

        // Creating a list of food dish images
        list = new ArrayList<>();
        list.add(R.drawable.dish1);
        list.add(R.drawable.dish2);
        list.add(R.drawable.dish3);
        list.add(R.drawable.dish4);
        list.add(R.drawable.dish5);
        list.add(R.drawable.dish6);
        list.add(R.drawable.dish7);
        list.add(R.drawable.dish8);
        list.add(R.drawable.dish9);
        list.add(R.drawable.dish10);
        list.add(R.drawable.dish11);
        list.add(R.drawable.dish12);
        list.add(R.drawable.dish13);
        list.add(R.drawable.dish14);
        list.add(R.drawable.dish15);
        list.add(R.drawable.dish16);
        list.add(R.drawable.dish17);
        list.add(R.drawable.dish18);

        // Shuffling the list for a random order
        Collections.shuffle(list);

        // Setting up the adapter and attaching it to Koloda
        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);

        // Button to navigate to chat screen on click
        ImageView chatButton = findViewById(R.id.nav_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwipePage.this, ChatActivity.class);
                startActivity(intent);
            }
        });

    }

}
